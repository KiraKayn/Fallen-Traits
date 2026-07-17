package net.kayn.fallen_traits.mixin;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.kayn.fallen_traits.content.traits.BerserkTrait;
import net.kayn.fallen_traits.init.FTTraits;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RangedAttackGoal.class)
public class RangedAttackGoalMixin {

    @Final
    @Shadow
    private Mob mob;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;floor(F)I", ordinal = 0))
    private int fallen_traits$berserkReset(float value) {
        int level = fallen_traits$getBerserkLevel();
        return level > 0 ? BerserkTrait.getRangedIntervalTicks(level) : Mth.floor(value);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;floor(D)I", ordinal = 0))
    private int fallen_traits$berserkInitial(double value) {
        int level = fallen_traits$getBerserkLevel();
        return level > 0 ? BerserkTrait.getRangedIntervalTicks(level) : Mth.floor(value);
    }

    @Unique
    private int fallen_traits$getBerserkLevel() {
        if (!MobTraitCap.HOLDER.isProper(mob)) return 0;
        return MobTraitCap.HOLDER.get(mob).getTraitLevel(FTTraits.BERSERK.get());
    }

}