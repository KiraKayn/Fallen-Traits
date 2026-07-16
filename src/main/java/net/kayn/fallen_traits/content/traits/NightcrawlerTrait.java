package net.kayn.fallen_traits.content.traits;

import dev.xkmc.l2hostility.content.logic.TraitManager;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.kayn.fallen_traits.init.FTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

public class NightcrawlerTrait extends MobTrait {

    public NightcrawlerTrait(ChatFormatting style) {
        super(style);
    }

    @Override
    public void tick(LivingEntity mob, int level) {
        if (mob.level().isClientSide()) return;
        if (mob.tickCount % 20 != 0) return;
        boolean active = !mob.level().isDay();
        double dmg = active ? level * FTConfig.COMMON.nightcrawlerDamageBonusPerLevel.get() : 0;
        double spd = active ? level * FTConfig.COMMON.nightcrawlerSpeedBonusPerLevel.get() : 0;
        TraitManager.addAttribute(mob, Attributes.ATTACK_DAMAGE, "fallen_traits_nightcrawler_damage", dmg, AttributeModifier.Operation.MULTIPLY_TOTAL);
        TraitManager.addAttribute(mob, Attributes.MOVEMENT_SPEED, "fallen_traits_nightcrawler_speed", spd, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
        list.add(mapLevel(e -> Component.literal("+" + Math.round(e * FTConfig.COMMON.nightcrawlerDamageBonusPerLevel.get() * 100) + "%")
                .withStyle(ChatFormatting.AQUA)).append(CommonComponents.SPACE).append(
                Component.translatable(Attributes.ATTACK_DAMAGE.getDescriptionId()).withStyle(ChatFormatting.BLUE)));
        list.add(mapLevel(e -> Component.literal("+" + Math.round(e * FTConfig.COMMON.nightcrawlerSpeedBonusPerLevel.get() * 100) + "%")
                .withStyle(ChatFormatting.AQUA)).append(CommonComponents.SPACE).append(
                Component.translatable(Attributes.MOVEMENT_SPEED.getDescriptionId()).withStyle(ChatFormatting.BLUE)));
    }

}