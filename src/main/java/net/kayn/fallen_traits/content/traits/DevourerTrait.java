package net.kayn.fallen_traits.content.traits;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.kayn.fallen_traits.init.FTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class DevourerTrait extends MobTrait {

    public DevourerTrait(ChatFormatting style) {
        super(style);
    }

    @Override
    public void tick(LivingEntity mob, int level) {
        if (mob.level().isClientSide()) return;
        int interval = FTConfig.COMMON.devourerDrainIntervalTicks.get();
        if (mob.tickCount % interval != 0) return;
        double radius = level * FTConfig.COMMON.devourerRadiusPerLevel.get();
        double percent = level * FTConfig.COMMON.devourerDrainPercentPerLevel.get();
        AABB box = mob.getBoundingBox().inflate(radius);
        for (Player player : mob.level().getEntitiesOfClass(Player.class, box, p -> !p.isSpectator() && !p.isCreative())) {
            if (player.distanceToSqr(mob) > radius * radius) continue;
            float drain = (float) (player.getHealth() * percent);
            if (drain <= 0) continue;
            player.hurt(mob.level().damageSources().magic(), drain);
            grantHealth(mob, drain);
        }
    }

    public static void grantHealth(LivingEntity mob, float amount) {
        if (!mob.isAlive() || mob.isDeadOrDying()) return;
        float max = mob.getMaxHealth();
        float newHealth = mob.getHealth() + amount;
        if (newHealth > max) {
            mob.setHealth(max);
            mob.setAbsorptionAmount(mob.getAbsorptionAmount() + (newHealth - max));
        } else {
            mob.setHealth(newHealth);
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        Component percent = mapLevel(i -> Component.literal(
                        Math.round(i * FTConfig.COMMON.devourerDrainPercentPerLevel.get() * 100) + "%")
                .withStyle(ChatFormatting.DARK_PURPLE));
        Component radius = mapLevel(i -> Component.literal(
                        "" + Math.round(i * FTConfig.COMMON.devourerRadiusPerLevel.get()))
                .withStyle(ChatFormatting.DARK_PURPLE));
        list.add(Component.translatable(getDescriptionId() + ".desc", percent, radius).withStyle(ChatFormatting.GRAY));
    }

}