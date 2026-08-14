package net.kayn.fallen_traits.mixin.traveloptics;

import com.gametechbc.traveloptics.entity.mobs.nightwarden_boss.NightwardenAttackGoal;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import net.kayn.fallen_traits.content.traits.legendary.BerserkTrait;
import net.kayn.fallen_traits.init.FTTraits;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NightwardenAttackGoal.class, remap = false)
public abstract class NightwardenAttackGoalMixin extends WizardAttackGoal {

    public NightwardenAttackGoalMixin(IMagicEntity abstractSpellCastingMob, double pSpeedModifier, int pAttackInterval) {
        super(abstractSpellCastingMob, pSpeedModifier, pAttackInterval);
    }

    @Shadow
    private int scytheUltimateCooldown;

    @Shadow
    private int bigSlamCooldown;

    @Shadow
    private int bigSlamClonesCooldown;

    @Shadow
    private int scytheGroundSlamCloneCooldown;

    @Inject(method = "tickAttackCooldowns", at = @At("HEAD"))
    private void fallen_traits$berserk(CallbackInfo ci) {
        if (!MobTraitCap.HOLDER.isProper(mob)) return;
        int level = MobTraitCap.HOLDER.get(mob).getTraitLevel(FTTraits.BERSERK.get());
        if (level <= 0) return;
        int extra = BerserkTrait.getCataclysmExtraDecrement(level);
        if (scytheUltimateCooldown > 0) scytheUltimateCooldown = Math.max(0, scytheUltimateCooldown - extra);
        if (bigSlamCooldown > 0) bigSlamCooldown = Math.max(0, bigSlamCooldown - extra);
        if (bigSlamClonesCooldown > 0) bigSlamClonesCooldown = Math.max(0, bigSlamClonesCooldown - extra);
        if (scytheGroundSlamCloneCooldown > 0) scytheGroundSlamCloneCooldown = Math.max(0, scytheGroundSlamCloneCooldown - extra);
    }

}