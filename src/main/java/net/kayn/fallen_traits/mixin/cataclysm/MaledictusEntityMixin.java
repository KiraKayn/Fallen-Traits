package net.kayn.fallen_traits.mixin.cataclysm;

import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Maledictus.Maledictus_Entity;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.kayn.fallen_traits.content.traits.BerserkTrait;
import net.kayn.fallen_traits.init.FTTraits;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Maledictus_Entity.class, remap = false)
public abstract class MaledictusEntityMixin {

    @Shadow
    private int charge_cooldown;
    @Shadow
    private int flyattack_cooldown;
    @Shadow
    private int grab_cooldown;
    @Shadow
    private int masseffect_cooldown;
    @Shadow
    private int radagon_cooldown;
    @Shadow
    private int spear_swing_cooldown;
    @Shadow
    private int spin_cooldown;
    @Shadow
    private int uppercut_cooldown;

    @Inject(method = "tick", at = @At("HEAD"))
    private void fallen_traits$berserk(CallbackInfo ci) {
        Maledictus_Entity self = (Maledictus_Entity) (Object) this;

        if (!MobTraitCap.HOLDER.isProper(self)) return;

        int level = MobTraitCap.HOLDER.get(self).getTraitLevel(FTTraits.BERSERK.get());
        if (level <= 0) return;

        int extra = BerserkTrait.getCataclysmExtraDecrement(level);

        charge_cooldown = Math.max(0, charge_cooldown - extra);
        flyattack_cooldown = Math.max(0, flyattack_cooldown - extra);
        grab_cooldown = Math.max(0, grab_cooldown - extra);
        masseffect_cooldown = Math.max(0, masseffect_cooldown - extra);
        radagon_cooldown = Math.max(0, radagon_cooldown - extra);
        spear_swing_cooldown = Math.max(0, spear_swing_cooldown - extra);
        spin_cooldown = Math.max(0, spin_cooldown - extra);
        uppercut_cooldown = Math.max(0, uppercut_cooldown - extra);
    }
}