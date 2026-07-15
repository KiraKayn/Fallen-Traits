package net.kayn.fallen_traits.init;

import dev.xkmc.l2hostility.content.item.traits.TraitSymbol;
import net.kayn.fallen_traits.FallenTraits;
import net.kayn.fallen_traits.content.item.curio.RageGlove;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FTItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FallenTraits.MOD_ID);

    public static final RegistryObject<TraitSymbol> RAGE_SYMBOL = ITEMS.register("rage",
            () -> new TraitSymbol(new Item.Properties().stacksTo(64).rarity(Rarity.RARE)));

    public static final RegistryObject<RageGlove> RAGE_GLOVE = ITEMS.register("rage_glove",
            () -> new RageGlove(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}