package net.kayn.fallen_traits.mixin.attributeslib;

import dev.shadowsoffire.attributeslib.impl.AttributeEvents;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import net.kayn.fallen_traits.init.FTItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AttributeEvents.class, remap = false)
public abstract class AttributeEventsMixin {

    @Inject(method = "dodge(Lnet/minecraftforge/event/entity/living/LivingAttackEvent;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false)
    private void fallen_traits$preventMeleeDodgeWithHandOfCreation(LivingAttackEvent event, CallbackInfo ci) {
        Entity attacker = event.getSource().getDirectEntity();
        if (attacker instanceof LivingEntity livingAttacker) {
            if (CurioCompat.hasItemInCurio(livingAttacker, FTItems.HAND_OF_CREATION.get())) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "dodge(Lnet/minecraftforge/event/entity/ProjectileImpactEvent;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false)
    private void fallen_traits$preventProjectileDodgeWithHandOfCreation(ProjectileImpactEvent event, CallbackInfo ci) {
        Entity attacker = event.getProjectile().getOwner();
        if (attacker instanceof LivingEntity livingAttacker) {
            if (CurioCompat.hasItemInCurio(livingAttacker, FTItems.HAND_OF_CREATION.get())) {
                ci.cancel();
            }
        }
    }
}