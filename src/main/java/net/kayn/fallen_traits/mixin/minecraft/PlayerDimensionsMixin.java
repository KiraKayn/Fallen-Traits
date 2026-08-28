package net.kayn.fallen_traits.mixin.minecraft;

import net.kayn.fallen_traits.content.traits.legendary.SizeTrait;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerDimensionsMixin {

    @Unique
    private float fallen_traits$lastSizeScale = Float.NaN;

    @Inject(method = "tick", at = @At("TAIL"))
    private void fallen_traits$refreshDimensionsWhenScaleChanges(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        float currentScale = SizeTrait.getScale(player);

        if (Float.compare(currentScale, fallen_traits$lastSizeScale) != 0) {
            fallen_traits$lastSizeScale = currentScale;
            player.refreshDimensions();
        }
    }

    @Inject(method = "getDimensions", at = @At("RETURN"), cancellable = true)
    private void fallen_traits$sizeScale(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        Player player = (Player) (Object) this;
        float scale = SizeTrait.getScale(player);

        if (scale != 1.0F) {
            cir.setReturnValue(cir.getReturnValue().scale(scale));
        }
    }

    @Inject(method = "getStandingEyeHeight", at = @At("RETURN"), cancellable = true)
    private void fallen_traits$scaleEyeHeight(Pose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> cir) {
        Player player = (Player) (Object) this;
        if (player.getAttributes() == null) return;

        float scale = SizeTrait.getScale(player);
        if (scale != 1.0F) {
            cir.setReturnValue(cir.getReturnValue() * scale);
        }
    }
}