package net.kayn.fallen_traits.mixin.minecraft;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import net.kayn.fallen_traits.content.item.SpawnRateModifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NaturalSpawner.class)
public abstract class NaturalSpawnerMixin {

    @Unique
    private static final ThreadLocal<Boolean> FALLEN_TRAITS_EXTRA_PASS =
            ThreadLocal.withInitial(() -> false);

    @Inject(method = "spawnForChunk", at = @At("RETURN"))
    private static void fallen_traits$extraNaturalSpawnPasses(
            ServerLevel level,
            LevelChunk chunk,
            NaturalSpawner.SpawnState state,
            boolean spawnFriendlies,
            boolean spawnEnemies,
            boolean spawnPersistent,
            CallbackInfo ci
    ) {
        if (FALLEN_TRAITS_EXTRA_PASS.get()) return;

        double x = chunk.getPos().getMiddleBlockX();
        double z = chunk.getPos().getMiddleBlockZ();
        double radiusSqr = 128.0 * 128.0;
        float bonus = 0.0F;

        for (ServerPlayer player : level.players()) {
            double dx = player.getX() - x;
            double dz = player.getZ() - z;
            if (dx * dx + dz * dz > radiusSqr) continue;

            float playerBonus = 0.0F;
            var stacks = CurioCompat.getItems(player,
                    stack -> stack.getItem() instanceof SpawnRateModifier);
            for (ItemStack stack : stacks) {
                if (stack.getItem() instanceof SpawnRateModifier modifier) {
                    playerBonus += Math.max(0.0F,
                            modifier.getSpawnRateMultiplier());
                }
            }
            bonus = Math.max(bonus, playerBonus);
        }

        if (bonus <= 0.0F) return;

        int extraPasses = (int) Math.floor(bonus);
        float fractionalPass = bonus - extraPasses;
        if (level.random.nextFloat() < fractionalPass) {
            extraPasses++;
        }
        if (extraPasses <= 0) return;

        FALLEN_TRAITS_EXTRA_PASS.set(true);
        try {
            for (int i = 0; i < extraPasses; i++) {
                NaturalSpawner.spawnForChunk(
                        level, chunk, state,
                        spawnFriendlies, spawnEnemies, spawnPersistent);
            }
        } finally {
            FALLEN_TRAITS_EXTRA_PASS.remove();
        }
    }
}