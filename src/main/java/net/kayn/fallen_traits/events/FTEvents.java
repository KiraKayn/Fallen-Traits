package net.kayn.fallen_traits.events;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.kayn.fallen_traits.FallenTraits;
import net.kayn.fallen_traits.content.item.curio.RageGlove;
import net.kayn.fallen_traits.content.traits.CloneTrait;
import net.kayn.fallen_traits.content.traits.MimicTrait;
import net.kayn.fallen_traits.init.FTConfig;
import net.kayn.fallen_traits.init.FTItems;
import net.kayn.fallen_traits.init.FTTraits;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FallenTraits.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FTEvents {

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getAmount() <= 0) return;
        LivingEntity entity = event.getEntity();
        if (CurioCompat.hasItemInCurio(entity, FTItems.RAGE_GLOVE.get())) {
            RageGlove.STACKS.remove(entity.getUUID());
        }
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

}