package net.kayn.fallen_traits.content.traits.legendary;

import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import net.kayn.fallen_traits.content.traits.logic.TraitCompatibility;
import net.kayn.fallen_traits.init.FTConfig;
import net.kayn.fallen_traits.init.FTTraits;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class BerserkTrait extends LegendaryTrait {

    private static final int[] INTERVALS = {10, 5, 0};

    public BerserkTrait(ChatFormatting style) {
        super(style);
        TraitCompatibility.register(this, FTTraits.INFERNAL);
    }

    public static int getIntervalTicks(int level) {
        return INTERVALS[Math.min(Math.max(level, 1), INTERVALS.length) - 1];
    }

    public static int getCataclysmExtraDecrement(int level) {
        return level * FTConfig.COMMON.berserkCataclysmDecrementPerLevel.get();
    }

    public static int getRangedIntervalTicks(int level) {
        return Math.max(1, getIntervalTicks(level));
    }

    public static double getCooldownMultiplier(int level) {
        return Math.max(1d - level * FTConfig.COMMON.berserkISSFactorPerLevel.get(), 0.1);
    }

    @Override
    public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
        return super.allow(le, difficulty, maxModLv) && !TraitCompatibility.isIncompatible(this, le);
    }

    @Override
    public void postInit(LivingEntity mob, int lv) {
        TraitCompatibility.resolve(mob);
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
        list.add(mapLevel(e -> {
            int interval = getIntervalTicks(e);
            String desc = interval <= 0 ? "Instant" : String.format("%.1fx Faster", 20.0 / interval);
            return Component.literal(desc).withStyle(ChatFormatting.RED);
        }));
    }

}