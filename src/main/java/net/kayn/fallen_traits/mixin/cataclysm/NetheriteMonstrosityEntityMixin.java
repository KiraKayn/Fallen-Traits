package net.kayn.fallen_traits.mixin.cataclysm;

import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.NewNetherite_Monstrosity.Netherite_Monstrosity_Entity;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.kayn.fallen_traits.content.traits.BerserkTrait;
import net.kayn.fallen_traits.init.FTTraits;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Netherite_Monstrosity_Entity.class, remap = false)
public abstract class NetheriteMonstrosityEntityMixin {

    @Shadow
    private int check_cooldown;
    @Shadow
    private int flare_shoot_cooldown;
    @Shadow
    private int overpower_cooldown;
    @Shadow
    private int shoot_cooldown;

    @Inject(method = "tick", at = @At("HEAD"))
    private void fallen_traits$berserk(CallbackInfo ci) {
        Netherite_Monstrosity_Entity self = (Netherite_Monstrosity_Entity) (Object) this;

        if (!MobTraitCap.HOLDER.isProper(self)) return;

        int level = MobTraitCap.HOLDER.get(self).getTraitLevel(FTTraits.BERSERK.get());
        if (level <= 0) return;

        int extra = BerserkTrait.getCataclysmExtraDecrement(level);

        check_cooldown = Math.max(0, check_cooldown - extra);
        flare_shoot_cooldown = Math.max(0, flare_shoot_cooldown - extra);
        overpower_cooldown = Math.max(0, overpower_cooldown - extra);
        shoot_cooldown = Math.max(0, shoot_cooldown - extra);
    }
}