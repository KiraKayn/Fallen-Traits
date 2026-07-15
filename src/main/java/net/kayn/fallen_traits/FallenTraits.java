package net.kayn.fallen_traits;

import net.kayn.fallen_traits.init.FTConfig;
import net.kayn.fallen_traits.init.FTCreativeTab;
import net.kayn.fallen_traits.init.FTItems;
import net.kayn.fallen_traits.init.FTTraits;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod(FallenTraits.MOD_ID)
public class FallenTraits {
    public static final String MOD_ID = "fallen_traits";
    private static final Logger LOGGER = LogManager.getLogger();

    public FallenTraits(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        LOGGER.info("Loading Fallen Traits");

        FTTraits.TRAITS.register(modEventBus);
        FTItems.ITEMS.register(modEventBus);
        FTCreativeTab.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FTConfig.COMMON_SPEC);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    public static ResourceLocation id(@NotNull String path) {
        return new ResourceLocation(FallenTraits.MOD_ID, path);
    }
}