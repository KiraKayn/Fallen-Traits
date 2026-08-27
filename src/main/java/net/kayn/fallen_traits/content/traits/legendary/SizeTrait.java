package net.kayn.fallen_traits.content.traits.legendary;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import net.kayn.fallen_traits.client.SizeRenderContext;
import net.kayn.fallen_traits.init.FTMiscs;
import net.kayn.fallen_traits.init.FTTraits;
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
        mob.getPersistentData().putBoolean(DIMENSIONS_UPDATED_TAG, true);
    }

    @Override
    public void postInit(LivingEntity mob, int level) {
        applyAttributes(mob, level);
        mob.getPersistentData().putInt(getLevelTag(), level);
        mob.refreshDimensions();
        mob.getPersistentData().putBoolean(DIMENSIONS_UPDATED_TAG, true);
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

    public String getLevelTagPublic() {
        return getLevelTag();
    }

    public static float getScale(Entity entity) {
        if (!(entity instanceof LivingEntity living)) {
            return 1.0F;
        }
        if (SizeRenderContext.isInventoryPreview()) {
            return 1.0F;
        }

        float scale = 1.0F;

        boolean hasTitan = true;
        boolean hasDwarf = true;

        if (MobTraitCap.HOLDER.isProper(living)) {
            MobTraitCap cap = MobTraitCap.HOLDER.get(living);
            hasTitan = cap.traits.containsKey(FTTraits.TITAN.get());
            hasDwarf = cap.traits.containsKey(FTTraits.DWARF.get());
        }

        if (hasTitan) {
            int titanLevel = living.getPersistentData().getInt(TITAN_LEVEL_TAG);
            if (titanLevel > 0) {
                scale *= (float) (1.0D + TitanTrait.sizeAt(titanLevel));
            }
        }

        if (hasDwarf) {
            int dwarfLevel = living.getPersistentData().getInt(DWARF_LEVEL_TAG);
            if (dwarfLevel > 0) {
                scale *= (float) (1.0D + DwarfTrait.sizeAt(dwarfLevel));
            }
        }

        var attributes = living.getAttributes();
        if (attributes == null) {
            return scale;
        }

        var sizeAttribute = attributes.getInstance(FTMiscs.SIZE_SCALE.get());
        if (sizeAttribute != null) {
            scale *= (float) sizeAttribute.getValue();
        }

        return scale;
    }

    public static double sizeAt(int level) {
        return 0;
    }
}