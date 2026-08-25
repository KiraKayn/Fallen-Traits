package net.kayn.fallen_traits.mixin.l2hostility;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.logic.ItemPopulator;
import net.kayn.fallen_traits.init.FTItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemPopulator.class)
public class ItemPopulatorMixin {

    @Redirect(
            method = "populateArmors",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;setDropChance(Lnet/minecraft/world/entity/EquipmentSlot;F)V"
            ),
            remap = false
    )
    private static void fallen_traits$preventArmorDrop(Mob mob, EquipmentSlot slot, float chance) {
        if (!hasOmniscientCharm(mob)) {
            mob.setDropChance(slot, chance);
        } else {
            mob.setDropChance(slot, 0.0f);
        }
    }

    @Redirect(
            method = "populateWeapons",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;setDropChance(Lnet/minecraft/world/entity/EquipmentSlot;F)V"
            ),
            remap = false
    )
    private static void fallen_traits$preventWeaponDrop(Mob mob, EquipmentSlot slot, float chance) {
        if (!hasOmniscientCharm(mob)) {
            mob.setDropChance(slot, chance);
        } else {
            mob.setDropChance(slot, 0.0f);
        }
    }

    @Redirect(
            method = "populateSimpleWeapons",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;setDropChance(Lnet/minecraft/world/entity/EquipmentSlot;F)V"
            ),
            remap = false
    )
    private static void fallen_traits$preventSimpleWeaponDrop(Mob mob, EquipmentSlot slot, float chance) {
        if (!hasOmniscientCharm(mob)) {
            mob.setDropChance(slot, chance);
        } else {
            mob.setDropChance(slot, 0.0f);
        }
    }

    private static boolean hasOmniscientCharm(Mob mob) {
        if (mob.getKillCredit() instanceof Player player) {
            return CurioCompat.hasItemInCurio(player, FTItems.OMNISCIENT_LOOTING_CHARM.get());
        }

        var players = mob.level().getEntitiesOfClass(Player.class, mob.getBoundingBox().inflate(32.0));
        for (Player player : players) {
            if (CurioCompat.hasItemInCurio(player, FTItems.OMNISCIENT_LOOTING_CHARM.get())) {
                return true;
            }
        }

        return false;
    }
}