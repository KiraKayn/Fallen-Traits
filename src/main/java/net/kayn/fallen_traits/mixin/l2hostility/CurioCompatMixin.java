package net.kayn.fallen_traits.mixin.l2hostility;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.kayn.fallen_traits.init.FTItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CurioCompat.class, remap = false)
public class CurioCompatMixin {

    @Inject(method = "hasItemInCurio", at = @At("HEAD"), cancellable = true)
    private static void fallen_traits$hasItemInCurio(LivingEntity player, Item item, CallbackInfoReturnable<Boolean> cir) {
        if (isLootingCharm(item)) {
            if (hasOmniscientCharm(player)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "hasItemInCurioChecked", at = @At("HEAD"), cancellable = true)
    private static void fallen_traits$hasItemInCurioChecked(LivingEntity le, Item item, CallbackInfoReturnable<Boolean> cir) {
        if (isLootingCharm(item)) {
            if (hasOmniscientCharm(le)) {
                cir.setReturnValue(true);
            }
        }
    }

    private static boolean isLootingCharm(Item item) {
        return item == LHItems.LOOT_1.get() ||
                item == LHItems.LOOT_2.get() ||
                item == LHItems.LOOT_3.get() ||
                item == LHItems.LOOT_4.get();
    }

    private static boolean hasOmniscientCharm(LivingEntity entity) {
        if (net.minecraftforge.fml.ModList.get().isLoaded("curios")) {
            var opt = top.theillusivec4.curios.api.CuriosApi.getCuriosInventory(entity);
            if (opt.resolve().isPresent()) {
                var handler = opt.resolve().get();
                var item = FTItems.OMNISCIENT_LOOTING_CHARM.get();

                for (var stacksHandler : handler.getCurios().values()) {
                    if (stacksHandler.getStacks().getSlots() > 0) {
                        for (int i = 0; i < stacksHandler.getStacks().getSlots(); i++) {
                            if (stacksHandler.getStacks().getStackInSlot(i).is(item)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}