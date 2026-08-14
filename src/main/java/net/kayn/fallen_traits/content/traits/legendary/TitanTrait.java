package net.kayn.fallen_traits.content.traits.legendary;

import dev.xkmc.l2hostility.content.logic.TraitManager;
import net.kayn.fallen_traits.content.traits.logic.TraitCompatibility;
import net.kayn.fallen_traits.init.FTConfig;
import net.kayn.fallen_traits.init.FTTraits;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

public class TitanTrait extends SizeTrait {

    private static final String LEVEL_TAG = "fallen_traits_titan_level";

    public TitanTrait(ChatFormatting style) {
        super(style);
        TraitCompatibility.register(this, FTTraits.DWARF);
    }

    @Override
    public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
        return super.allow(le, difficulty, maxModLv) && !TraitCompatibility.isIncompatible(this, le);
    }

    @Override
    public void postInit(LivingEntity mob, int lv) {
        super.postInit(mob, lv);
        TraitCompatibility.resolve(mob);
    }

    @Override
    protected void applyAttributes(LivingEntity mob, int level) {
        if (mob.level().isClientSide()) return;
        TraitManager.addAttribute(mob, Attributes.MAX_HEALTH, "fallen_traits_titan_health",
                healthAt(level), AttributeModifier.Operation.MULTIPLY_TOTAL);
        TraitManager.addAttribute(mob, Attributes.KNOCKBACK_RESISTANCE, "fallen_traits_titan_knockback",
                knockbackAt(level), AttributeModifier.Operation.ADDITION);
        mob.setHealth(mob.getMaxHealth());
    }

    @Override
    protected String getLevelTag() {
        return LEVEL_TAG;
    }

    public static double sizeAt(int level) {
        return FTConfig.COMMON.titanSizeBase.get() + FTConfig.COMMON.titanSizeStep.get() * (level - 1);
    }

    private double healthAt(int level) {
        return FTConfig.COMMON.titanHealthBase.get() + FTConfig.COMMON.titanHealthStep.get() * (level - 1);
    }

    private double knockbackAt(int level) {
        return FTConfig.COMMON.titanKnockbackBase.get() + FTConfig.COMMON.titanKnockbackStep.get() * (level - 1);
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(mapLevel(i -> Component.literal("+" + Math.round(sizeAt(i) * 100) + "%")
                .withStyle(ChatFormatting.AQUA)).append(CommonComponents.SPACE).append(
                Component.translatable("attribute.fallen_traits.size").withStyle(ChatFormatting.BLUE)));
        list.add(mapLevel(i -> Component.literal("+" + Math.round(healthAt(i) * 100) + "%")
                .withStyle(ChatFormatting.AQUA)).append(CommonComponents.SPACE).append(
                Component.translatable(Attributes.MAX_HEALTH.getDescriptionId()).withStyle(ChatFormatting.BLUE)));
        list.add(mapLevel(i -> Component.literal("+" + Math.round(knockbackAt(i) * 100) + "%")
                .withStyle(ChatFormatting.AQUA)).append(CommonComponents.SPACE).append(
                Component.translatable(Attributes.KNOCKBACK_RESISTANCE.getDescriptionId()).withStyle(ChatFormatting.BLUE)));
    }
}