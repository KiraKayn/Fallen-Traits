package net.kayn.fallen_traits.mixin.cataclysm;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.The_Leviathan.The_Leviathan_Entity;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.kayn.fallen_traits.content.traits.BerserkTrait;
import net.kayn.fallen_traits.init.FTTraits;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = The_Leviathan_Entity.class, remap = false)
public abstract class TheLeviathanEntityMixin {

    @Shadow
    private int bite_cooldown;
    @Shadow
    private int hunting_cooldown;
    @Shadow
    private int melee_cooldown;

    @Inject(method = "tick", at = @At("HEAD"))
    private void fallen_traits$berserk(CallbackInfo ci) {
        The_Leviathan_Entity self = (The_Leviathan_Entity) (Object) this;

        if (!MobTraitCap.HOLDER.isProper(self)) return;

        int level = MobTraitCap.HOLDER.get(self).getTraitLevel(FTTraits.BERSERK.get());
        if (level <= 0) return;

        int extra = BerserkTrait.getCataclysmExtraDecrement(level);

        bite_cooldown = Math.max(0, bite_cooldown - extra);
        hunting_cooldown = Math.max(0, hunting_cooldown - extra);
        melee_cooldown = Math.max(0, melee_cooldown - extra);
    }
}