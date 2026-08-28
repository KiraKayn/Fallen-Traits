package net.kayn.fallen_traits.mixin.minecraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import net.kayn.fallen_traits.content.item.curio.HandOfCreation;
import net.kayn.fallen_traits.init.FTConfig;
import net.kayn.fallen_traits.init.FTItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin {

    @WrapOperation(
            method = "tick(Lnet/minecraft/world/entity/LivingEntity;Ljava/lang/Runnable;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;isDurationEffectTick(II)Z")
    )
    private boolean fallen_traits$forceDotTick(MobEffect effect, int duration, int amplifier, Operation<Boolean> original, LivingEntity entity, Runnable onExpire) {
        if (effect.getCategory() == MobEffectCategory.HARMFUL) {
            LivingEntity owner = HandOfCreation.getEffectOwner(entity, FTConfig.COMMON.handOfCreationDotOwnerWindowTicks.get());
            if (owner != null && CurioCompat.hasItemInCurio(owner, FTItems.HAND_OF_CREATION.get())) {
                return true;
            }
        }
        return original.call(effect, duration, amplifier);
    }
}
