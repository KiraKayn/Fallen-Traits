package net.kayn.fallen_traits.mixin.soulsweaponry;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.kayn.fallen_traits.content.traits.BerserkTrait;
import net.kayn.fallen_traits.init.FTTraits;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.mobs.ReturningKnight;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ReturningKnightGoal.class, remap = false)
public abstract class ReturningKnightGoalMixin {

    @Shadow
    @Final
    private ReturningKnight boss;

    @Shadow
    private int attackCooldown;

    @Inject(method = "tick", at = @At("HEAD"))
    private void fallen_traits$berserk(CallbackInfo ci) {
        if (!MobTraitCap.HOLDER.isProper(this.boss)) {
            return;
        }

        int level = MobTraitCap.HOLDER
                .get(this.boss)
                .getTraitLevel(FTTraits.BERSERK.get());

        if (level <= 0) {
            return;
        }

        this.attackCooldown = Math.min(
                this.attackCooldown,
                BerserkTrait.getIntervalTicks(level)
        );
    }
}