package net.kayn.fallen_traits.mixin.minecraft;

import net.kayn.fallen_traits.content.traits.legendary.SizeTrait;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private Entity entity;

    @Inject(method = "getMaxZoom", at = @At("RETURN"), cancellable = true)
    private void fallen_traits$scaleCameraDistance(double startingDistance, CallbackInfoReturnable<Double> cir) {
        if (this.entity instanceof LivingEntity living) {
            float scale = SizeTrait.getScale(living);
            if (scale != 1.0F && scale > 0.0F) {
                cir.setReturnValue(cir.getReturnValue() * scale);
            }
        }
    }
}