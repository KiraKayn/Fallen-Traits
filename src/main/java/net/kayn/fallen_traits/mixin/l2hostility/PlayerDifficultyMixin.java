package net.kayn.fallen_traits.mixin.l2hostility;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import net.kayn.fallen_traits.init.FTItems;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerDifficulty.class, remap = false)
public abstract class PlayerDifficultyMixin {

    @Unique
    private Player fallen_traits$cachedPlayer;

    @ModifyArg(method = "apply(Ldev/xkmc/l2hostility/content/logic/MobDifficultyCollector;)V",
            at = @At(value = "INVOKE", target = "Ldev/xkmc/l2hostility/content/logic/MobDifficultyCollector;setPlayer(Lnet/minecraft/world/entity/player/Player;)V"))
    private Player fallen_traits$capturePlayer(Player player) {
        fallen_traits$cachedPlayer = player;
        return player;
    }

    @Inject(method = "apply(Ldev/xkmc/l2hostility/content/logic/MobDifficultyCollector;)V", at = @At("TAIL"))
    private void fallen_traits$applyFury(MobDifficultyCollector instance, CallbackInfo ci) {
        if (fallen_traits$cachedPlayer != null &&
                CurioCompat.hasItemInCurio(fallen_traits$cachedPlayer, FTItems.FURY_OF_INFERNAL.get())) {
            instance.setFullChance();
        }
    }

}