package net.kayn.fallen_traits.mixin.irons_spellbooks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import net.kayn.fallen_traits.content.traits.BerserkTrait;
import net.kayn.fallen_traits.init.FTTraits;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = WarlockAttackGoal.class)
public abstract class WarlockAttackGoalMixin extends WizardAttackGoal {

    public WarlockAttackGoalMixin(IMagicEntity abstractSpellCastingMob, double pSpeedModifier, int pAttackInterval) {
        super(abstractSpellCastingMob, pSpeedModifier, pAttackInterval);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextIntBetweenInclusive(II)I"))
    private int fallen_traits$berserk(RandomSource instance, int min, int max, Operation<Integer> original) {
        if (!MobTraitCap.HOLDER.isProper(mob)) return original.call(instance, min, max);
        int level = MobTraitCap.HOLDER.get(mob).getTraitLevel(FTTraits.BERSERK.get());
        if (level <= 0) return original.call(instance, min, max);
        double factor = BerserkTrait.getCooldownMultiplier(level);
        return original.call(instance, (int) Math.max(min * factor, 5), (int) Math.max(min * factor, 5));
    }

    @WrapOperation(method = "resetMeleeAttackInterval", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;floor(F)I"))
    private int fallen_traits$berserk(float value, Operation<Integer> original) {
        if (!MobTraitCap.HOLDER.isProper(mob)) return original.call(value);
        int level = MobTraitCap.HOLDER.get(mob).getTraitLevel(FTTraits.BERSERK.get());
        if (level <= 0) return level;
        double factor = BerserkTrait.getCooldownMultiplier(level);
        return original.call(value * (float) factor);
    }
}
