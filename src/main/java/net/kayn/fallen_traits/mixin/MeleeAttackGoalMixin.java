package net.kayn.fallen_traits.mixin;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.kayn.fallen_traits.content.traits.BerserkTrait;
import net.kayn.fallen_traits.init.FTTraits;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MeleeAttackGoal.class)
public class MeleeAttackGoalMixin {

    @Final
    @Shadow
    protected PathfinderMob mob;

    @Inject(method = "getAttackInterval", at = @At("RETURN"), cancellable = true)
    private void fallen_traits$berserk(CallbackInfoReturnable<Integer> cir) {
        if (!MobTraitCap.HOLDER.isProper(mob)) return;
        int level = MobTraitCap.HOLDER.get(mob).getTraitLevel(FTTraits.BERSERK.get());
        if (level <= 0) return;
        cir.setReturnValue(BerserkTrait.getIntervalTicks(level));
    }

}