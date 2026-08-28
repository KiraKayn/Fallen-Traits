package net.kayn.fallen_traits.mixin.minecraft;

import net.kayn.fallen_traits.client.SizeRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {

    @Inject(
            method = "renderEntityInInventoryFollowsMouse",
            at = @At("HEAD")
    )
    private static void fallen_traits$beginUnscaledPreview(
            GuiGraphics graphics,
            int x,
            int y,
            int size,
            float mouseX,
            float mouseY,
            LivingEntity entity,
            CallbackInfo ci
    ) {
        SizeRenderContext.beginInventoryPreview();
    }

    @Inject(
            method = "renderEntityInInventoryFollowsMouse",
            at = @At("RETURN")
    )
    private static void fallen_traits$endUnscaledPreview(
            GuiGraphics graphics,
            int x,
            int y,
            int size,
            float mouseX,
            float mouseY,
            LivingEntity entity,
            CallbackInfo ci
    ) {
        SizeRenderContext.endInventoryPreview();
    }
}