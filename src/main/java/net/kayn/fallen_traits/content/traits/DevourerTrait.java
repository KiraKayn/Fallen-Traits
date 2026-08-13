package net.kayn.fallen_traits.content.traits;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.kayn.fallen_traits.init.FTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class DevourerTrait extends MobTrait {

    public DevourerTrait(ChatFormatting style) {
        super(style);
    }

    @Override
    public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        var event = cache.getLivingHurtEvent();
        if (event == null || event.getAmount() <= 0) return;
        double percent = level * FTConfig.COMMON.devourerDrainPercentPerLevel.get();
        float gain = (float) (event.getAmount() * percent);
        if (gain <= 0) return;
        grantMaxHealth(attacker, gain);
    }

    public static void grantMaxHealth(LivingEntity mob, float amount) {
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