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

    // Define the custom creative tab
    public static final RegistryObject<CreativeModeTab> FALLEN_TRAITS_TAB = CREATIVE_MODE_TABS.register("fallen_traits_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.fallen_traits_tab")) // Localization key
                    .icon(() -> new ItemStack(FTItems.RAGE_GLOVE.get())) // Set the icon of the tab
                    .displayItems((parameters, output) -> {
                        // Add your items here in the order you want them to appear
                        output.accept(FTItems.RAGE_SYMBOL.get());
                        output.accept(FTItems.RAGE_GLOVE.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}