package net.kayn.fallen_traits.mixin.cataclysm;

import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Ancient_Remnant.Ancient_Remnant_Entity;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.kayn.fallen_traits.content.traits.BerserkTrait;
import net.kayn.fallen_traits.init.FTTraits;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Ancient_Remnant_Entity.class, remap = false)
public abstract class AncientRemnantEntityMixin {

    @Shadow
    private int earthquake_cooldown;
    @Shadow
    private int hunting_cooldown;
    @Shadow
    private int monoltih_cooldown;
    @Shadow
    private int roar_cooldown;
    @Shadow
    private int stomp_cooldown;

    @Inject(method = "tick", at = @At("HEAD"))
    private void fallen_traits$berserk(CallbackInfo ci) {
        Ancient_Remnant_Entity self = (Ancient_Remnant_Entity) (Object) this;

        if (!MobTraitCap.HOLDER.isProper(self)) return;

        int level = MobTraitCap.HOLDER.get(self).getTraitLevel(FTTraits.BERSERK.get());
        if (level <= 0) return;

        int extra = BerserkTrait.getCataclysmExtraDecrement(level);

        earthquake_cooldown = Math.max(0, earthquake_cooldown - extra);
        hunting_cooldown = Math.max(0, hunting_cooldown - extra);
        monoltih_cooldown = Math.max(0, monoltih_cooldown - extra);
        roar_cooldown = Math.max(0, roar_cooldown - extra);
        stomp_cooldown = Math.max(0, stomp_cooldown - extra);
    }
}