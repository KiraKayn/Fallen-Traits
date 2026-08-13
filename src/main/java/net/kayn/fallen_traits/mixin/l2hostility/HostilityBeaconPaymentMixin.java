package net.kayn.fallen_traits.mixin.l2hostility;

import net.kayn.fallen_traits.init.FTItems;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets =
        "dev.xkmc.l2hostility.content.item.beacon.HostilityBeaconMenu$PaymentSlot")
public abstract class HostilityBeaconPaymentMixin {

    @Inject(
            method = "mayPlace",
            at = @At("RETURN"),
            cancellable = true
    )
    private void fallenTraits$acceptTranscendentIngot(
            ItemStack stack,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (stack.is(FTItems.TRANSCENDENT_INGOT.get())) {
            cir.setReturnValue(true);
        }
    }
}