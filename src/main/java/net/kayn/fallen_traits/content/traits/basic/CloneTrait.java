package net.kayn.fallen_traits.content.traits.basic;

import dev.xkmc.l2hostility.content.capability.mob.CapStorageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.InheritContext;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.events.MiscHandlers;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.kayn.fallen_traits.init.FTConfig;
import net.kayn.fallen_traits.init.FTTraits;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CloneTrait extends MobTrait {

    public static final String CLONE_TAG = "fallen_traits_is_clone";
    public static final String CLONE_SPAWN_TICK_TAG = "fallen_traits_clone_spawn_tick";
    public static final String CLONE_ORIGINAL_LEVEL_TAG = "fallen_traits_clone_original_level";
    public static final String CLONE_LAST_EXPLODE_TAG = "fallen_traits_clone_last_explode";
    public static final String CLONE_OWNER_TAG = "fallen_traits_clone_owner";
    private static final String CLONE_TEAM = "fallen_traits_clone_glow";

    public CloneTrait(ChatFormatting style) {
        super(style);
    }

    @Override
    public void tick(LivingEntity mob, int level) {
        if (mob.level().isClientSide()) return;
        if (!(mob instanceof Mob m)) return;
        if (m.getTarget() == null) return;
        if (!(mob.level() instanceof ServerLevel sl)) return;

        MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
        Data data = cap.getOrCreateData(getRegistryName(), Data::new);
        data.clones.removeIf(id -> {
            Entity e = sl.getEntity(id);
            return e == null || !e.isAlive();
        });

        int maxAlive = FTConfig.COMMON.cloneMaxAlive.get();
        int interval = FTConfig.COMMON.cloneSpawnIntervalTicks.get();
        if (data.clones.size() >= maxAlive) return;
        if (mob.tickCount - data.lastSpawnTick < interval) return;

        UUID id = spawnClone(m, sl);
        if (id != null) {
            data.clones.add(id);
            data.lastSpawnTick = mob.tickCount;
        }
    }

    private UUID spawnClone(Mob original, ServerLevel sl) {
        Entity raw = original.getType().create(sl);
        if (!(raw instanceof Mob clone)) return null;
        clone.moveTo(original.getX(), original.getY(), original.getZ(), original.getYRot(), original.getXRot());
        sl.addFreshEntity(clone);

        MiscHandlers.copyCap(original, clone);

        MobTrait split = LHTraits.TRAITS.get().getValue(new ResourceLocation("l2hostility", "split"));
        if (split != null) {
            MobTraitCap.HOLDER.get(clone).removeTrait(split);
        }

        double health = FTConfig.COMMON.cloneHealth.get();
        var healthAttr = clone.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttr != null) {
            for (var mod : new ArrayList<>(healthAttr.getModifiers())) {
                healthAttr.removeModifier(mod.getId());
            }
            healthAttr.setBaseValue(health);
        }
        clone.setHealth((float) health);

        clone.getPersistentData().putBoolean(CLONE_TAG, true);
        clone.getPersistentData().putLong(CLONE_SPAWN_TICK_TAG, clone.level().getGameTime());
        clone.getPersistentData().putUUID(CLONE_OWNER_TAG, original.getUUID());

        if (MobTraitCap.HOLDER.isProper(original)) {
            int originalLevel = MobTraitCap.HOLDER.get(original).getLevel();
            clone.getPersistentData().putInt(CLONE_ORIGINAL_LEVEL_TAG, originalLevel);
        }

        if (FTConfig.COMMON.cloneGlowEnabled.get()) {
            clone.setGlowingTag(true);
            var scoreboard = sl.getScoreboard();
            PlayerTeam team = scoreboard.getPlayerTeam(CLONE_TEAM);
            if (team == null) {
                team = scoreboard.addPlayerTeam(CLONE_TEAM);
                team.setColor(ChatFormatting.AQUA);
            }
            scoreboard.addPlayerToTeam(clone.getScoreboardName(), team);
        }

        if (original.getTarget() != null) {
            clone.setTarget(original.getTarget());
        }

        return clone.getUUID();
    }

    @Override
    public int inherited(MobTraitCap cap, int rank, InheritContext ctx) {
        cap.noDrop = true;
        return 0;
    }

    public static void explode(LivingEntity clone) {
        if (!(clone.level() instanceof ServerLevel sl)) return;

        long now = sl.getGameTime();
        long lastExplode = clone.getPersistentData().getLong(CLONE_LAST_EXPLODE_TAG);
        if (now - lastExplode < 5) {
            clone.discard();
            return;
        }
        clone.getPersistentData().putLong(CLONE_LAST_EXPLODE_TAG, now);

        double baseDamage = FTConfig.COMMON.cloneExplosionDamage.get();
        double radius = FTConfig.COMMON.cloneExplosionRadius.get();
        double levelScale = FTConfig.COMMON.cloneExplosionLevelScale.get();
        double healthCap = FTConfig.COMMON.cloneExplosionMaxHealthPercentCap.get();

        int originalLevel = clone.getPersistentData().getInt(CLONE_ORIGINAL_LEVEL_TAG);
        double scaledDamage = baseDamage + (originalLevel * levelScale);

        UUID ownerId = clone.getPersistentData().contains(CLONE_OWNER_TAG)
                ? clone.getPersistentData().getUUID(CLONE_OWNER_TAG)
                : null;

        Vec3 pos = clone.position().add(0, clone.getBbHeight() * 0.5, 0);

        sl.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE,
                2.0F, (1.0F + (sl.random.nextFloat() - sl.random.nextFloat()) * 0.2F) * 0.7F);
        sl.sendParticles(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);

        AABB box = new AABB(pos, pos).inflate(radius);
        for (LivingEntity target : sl.getEntitiesOfClass(LivingEntity.class, box,
                e -> e != clone && e.isAlive() && !e.isSpectator()
                        && (ownerId == null || !e.getUUID().equals(ownerId))
                        && !e.getPersistentData().getBoolean(CLONE_TAG)
                        && !(MobTraitCap.HOLDER.isProper(e) && MobTraitCap.HOLDER.get(e).hasTrait(FTTraits.CLONE.get())))) {
            double dist = target.position().distanceTo(pos);
            if (dist > radius) continue;

            double falloff = 1.0 - (dist / radius);
            float damage = (float) (scaledDamage * falloff);

            float maxAllowed = (float) (target.getMaxHealth() * healthCap);
            damage = Math.min(damage, maxAllowed);

            if (damage <= 0.5f) continue;

            target.hurt(sl.damageSources().explosion(clone, clone), damage);

            Vec3 dir = target.position().subtract(pos);
            if (dir.lengthSqr() > 0.001) {
                dir = dir.normalize();
                target.setDeltaMovement(target.getDeltaMovement().add(dir.x * 0.3, 0.2, dir.z * 0.3));
            }
        }

        clone.discard();
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
    }

    @SerialClass
    public static class Data extends CapStorageData {

        @SerialClass.SerialField
        public final ArrayList<UUID> clones = new ArrayList<>();

        @SerialClass.SerialField
        public int lastSpawnTick = -100000;

    }

}