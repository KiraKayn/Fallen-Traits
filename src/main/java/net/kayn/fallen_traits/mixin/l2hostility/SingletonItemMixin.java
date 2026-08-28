package net.kayn.fallen_traits.mixin.l2hostility;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.item.curio.core.SingletonItem;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.kayn.fallen_traits.content.item.curio.HandOfCreation;
import net.kayn.fallen_traits.content.item.curio.WrathOfFenrir;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotContext;

@Mixin(value = SingletonItem.class, remap = false)
public abstract class SingletonItemMixin {

    @Inject(method = "canEquip", at = @At("RETURN"), cancellable = true)
    private void fallen_traits$blockIncompatible(SlotContext slotContext, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) return;

        Item item = (Item) (Object) this;
        LivingEntity wearer = slotContext.entity();

        if (fallen_traits$isRestrictedHandOfCreation(item)) {
            boolean hasHandOfCreation = !CurioCompat.getItems(
                    wearer, equipped -> equipped.getItem() instanceof HandOfCreation
            ).isEmpty();
            if (hasHandOfCreation) {
                cir.setReturnValue(false);
                return;
            }
        }

        if (item == LHItems.CURSE_WRATH.get()) {
            boolean hasWrathOfFenrir = !CurioCompat.getItems(
                    wearer, equipped -> equipped.getItem() instanceof WrathOfFenrir
            ).isEmpty();
            if (hasWrathOfFenrir) {
                cir.setReturnValue(false);
            }
        }
    }

    private static boolean fallen_traits$isRestrictedHandOfCreation(Item item) {
        return item == LHItems.FLAMING_THORN.get() ||
                item == LHItems.IMAGINE_BREAKER.get() ||
                item == LHItems.PLATINUM_STAR.get() ||
                item == LHItems.INFINITY_GLOVE.get();
    }
}