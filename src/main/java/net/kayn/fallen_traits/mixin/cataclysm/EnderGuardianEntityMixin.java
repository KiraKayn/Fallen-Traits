package net.kayn.fallen_traits.mixin.cataclysm;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ender_Guardian_Entity;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.kayn.fallen_traits.content.traits.BerserkTrait;
import net.kayn.fallen_traits.init.FTTraits;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Ender_Guardian_Entity.class, remap = false)
public abstract class EnderGuardianEntityMixin {

    @Shadow
    private int stomp_cooldown;
    @Shadow
    private int teleport_cooldown;
    @Shadow
    private int teleport_smash_cooldown;
    @Shadow
    private int uppercut_cooldown;
    @Shadow
    private int vortexcooldown;

    @Inject(method = "tick", at = @At("HEAD"))
    private void fallen_traits$berserk(CallbackInfo ci) {
        Ender_Guardian_Entity self = (Ender_Guardian_Entity) (Object) this;

        if (!MobTraitCap.HOLDER.isProper(self)) return;

        int level = MobTraitCap.HOLDER.get(self).getTraitLevel(FTTraits.BERSERK.get());
        if (level <= 0) return;

        int extra = BerserkTrait.getCataclysmExtraDecrement(level);

        stomp_cooldown = Math.max(0, stomp_cooldown - extra);
        teleport_cooldown = Math.max(0, teleport_cooldown - extra);
        teleport_smash_cooldown = Math.max(0, teleport_smash_cooldown - extra);
        uppercut_cooldown = Math.max(0, uppercut_cooldown - extra);
        vortexcooldown = Math.max(0, vortexcooldown - extra);
    }

}