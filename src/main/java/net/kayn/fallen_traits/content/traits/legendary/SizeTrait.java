package net.kayn.fallen_traits.content.traits.legendary;

import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public abstract class SizeTrait extends LegendaryTrait {

    private static final String DIMENSIONS_UPDATED_TAG = "fallen_traits_size_dimensions_updated";
    private static final String TITAN_LEVEL_TAG = "fallen_traits_titan_level";
    private static final String DWARF_LEVEL_TAG = "fallen_traits_dwarf_level";

    public SizeTrait(ChatFormatting style) {
        super(style);
    }

    @Override
    public void initialize(LivingEntity mob, int level) {
        applyAttributes(mob, level);
        mob.getPersistentData().putInt(getLevelTag(), level);
        mob.refreshDimensions();
    }

    @Override
    public void postInit(LivingEntity mob, int level) {
        applyAttributes(mob, level);
        mob.getPersistentData().putInt(getLevelTag(), level);
        mob.refreshDimensions();
    }

    @Override
    public void tick(LivingEntity mob, int level) {
        int prev = mob.getPersistentData().getInt(getLevelTag());
        if (prev != level) {
            mob.getPersistentData().putInt(getLevelTag(), level);
            mob.getPersistentData().putBoolean(DIMENSIONS_UPDATED_TAG, false);
        }

        if (!mob.getPersistentData().getBoolean(DIMENSIONS_UPDATED_TAG)) {
            mob.refreshDimensions();
            mob.getPersistentData().putBoolean(DIMENSIONS_UPDATED_TAG, true);
        }
    }

    protected abstract void applyAttributes(LivingEntity mob, int level);

    protected abstract String getLevelTag();

    public static float getScale(Entity entity) {
        if (!(entity instanceof LivingEntity)) {
            return 1.0F;
        }

        float scale = 1.0F;

        int titanLevel = entity.getPersistentData().getInt(TITAN_LEVEL_TAG);
        if (titanLevel > 0) {
            scale *= (float) (1 + TitanTrait.sizeAt(titanLevel));
        }

        int dwarfLevel = entity.getPersistentData().getInt(DWARF_LEVEL_TAG);
        if (dwarfLevel > 0) {
            scale *= (float) (1 + DwarfTrait.sizeAt(dwarfLevel));
        }

        return scale;
    }

    public static double sizeAt(int level) {
        return 0;
    }
}