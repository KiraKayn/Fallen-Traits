package net.kayn.fallen_traits.mixin.minecraft;

import net.kayn.fallen_traits.content.traits.legendary.SizeTrait;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityDimensionsMixin {

    @Inject(
            method = "getDimensions",
            at = @At("RETURN"),
            cancellable = true
    )
    private void fallen_traits$sizeScale(
            Pose pose,
            CallbackInfoReturnable<EntityDimensions> cir
    ) {
        Entity entity = (Entity) (Object) this;
        float scale = SizeTrait.getScale(entity);

        if (scale == 1.0F) {
            return;
        }

        cir.setReturnValue(cir.getReturnValue().scale(scale));
    }
}