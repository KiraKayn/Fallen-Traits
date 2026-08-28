package net.kayn.fallen_traits.events;

import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.xkmc.l2damagetracker.init.data.ArmorEffectConfig;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.TraitManager;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.kayn.fallen_traits.FallenTraits;
import net.kayn.fallen_traits.content.item.curio.HandOfCreation;
import net.kayn.fallen_traits.content.item.curio.RageGlove;
import net.kayn.fallen_traits.content.item.curio.WrathOfFenrir;
import net.kayn.fallen_traits.content.traits.basic.CloneTrait;
import net.kayn.fallen_traits.content.traits.legendary.DevourerTrait;
import net.kayn.fallen_traits.content.traits.legendary.MimicTrait;
import net.kayn.fallen_traits.init.FTConfig;
import net.kayn.fallen_traits.init.FTItems;
import net.kayn.fallen_traits.init.FTTraits;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.rtxyd.fallen.lib.runtime.forgemod.util.EntityCakyHandler;
import net.rtxyd.fallen.lib.util.IObjectCaky;

@Mod.EventBusSubscriber(modid = FallenTraits.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FTEvents {

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getAmount() <= 0) return;
        EntityCakyHandler.resolveWith(event.getEntity(), RageGlove.GLOVE_STACK_KEY, IObjectCaky.Type.MANUAL, e -> new RageGlove.Stack(), e -> 1);
        EntityCakyHandler.resolveWith(event.getEntity(), HandOfCreation.STACK_KEY, IObjectCaky.Type.MANUAL, e -> new HandOfCreation.Stack(), e -> 1);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onMobDeathLustMimicGuard(LivingDeathEvent event) {
        if (FTConfig.COMMON.allowLustToDropMimicEquipment.get()) return;
        if (!(event.getEntity() instanceof Mob mob)) return;
        var credit = mob.getKillCredit();
        if (credit == null || !CurioCompat.hasItemInCurio(credit, LHItems.CURSE_LUST.get())) return;
        if (!MobTraitCap.HOLDER.isProper(mob)) return;
        MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
        if (!cap.hasTrait(FTTraits.MIMIC.get())) return;
        MimicTrait.Data data = cap.getData(FTTraits.MIMIC.get().getRegistryName());
        if (data == null) return;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (data.copiedSlots.contains(slot.getName())) {
                mob.setDropChance(slot, 0);
            }
        }
    }

    @SubscribeEvent
    public static void onCloneDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (!entity.getPersistentData().getBoolean(CloneTrait.CLONE_TAG)) return;
        CloneTrait.explode(entity);
    }

    @SubscribeEvent
    public static void onCloneTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (!entity.getPersistentData().getBoolean(CloneTrait.CLONE_TAG)) return;
        int lifetime = FTConfig.COMMON.cloneLifetimeTicks.get();
        if (lifetime <= 0) return;
        long spawnTick = entity.getPersistentData().getLong(CloneTrait.CLONE_SPAWN_TICK_TAG);
        if (entity.level().getGameTime() - spawnTick >= lifetime) {
            entity.discard();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onCloneDamaged(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (!entity.getPersistentData().getBoolean(CloneTrait.CLONE_TAG)) return;

        // Clones always die in one hit regardless of damage
        event.setAmount(Float.MAX_VALUE);
    }

    @SubscribeEvent
    public static void onWearerHurt(LivingHurtEvent event) {
        if (event.getAmount() <= 0) return;
        LivingEntity entity = event.getEntity();
        if (CurioCompat.hasItemInCurio(entity, FTItems.INVULNERABILITY_BREAKER.get())) {
            FTItems.INVULNERABILITY_BREAKER.get().onWearerHurt(entity);
        }
        if (CurioCompat.hasItemInCurio(entity, FTItems.HAND_OF_CREATION.get())) {
            FTItems.HAND_OF_CREATION.get().onWearerHurt(entity);
        }
    }

    @SubscribeEvent
    public static void onDevourerHeal(LivingHealEvent event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) return;
        float amount = event.getAmount();
        if (amount <= 0) return;
        double maxRadius = FTConfig.COMMON.devourerRadiusPerLevel.get() * TraitManager.getMaxLevel();
        AABB box = target.getBoundingBox().inflate(maxRadius);
        for (LivingEntity le : target.level().getEntitiesOfClass(LivingEntity.class, box,
                e -> e != target && e.isAlive() && MobTraitCap.HOLDER.isProper(e))) {
            MobTraitCap cap = MobTraitCap.HOLDER.get(le);
            int lvl = cap.getTraitLevel(FTTraits.DEVOURER.get());
            if (lvl <= 0) continue;
            double radius = lvl * FTConfig.COMMON.devourerRadiusPerLevel.get();
            if (le.distanceToSqr(target) > radius * radius) continue;
            event.setCanceled(true);
            DevourerTrait.grantOverheal(le, amount);
            break;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerAttacked(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!player.level().isClientSide() && CurioCompat.hasItemInCurio(player, FTItems.PHASEWALKERS_RING.get())) {
                double dodgeChance = player.getAttributeValue(ALObjects.Attributes.DODGE_CHANCE.get());
                Entity attacker = event.getSource().getEntity();

                if (dodgeChance > 0 && player.getRandom().nextDouble() < dodgeChance) {
                    if (teleportBehind(player, attacker)) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!player.level().isClientSide() && CurioCompat.hasItemInCurio(player, FTItems.PHASEWALKERS_RING.get())) {
                double dodgeChance = player.getAttributeValue(ALObjects.Attributes.DODGE_CHANCE.get());
                Entity attacker = event.getSource().getEntity();

                if (dodgeChance > 0 && player.getRandom().nextDouble() < dodgeChance) {
                    if (teleportBehind(player, attacker)) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onProjectileHit(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof net.minecraft.world.phys.EntityHitResult entityHit) {
            if (entityHit.getEntity() instanceof Player player) {
                if (!player.level().isClientSide() && CurioCompat.hasItemInCurio(player, FTItems.PHASEWALKERS_RING.get())) {
                    double dodgeChance = player.getAttributeValue(ALObjects.Attributes.DODGE_CHANCE.get());
                    Entity attacker = event.getProjectile().getOwner();

                    if (dodgeChance > 0 && player.getRandom().nextDouble() < dodgeChance) {
                        if (teleportBehind(player, attacker)) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    private static boolean teleportBehind(LivingEntity entity, Entity attacker) {
        int range = 8;
        if (!entity.level().isClientSide() && entity.isAlive() && range > 0) {
            Vec3 target;
            if (attacker != null) {
                Vec3 attackerPos = attacker.position();
                Vec3 entityPos = entity.position();
                Vec3 direction = entityPos.subtract(attackerPos).normalize();
                target = attackerPos.subtract(direction.scale(2));
            } else {
                double d0 = entity.getX() + (entity.getRandom().nextDouble() - 0.5D) * range * 2;
                double d1 = entity.getY() + (double) (entity.getRandom().nextInt(range * 2) - range);
                double d2 = entity.getZ() + (entity.getRandom().nextDouble() - 0.5D) * range * 2;
                target = new Vec3(d0, d1, d2);
            }
            return teleport(entity, target.x, target.y, target.z);
        } else {
            return false;
        }
    }

    private static boolean teleport(LivingEntity entity, double pX, double pY, double pZ) {
        BlockPos.MutableBlockPos ipos = new BlockPos.MutableBlockPos(pX, pY, pZ);
        while (ipos.getY() > entity.level().getMinBuildHeight() && !entity.level().getBlockState(ipos).blocksMotion()) {
            ipos.move(Direction.DOWN);
        }

        BlockState blockstate = entity.level().getBlockState(ipos);
        boolean flag = blockstate.blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            EntityTeleportEvent.EnderEntity event = ForgeEventFactory.onEnderTeleport(entity, pX, pY, pZ);
            if (event.isCanceled()) return false;
            Vec3 vec3 = entity.position();
            boolean flag2 = entity.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2) {
                entity.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(entity));
                if (!entity.isSilent()) {
                    entity.level().playSound(null, entity.xo, entity.yo, entity.zo, SoundEvents.ENDERMAN_TELEPORT, entity.getSoundSource(), 1.0F, 1.0F);
                    entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }
            return flag2;
        } else {
            return false;
        }
    }
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onAttackIgnoreInvuln(LivingAttackEvent event) {
        Entity direct = event.getSource().getEntity();
        if (direct instanceof LivingEntity attacker && CurioCompat.hasItemInCurio(attacker, FTItems.HAND_OF_CREATION.get())) {
            event.getEntity().invulnerableTime = 0;
            return;
        }
        LivingEntity owner = resolveEffectOwner(event.getEntity(), event.getSource());
        if (owner != null && CurioCompat.hasItemInCurio(owner, FTItems.HAND_OF_CREATION.get())) {
            event.getEntity().invulnerableTime = 0;
        }
    }

    private static LivingEntity resolveEffectOwner(LivingEntity target, net.minecraft.world.damagesource.DamageSource source) {
        if (source.getEntity() instanceof LivingEntity le) return le;
        for (var effect : target.getActiveEffectsMap().values()) {
            LivingEntity owner = HandOfCreation.getEffectOwner(target, effect.getEffect());
            if (owner != null) return owner;
        }
        return null;
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        WrathOfFenrir.restoreExpiredTraits(event.getEntity());
    }
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPotionTest(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();

        checkImmunity(entity, event, FTItems.WRATH_OF_FENRIR.get());
        checkImmunity(entity, event, FTItems.TITANS_HEART.get());
    }

    private static void checkImmunity(LivingEntity entity, MobEffectEvent.Applicable event, Item item) {
        if (CurioCompat.hasItemInCurio(entity, item)) {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
            if (id == null) return;
            var immunitySet = ArmorEffectConfig.get().getImmunity(id.toString());
            if (immunitySet != null && immunitySet.contains(event.getEffectInstance().getEffect())) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}