package net.kayn.fallen_traits.content.traits.legendary;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.content.capability.mob.CapStorageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.logic.TraitManager;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.kayn.fallen_traits.init.FTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

public class DevourerTrait extends LegendaryTrait {

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
        growMaxHealth(attacker, gain);
    }

    private void growMaxHealth(LivingEntity mob, float amount) {
        if (!mob.isAlive() || mob.isDeadOrDying()) return;
        MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
        Data data = cap.getOrCreateData(getRegistryName(), Data::new);
        data.bonusHealth += amount;
        TraitManager.addAttribute(mob, Attributes.MAX_HEALTH, "fallen_traits_devourer",
                data.bonusHealth, AttributeModifier.Operation.ADDITION);
        mob.heal(amount);
    }

    public static void grantOverheal(LivingEntity mob, float amount) {
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

    @SerialClass
    public static class Data extends CapStorageData {

        @SerialClass.SerialField
        public double bonusHealth = 0;

    }

}