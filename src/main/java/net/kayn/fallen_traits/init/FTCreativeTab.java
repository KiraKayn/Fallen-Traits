package net.kayn.fallen_traits.init;

import net.kayn.fallen_traits.FallenTraits;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class FTCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FallenTraits.MOD_ID);

    public static final RegistryObject<CreativeModeTab> FALLEN_TRAITS_TAB = CREATIVE_MODE_TABS.register("fallen_traits_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.fallen_traits_tab"))
                    .icon(() -> new ItemStack(FTItems.RAGE_GLOVE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(FTItems.RAGE_SYMBOL.get());
                        output.accept(FTItems.RAGE_GLOVE.get());
                        output.accept(FTItems.MIMIC_SYMBOL.get());
                        output.accept(FTItems.CLONE_SYMBOL.get());
                        output.accept(FTItems.CLEANSE_SYMBOL.get());
                        output.accept(FTItems.NIGHTCRAWLER_SYMBOL.get());
                        output.accept(FTItems.DAYWALKER_SYMBOL.get());
                        output.accept(FTItems.BERSERK_SYMBOL.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}