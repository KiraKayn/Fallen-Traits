package net.kayn.fallen_traits.mixin.cataclysm;

import com.github.L_Ender.cataclysm.client.render.entity.Scylla_Renderer;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Scylla.Scylla_Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kayn.fallen_traits.content.traits.legendary.SizeTrait;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Scylla_Renderer.class, remap = false)
public abstract class ScyllaRendererMixin {

    @Inject(
            method = "render*",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/github/L_Ender/cataclysm/client/render/entity/Scylla_Renderer;scale(Lcom/github/L_Ender/cataclysm/entity/InternalAnimationMonster/IABossMonsters/Scylla/Scylla_Entity;Lcom/mojang/blaze3d/vertex/PoseStack;F)V",
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private void fallen_traits$scaleScylla(
            Scylla_Entity entity,
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