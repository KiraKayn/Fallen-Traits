package net.kayn.fallen_traits.mixin.cataclysm;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.kayn.fallen_traits.content.traits.BerserkTrait;
import net.kayn.fallen_traits.init.FTTraits;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Ignis_Entity.class, remap = false)
public abstract class IgnisEntityMixin {

    @Shadow
    private int air_smash_cooldown;
    @Shadow
    private int body_check_cooldown;
    @Shadow
    private int poke_cooldown;
    @Shadow
    private int counter_strike_cooldown;
    @Shadow
    private int horizontal_small_swing_cooldown;
    @Shadow
    private int horizontal_swing_cooldown;
    @Shadow
    private int magic_cooldown;
    @Shadow
    private int earth_shudders_cooldown;
    @Shadow
    private int sword_dance_cooldown;
    @Shadow
    private int reinforced_smash_cooldown;
    @Shadow
    private int ultimate_cooldown;

    @Inject(method = "tick", at = @At("HEAD"))
    private void fallen_traits$berserk(CallbackInfo ci) {
        Ignis_Entity self = (Ignis_Entity) (Object) this;
        if (!MobTraitCap.HOLDER.isProper(self)) return;
        int level = MobTraitCap.HOLDER.get(self).getTraitLevel(FTTraits.BERSERK.get());
        if (level <= 0) return;
        int extra = BerserkTrait.getCataclysmExtraDecrement(level);
        air_smash_cooldown = Math.max(0, air_smash_cooldown - extra);
        body_check_cooldown = Math.max(0, body_check_cooldown - extra);
        poke_cooldown = Math.max(0, poke_cooldown - extra);
        counter_strike_cooldown = Math.max(0, counter_strike_cooldown - extra);
        horizontal_small_swing_cooldown = Math.max(0, horizontal_small_swing_cooldown - extra);
        horizontal_swing_cooldown = Math.max(0, horizontal_swing_cooldown - extra);
        magic_cooldown = Math.max(0, magic_cooldown - extra);
        earth_shudders_cooldown = Math.max(0, earth_shudders_cooldown - extra);
        sword_dance_cooldown = Math.max(0, sword_dance_cooldown - extra);
        reinforced_smash_cooldown = Math.max(0, reinforced_smash_cooldown - extra);
        ultimate_cooldown = Math.max(0, ultimate_cooldown - extra);
    }

}