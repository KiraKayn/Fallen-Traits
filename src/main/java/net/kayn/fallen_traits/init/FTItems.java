package net.kayn.fallen_traits.init;

import dev.xkmc.l2hostility.content.item.traits.TraitSymbol;
import net.kayn.fallen_traits.FallenTraits;
import net.kayn.fallen_traits.content.block.FTBlocks;
import net.kayn.fallen_traits.content.item.FoiledItem;
import net.kayn.fallen_traits.content.item.curio.FuryOfInfernal;
import net.kayn.fallen_traits.content.item.curio.InvulnerabilityBreaker;
import net.kayn.fallen_traits.content.item.curio.RageGlove;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FTItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FallenTraits.MOD_ID);

    public static final RegistryObject<TraitSymbol> RAGE_SYMBOL = ITEMS.register("rage",
            () -> new TraitSymbol(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<RageGlove> RAGE_GLOVE = ITEMS.register("rage_glove",
            () -> new RageGlove(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<TraitSymbol> MIMIC_SYMBOL = ITEMS.register("mimic",
            () -> new TraitSymbol(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<TraitSymbol> CLONE_SYMBOL = ITEMS.register("clone",
            () -> new TraitSymbol(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<TraitSymbol> CLEANSE_SYMBOL = ITEMS.register("cleanse",
            () -> new TraitSymbol(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<TraitSymbol> DAYWALKER_SYMBOL = ITEMS.register("daywalker",
            () -> new TraitSymbol(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<TraitSymbol> NIGHTCRAWLER_SYMBOL = ITEMS.register("nightcrawler",
            () -> new TraitSymbol(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<TraitSymbol> BERSERK_SYMBOL = ITEMS.register("berserk",
            () -> new TraitSymbol(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<TraitSymbol> INFERNAL_SYMBOL = ITEMS.register("infernal",
            () -> new TraitSymbol(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<InvulnerabilityBreaker> INVULNERABILITY_BREAKER = ITEMS.register("invulnerability_breaker",
            () -> new InvulnerabilityBreaker(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<FuryOfInfernal> FURY_OF_INFERNAL = ITEMS.register("fury_of_infernal",
            () -> new FuryOfInfernal(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<FoiledItem> TRANSCENDENT_INGOT = ITEMS.register("transcendent_ingot",
            () -> new FoiledItem(new Item.Properties().rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<FoiledItem> TRANSCENDENT_DUST = ITEMS.register("transcendent_dust",
            () -> new FoiledItem(new Item.Properties().rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<BlockItem> TRANSCENDENT_BLOCK = ITEMS.register("transcendent_block",
            () -> new BlockItem(FTBlocks.TRANSCENDENT_BLOCK.get(), new Item.Properties().rarity(Rarity.EPIC).fireResistant()));

    public static void register() {
    }

}