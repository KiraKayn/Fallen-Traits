package net.kayn.fallen_traits.mixin.geckolib;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.kayn.fallen_traits.content.traits.legendary.SizeTrait;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Mixin(value = GeoEntityRenderer.class, remap = false)
public abstract class GeoEntityRendererMixin {

    @Inject(
            method = "actuallyRender*",
            at = @At("HEAD"),
            remap = false
    )
    private void fallen_traits$scaleGeoModel(
            PoseStack poseStack,
            GeoAnimatable animatable,
            BakedGeoModel model,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            boolean isReRender,
            float partialTick,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha,
            CallbackInfo ci
    ) {
        if (animatable instanceof net.minecraft.world.entity.Entity entity) {
            float scale = SizeTrait.getScale(entity);
            if (scale != 1.0F) {
                poseStack.scale(scale, scale, scale);
            }
        }
    }
}