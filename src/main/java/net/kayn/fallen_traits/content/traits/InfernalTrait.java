package net.kayn.fallen_traits.content.traits;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.capability.mob.CapStorageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.PlayerFinder;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.logic.TraitManager;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.kayn.fallen_traits.content.traits.logic.TraitCompatibility;
import net.kayn.fallen_traits.init.FTConfig;
import net.kayn.fallen_traits.init.FTTraits;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;

import java.util.List;
import java.util.UUID;

public class InfernalTrait extends LegendaryTrait {

    private static final String ENRAGE_TEAM = "fallen_traits_infernal_glow";
    private static final UUID SPEED_MODIFIER_ID = MathHelper.getUUIDFromString("fallen_traits_infernal_speed");

    public InfernalTrait(ChatFormatting style) {
        super(style);
        TraitCompatibility.register(this, FTTraits.BERSERK);
    }

    @Override
    public void initialize(LivingEntity le, int level) {
        TraitManager.addAttribute(le, Attributes.FOLLOW_RANGE, "fallen_traits_infernal_follow_range",
                2048, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void tick(LivingEntity le, int level) {
        if (le.level().isClientSide()) return;
        if (!(le instanceof Mob mob)) return;
        long time = mob.level().getGameTime();
        MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
        Data data = cap.getOrCreateData(getRegistryName(), Data::new);

        if (data.enrageEndTick > 0) {
            if (time < data.enrageEndTick) {
                handleEnraged(mob, data, time);
                return;
            }
            endEnrage(mob, data);
        }
        if (time < data.cooldownEndTick) return;

        Player target = resolveTarget(mob, data);
        if (target == null) {
            data.outOfRangeTicks = 0;
            return;
        }
        if (mob.getTarget() != target) {
            mob.setTarget(target);
        }

        double distance = FTConfig.COMMON.infernalTriggerDistanceBase.get()
                - level * FTConfig.COMMON.infernalTriggerDistancePerLevel.get();
        int duration = FTConfig.COMMON.infernalTriggerDurationBaseTicks.get()
                + level * FTConfig.COMMON.infernalTriggerDurationPerLevelTicks.get();

        if (target.distanceTo(mob) > distance) {
            data.outOfRangeTicks++;
        } else {
            data.outOfRangeTicks = 0;
        }
        if (data.outOfRangeTicks >= duration) {
            startEnrage(mob, level, data, time);
        }
    }

    private Player resolveTarget(Mob mob, Data data) {
        LivingEntity target = mob.getTarget();
        if (target instanceof Player player) {
            data.lastTargetId = player.getUUID();
            return player;
        }
        if (data.lastTargetId != null) {
            Player player = mob.level().getPlayerByUUID(data.lastTargetId);
            if (player != null && player.isAlive() && player.level() == mob.level()) {
                return player;
            }
            data.lastTargetId = null;
        }
        Player nearest = PlayerFinder.getNearestPlayer(mob.level(), mob);
        if (nearest != null) {
            data.lastTargetId = nearest.getUUID();
        }
        return nearest;
    }

    private void startEnrage(Mob mob, int level, Data data, long time) {
        data.outOfRangeTicks = 0;
        data.enrageEndTick = time + FTConfig.COMMON.infernalEnrageDurationTicks.get();
        data.lastLungeTick = time;
        data.damageFactor = (float) (1 + level * FTConfig.COMMON.infernalDamageBonusPerLevel.get());
        data.level = level;

        var speedAttr = mob.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttr != null) {
            if (speedAttr.getModifier(SPEED_MODIFIER_ID) != null) {
                speedAttr.removeModifier(SPEED_MODIFIER_ID);
            }
            double bonus = level * FTConfig.COMMON.infernalSpeedBonusPerLevel.get();
            speedAttr.addTransientModifier(new AttributeModifier(SPEED_MODIFIER_ID,
                    "fallen_traits_infernal_speed", bonus, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }

        mob.setGlowingTag(true);

        data.originalName = mob.getCustomName();
        data.originalNameVisible = mob.isCustomNameVisible();

        Component baseName = data.originalName != null
                ? data.originalName.copy()
                : mob.getType().getDescription();

        Component enragedName = Component.translatable(
                getDescriptionId() + ".enraged_name",
                baseName
        ).withStyle(ChatFormatting.RED, ChatFormatting.BOLD);

        mob.setCustomName(enragedName);
        mob.setCustomNameVisible(true);

        if (mob.level() instanceof ServerLevel sl) {
            var scoreboard = sl.getScoreboard();
            PlayerTeam team = scoreboard.getPlayerTeam(ENRAGE_TEAM);
            if (team == null) {
                team = scoreboard.addPlayerTeam(ENRAGE_TEAM);
                team.setColor(ChatFormatting.GOLD);
            }
            scoreboard.addPlayerToTeam(mob.getScoreboardName(), team);
            sl.playSound(null, mob.getX(), mob.getY(), mob.getZ(),
                    SoundEvents.RAVAGER_ROAR, SoundSource.HOSTILE, 2f, 0.7f);
        }

        int radius = FTConfig.COMMON.infernalActionbarRadius.get();
        Component msg = baseName.copy().withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
                .append(Component.translatable(getDescriptionId() + ".enrage_suffix")
                        .withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        for (Player p : mob.level().getEntitiesOfClass(Player.class, mob.getBoundingBox().inflate(radius))) {
            p.displayClientMessage(msg, true);
        }
    }

    private void endEnrage(Mob mob, Data data) {
        data.enrageEndTick = -1;
        data.damageFactor = 1f;
        int cooldownTicks = FTConfig.COMMON.infernalCooldownBaseTicks.get()
                - (data.level - 1) * FTConfig.COMMON.infernalCooldownPerLevelTicks.get();
        data.cooldownEndTick = mob.level().getGameTime() + Math.max(0, cooldownTicks);

        var speedAttr = mob.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttr != null && speedAttr.getModifier(SPEED_MODIFIER_ID) != null) {
            speedAttr.removeModifier(SPEED_MODIFIER_ID);
        }
        mob.setGlowingTag(false);

        mob.setCustomName(data.originalName);
        mob.setCustomNameVisible(data.originalNameVisible);

        data.originalName = null;
        data.originalNameVisible = false;
        if (mob.level() instanceof ServerLevel sl) {
            sl.getScoreboard().removePlayerFromTeam(mob.getScoreboardName());
        }
    }

    private void handleEnraged(Mob mob, Data data, long time) {
        Player target = resolveTarget(mob, data);
        if (target == null) return;
        int interval = FTConfig.COMMON.infernalLungeIntervalTicks.get();
        if (time - data.lastLungeTick < interval) return;
        data.lastLungeTick = time;

        Vec3 dir = target.position().subtract(mob.position());
        if (dir.horizontalDistanceSqr() < 0.01) return;
        dir = new Vec3(dir.x, 0, dir.z).normalize();
        double strength = FTConfig.COMMON.infernalLungeStrength.get();
        mob.setDeltaMovement(mob.getDeltaMovement().add(dir.x * strength, 0.4, dir.z * strength));
        mob.hasImpulse = true;

        if (mob.level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.FLAME,
                    mob.getX(), mob.getY() + mob.getBbHeight() * 0.5, mob.getZ(),
                    20, mob.getBbWidth() * 0.5, mob.getBbHeight() * 0.5, mob.getBbWidth() * 0.5, 0.02);
        }
    }

    @Override
    public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        var event = cache.getLivingHurtEvent();
        if (event == null || event.getAmount() <= 0) return;
        MobTraitCap cap = MobTraitCap.HOLDER.get(attacker);
        Data data = cap.getData(getRegistryName());
        if (data != null && data.damageFactor > 1f) {
            cache.addHurtModifier(DamageModifier.multBase(data.damageFactor));
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
        list.add(mapLevel(i -> {
            double secs = (FTConfig.COMMON.infernalTriggerDurationBaseTicks.get() + i * FTConfig.COMMON.infernalTriggerDurationPerLevelTicks.get()) / 20.0;
            return Component.literal(formatSeconds(secs) + "s").withStyle(ChatFormatting.GOLD);
        }).append(CommonComponents.SPACE).append(
                Component.translatable(getDescriptionId() + ".duration").withStyle(ChatFormatting.RED)));
        list.add(mapLevel(i -> {
            double secs = (FTConfig.COMMON.infernalCooldownBaseTicks.get() - (i - 1) * FTConfig.COMMON.infernalCooldownPerLevelTicks.get()) / 20.0;
            return Component.literal(formatSeconds(secs) + "s").withStyle(ChatFormatting.GOLD);
        }).append(CommonComponents.SPACE).append(
                Component.translatable(getDescriptionId() + ".cooldown").withStyle(ChatFormatting.RED)));
        list.add(mapLevel(i -> {
            double dist = FTConfig.COMMON.infernalTriggerDistanceBase.get() - i * FTConfig.COMMON.infernalTriggerDistancePerLevel.get();
            return Component.literal((int) dist + " blocks").withStyle(ChatFormatting.GOLD);
        }).append(CommonComponents.SPACE).append(
                Component.translatable(getDescriptionId() + ".trigger_range").withStyle(ChatFormatting.RED)));
        list.add(mapLevel(i -> Component.literal("+" + Math.round(i * FTConfig.COMMON.infernalDamageBonusPerLevel.get() * 100) + "%")
                .withStyle(ChatFormatting.GOLD)).append(CommonComponents.SPACE).append(
                Component.translatable(Attributes.ATTACK_DAMAGE.getDescriptionId()).withStyle(ChatFormatting.RED)));
        list.add(mapLevel(i -> Component.literal("+" + Math.round(i * FTConfig.COMMON.infernalSpeedBonusPerLevel.get() * 100) + "%")
                .withStyle(ChatFormatting.GOLD)).append(CommonComponents.SPACE).append(
                Component.translatable(Attributes.MOVEMENT_SPEED.getDescriptionId()).withStyle(ChatFormatting.RED)));
    }

    private static String formatSeconds(double val) {
        return val == Math.floor(val) ? String.valueOf((int) val) : String.valueOf(val);
    }

    @Override
    public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
        return super.allow(le, difficulty, maxModLv) && !TraitCompatibility.isIncompatible(this, le);
    }

    @SerialClass
    public static class Data extends CapStorageData {

        @SerialClass.SerialField
        public int outOfRangeTicks = 0;

        @SerialClass.SerialField
        public long enrageEndTick = -1;

        @SerialClass.SerialField
        public long cooldownEndTick = 0;

        @SerialClass.SerialField
        public long lastLungeTick = -100000;

        @SerialClass.SerialField
        public float damageFactor = 1f;

        @SerialClass.SerialField
        public UUID lastTargetId = null;

        @SerialClass.SerialField
        public int level = 1;

        public Component originalName = null;
        public boolean originalNameVisible = false;
    }

}