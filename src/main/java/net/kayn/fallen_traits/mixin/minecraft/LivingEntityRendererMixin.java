package net.kayn.fallen_traits.mixin.minecraft;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kayn.fallen_traits.content.traits.legendary.SizeTrait;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @Inject(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FF" +
                    "Lcom/mojang/blaze3d/vertex/PoseStack;" +
                    "Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;" +
                            "scale(Lnet/minecraft/world/entity/LivingEntity;" +
                            "Lcom/mojang/blaze3d/vertex/PoseStack;F)V",
                    shift = At.Shift.AFTER
            )
    )
    private void fallen_traits$scaleSizeModel(
            LivingEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            CallbackInfo ci
    ) {
        float scale = SizeTrait.getScale(entity);

        if (scale != 1.0F) {
            poseStack.scale(scale, scale, scale);
        }
    }
}