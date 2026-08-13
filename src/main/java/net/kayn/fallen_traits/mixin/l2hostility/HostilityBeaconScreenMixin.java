package net.kayn.fallen_traits.mixin.l2hostility;

import dev.xkmc.l2hostility.content.item.beacon.HostilityBeaconScreen;
import net.kayn.fallen_traits.FallenTraits;
import net.kayn.fallen_traits.init.FTItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HostilityBeaconScreen.class)
public abstract class HostilityBeaconScreenMixin {

    @Shadow
    @Final
    @Mutable
    static ResourceLocation BEACON_LOCATION;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void fallenTraits$replaceBeaconTexture(CallbackInfo ci) {
        BEACON_LOCATION = new ResourceLocation(
                FallenTraits.MOD_ID,
                "textures/gui/container/beacon.png"
        );
    }

    @ModifyArg(
            method = "renderBg(Lnet/minecraft/client/gui/GuiGraphics;FII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem" +
                            "(Lnet/minecraft/world/item/ItemStack;II)V"
            ),
            index = 1
    )
    private int fallenTraits$movePaymentIconsLeft(int originalX) {
        return originalX - 22;
    }

    @Inject(
            method = "renderBg(Lnet/minecraft/client/gui/GuiGraphics;FII)V",
            at = @At("TAIL")
    )
    private void fallenTraits$renderTranscendentIngot(
            GuiGraphics graphics,
            float partialTick,
            int mouseX,
            int mouseY,
            CallbackInfo ci
    ) {
        HostilityBeaconScreen screen =
                (HostilityBeaconScreen) (Object) this;

        int left = (screen.width - 230) / 2;
        int top = (screen.height - 219) / 2;

        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 100.0F);

        graphics.renderItem(
                new ItemStack(FTItems.TRANSCENDENT_INGOT.get()),
                left + 108,
                top + 109
        );

        graphics.pose().popPose();
    }
}