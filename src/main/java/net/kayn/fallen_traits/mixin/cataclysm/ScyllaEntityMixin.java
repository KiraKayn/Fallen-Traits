package net.kayn.fallen_traits.mixin.cataclysm;

import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Scylla.Scylla_Entity;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.kayn.fallen_traits.content.traits.BerserkTrait;
import net.kayn.fallen_traits.init.FTTraits;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Scylla_Entity.class, remap = false)
public abstract class ScyllaEntityMixin {

    @Shadow private int anchor_pull_cooldown;
    @Shadow private int back_step_cooldown;
    @Shadow private int explosion_cooldown;
    @Shadow private int flying_cooldown;
    @Shadow private int parry_cooldown;
    @Shadow private int spear_cooldown;
    @Shadow private int spin_cooldown;
    @Shadow private int summon_snake_cooldown;
    @Shadow private int thundercloud_cooldown;
    @Shadow private int wave_cooldown;
    @Shadow private int whip_cooldown;

    @Inject(method = "tick", at = @At("HEAD"))
    private void fallen_traits$berserk(CallbackInfo ci) {
        Scylla_Entity self = (Scylla_Entity) (Object) this;

        if (!MobTraitCap.HOLDER.isProper(self)) return;

        int level = MobTraitCap.HOLDER.get(self).getTraitLevel(FTTraits.BERSERK.get());
        if (level <= 0) return;

        int extra = BerserkTrait.getCataclysmExtraDecrement(level);

        anchor_pull_cooldown = Math.max(0, anchor_pull_cooldown - extra);
        back_step_cooldown = Math.max(0, back_step_cooldown - extra);
        explosion_cooldown = Math.max(0, explosion_cooldown - extra);
        flying_cooldown = Math.max(0, flying_cooldown - extra);
        parry_cooldown = Math.max(0, parry_cooldown - extra);
        spear_cooldown = Math.max(0, spear_cooldown - extra);
        spin_cooldown = Math.max(0, spin_cooldown - extra);
        summon_snake_cooldown = Math.max(0, summon_snake_cooldown - extra);
        thundercloud_cooldown = Math.max(0, thundercloud_cooldown - extra);
        wave_cooldown = Math.max(0, wave_cooldown - extra);
        whip_cooldown = Math.max(0, whip_cooldown - extra);
    }
}