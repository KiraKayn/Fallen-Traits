package net.kayn.fallen_traits.init;

import net.kayn.fallen_traits.FallenTraits;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class FTTags {

    public static final TagKey<dev.xkmc.l2hostility.content.traits.base.MobTrait> NO_OVER_MAX =
            TagKey.create(
                    dev.xkmc.l2hostility.init.registrate.LHTraits.TRAITS.key(),
                    new ResourceLocation(FallenTraits.MOD_ID, "no_over_max")
            );

    public static void register() {}
}