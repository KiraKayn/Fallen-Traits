package net.kayn.fallen_traits.init;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.kayn.fallen_traits.FallenTraits;
import net.kayn.fallen_traits.content.traits.*;
import net.minecraft.ChatFormatting;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class FTTraits {

    public static final DeferredRegister<MobTrait> TRAITS =
            DeferredRegister.create(LHTraits.TRAITS.key(), FallenTraits.MOD_ID);

    public static final RegistryObject<RageTrait> RAGE = TRAITS.register("rage",
            () -> new RageTrait(ChatFormatting.RED));

    public static final RegistryObject<MimicTrait> MIMIC = TRAITS.register("mimic",
            () -> new MimicTrait(ChatFormatting.LIGHT_PURPLE));

    public static final RegistryObject<CloneTrait> CLONE = TRAITS.register("clone",
            () -> new CloneTrait(ChatFormatting.AQUA));

    public static final RegistryObject<CleanseTrait> CLEANSE = TRAITS.register("cleanse",
            () -> new CleanseTrait(ChatFormatting.YELLOW));

    public static final RegistryObject<DaywalkerTrait> DAYWALKER = TRAITS.register("daywalker",
            () -> new DaywalkerTrait(ChatFormatting.GOLD));

    public static final RegistryObject<NightcrawlerTrait> NIGHTCRAWLER = TRAITS.register("nightcrawler",
            () -> new NightcrawlerTrait(ChatFormatting.DARK_PURPLE));

    public static final RegistryObject<BerserkTrait> BERSERK = TRAITS.register("berserk",
            () -> new BerserkTrait(ChatFormatting.DARK_RED));

    public static void register() {
    }

}