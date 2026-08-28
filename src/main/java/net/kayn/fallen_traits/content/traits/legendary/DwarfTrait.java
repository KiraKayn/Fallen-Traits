package net.kayn.fallen_traits.content.traits.legendary;

import dev.shadowsoffire.attributeslib.api.ALObjects;
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

public class DwarfTrait extends SizeTrait {

    private static final String LEVEL_TAG = "fallen_traits_dwarf_level";

    public DwarfTrait(ChatFormatting style) {
        super(style);
        TraitCompatibility.register(this, FTTraits.TITAN);
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

        TraitManager.addAttribute(mob, Attributes.MOVEMENT_SPEED, "fallen_traits_dwarf_speed",
                speedAt(level), AttributeModifier.Operation.MULTIPLY_TOTAL);

        TraitManager.addAttribute(mob, ALObjects.Attributes.DODGE_CHANCE.get(), "fallen_traits_dwarf_dodge",
                dodgeAt(level), AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void disableNonHealthAttributes(LivingEntity mob) {
        if (mob.level().isClientSide()) return;
        TraitManager.addAttribute(mob, Attributes.MOVEMENT_SPEED, "fallen_traits_dwarf_speed",
                0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        TraitManager.addAttribute(mob, ALObjects.Attributes.DODGE_CHANCE.get(), "fallen_traits_dwarf_dodge",
                0, AttributeModifier.Operation.ADDITION);
    }

    @Override
    protected String getLevelTag() {
        return LEVEL_TAG;
    }

    public static double sizeAt(int level) {
        return -(FTConfig.COMMON.dwarfSizeBase.get() + FTConfig.COMMON.dwarfSizeStep.get() * (level - 1));
    }

    private double speedAt(int level) {
        return FTConfig.COMMON.dwarfSpeedBase.get() + FTConfig.COMMON.dwarfSpeedStep.get() * (level - 1);
    }

    private double dodgeAt(int level) {
        return FTConfig.COMMON.dwarfDodgeBase.get() + FTConfig.COMMON.dwarfDodgeStep.get() * (level - 1);
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(mapLevel(i -> Component.literal("-" + Math.round(Math.abs(sizeAt(i)) * 100) + "%")
                .withStyle(ChatFormatting.AQUA)).append(CommonComponents.SPACE).append(
                Component.translatable("attribute.fallen_traits.size").withStyle(ChatFormatting.BLUE)));
        list.add(mapLevel(i -> Component.literal("+" + Math.round(speedAt(i) * 100) + "%")
                .withStyle(ChatFormatting.AQUA)).append(CommonComponents.SPACE).append(
                Component.translatable(Attributes.MOVEMENT_SPEED.getDescriptionId()).withStyle(ChatFormatting.BLUE)));
        list.add(mapLevel(i -> Component.literal("+" + Math.round(dodgeAt(i) * 100) + "%")
                .withStyle(ChatFormatting.AQUA)).append(CommonComponents.SPACE).append(
                Component.translatable(ALObjects.Attributes.DODGE_CHANCE.get().getDescriptionId()).withStyle(ChatFormatting.BLUE)));
    }
}