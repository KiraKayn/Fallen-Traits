package net.kayn.fallen_traits.init;

import net.kayn.fallen_traits.FallenTraits;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class FTCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FallenTraits.MOD_ID);

    public static final RegistryObject<CreativeModeTab> FALLEN_TRAITS_TAB = CREATIVE_MODE_TABS.register("fallen_traits_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.fallen_traits_tab"))
                    .icon(() -> new ItemStack(FTItems.INFERNAL_SYMBOL.get()))
                    .displayItems((parameters, output) -> {
                        //items
                        output.accept(FTItems.TITANS_HEART.get());
                        output.accept(FTItems.FURY_OF_INFERNAL.get());
                        output.accept(FTItems.TRANSCENDENT_INGOT.get());
                        output.accept(FTItems.TRANSCENDENT_DUST.get());
                        output.accept(FTItems.TRANSCENDENT_BLOCK.get());
                        output.accept(FTItems.RAGE_GLOVE.get());
                        output.accept(FTItems.INVULNERABILITY_BREAKER.get());
                        output.accept(FTItems.LAW_OF_SCALE.get());
                        output.accept(FTItems.FEYWEIGHT.get());
                        output.accept(FTItems.OMNISCIENT_LOOTING_CHARM.get());
                        output.accept(FTItems.PHASEWALKERS_RING.get());
                        output.accept(FTItems.HAND_OF_CREATION.get());
                        output.accept(FTItems.DIVINE_SINGULARITY.get());
                        output.accept(FTItems.WRATH_OF_FENRIR.get());
                        output.accept(FTItems.UNIVERSAL_POINT_OF_DISCONTINUITY.get());
                        //traits
                        output.accept(FTItems.RAGE_SYMBOL.get());
                        output.accept(FTItems.MIMIC_SYMBOL.get());
                        output.accept(FTItems.CLONE_SYMBOL.get());
                        output.accept(FTItems.CLEANSE_SYMBOL.get());
                        output.accept(FTItems.NIGHTCRAWLER_SYMBOL.get());
                        output.accept(FTItems.DAYWALKER_SYMBOL.get());
                        output.accept(FTItems.BERSERK_SYMBOL.get());
                        output.accept(FTItems.INFERNAL_SYMBOL.get());
                        output.accept(FTItems.DEVOURER_SYMBOL.get());
                        output.accept(FTItems.SHREDDER_SYMBOL.get());
                        output.accept(FTItems.TITAN_SYMBOL.get());
                        output.accept(FTItems.DWARF_SYMBOL.get());
                        // Enchanted books
                        for (int level = 1; level <= FTEnchantments.INCARCERATING_BLADE.get().getMaxLevel(); level++) {
                            output.accept(EnchantedBookItem.createForEnchantment(
                                    new EnchantmentInstance(FTEnchantments.INCARCERATING_BLADE.get(), level)
                            ));
                        }
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}