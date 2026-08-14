package net.kayn.fallen_traits.mixin.traveloptics;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.gametechbc.traveloptics.api.entity.ai.WarlockAttackGoal;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import net.kayn.fallen_traits.content.traits.legendary.BerserkTrait;
import net.kayn.fallen_traits.init.FTTraits;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = WarlockAttackGoal.class, remap = false)
public abstract class TOWarlockAttackGoalMixin extends WizardAttackGoal {

    public TOWarlockAttackGoalMixin(IMagicEntity abstractSpellCastingMob, double pSpeedModifier, int pAttackInterval) {
        super(abstractSpellCastingMob, pSpeedModifier, pAttackInterval);
    }

    @WrapOperation(method = "resetMeleeAttackInterval", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;floor(F)I"))
    private int fallen_traits$berserk(float value, Operation<Integer> original) {
        if (!MobTraitCap.HOLDER.isProper(mob)) return original.call(value);
        int level = MobTraitCap.HOLDER.get(mob).getTraitLevel(FTTraits.BERSERK.get());
        if (level <= 0) return original.call(value);
        double factor = BerserkTrait.getCooldownMultiplier(level);
        return original.call(value * (float) factor);
    }

}