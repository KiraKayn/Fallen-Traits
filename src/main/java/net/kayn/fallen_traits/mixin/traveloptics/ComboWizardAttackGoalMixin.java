package net.kayn.fallen_traits.mixin.traveloptics;

import com.gametechbc.traveloptics.api.entity.ai.ComboWizardAttackGoal;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.kayn.fallen_traits.content.traits.BerserkTrait;
import net.kayn.fallen_traits.init.FTTraits;
import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ComboWizardAttackGoal.class, remap = false)
public abstract class ComboWizardAttackGoalMixin {

    @Shadow
    protected int attackTime;

    @Final
    @Shadow
    protected PathfinderMob mob;

    @Inject(method = "resetAttackTimer", at = @At("RETURN"))
    private void fallen_traits$berserk(double distanceSquared, CallbackInfo ci) {
        if (!MobTraitCap.HOLDER.isProper(mob)) return;
        int level = MobTraitCap.HOLDER.get(mob).getTraitLevel(FTTraits.BERSERK.get());
        if (level <= 0) return;
        double factor = BerserkTrait.getCooldownMultiplier(level);
        attackTime = Math.max(1, (int) Math.round(attackTime * factor));
    }

}