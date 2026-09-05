package net.kayn.fallen_traits.client;

import net.kayn.fallen_traits.FallenTraits;
import net.kayn.fallen_traits.init.FTItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = FallenTraits.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FTClientSetup {

    @SubscribeEvent
    public static void onClientSetup(net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            net.minecraft.client.renderer.item.ItemProperties.register(
                    FTItems.UNIVERSAL_POINT_OF_DISCONTINUITY.get(),
                    new net.minecraft.resources.ResourceLocation("fallen_traits", "mode"),
                    (stack, level, entity, seed) -> stack.getOrCreateTag().getInt("mode")
            );
        });
    }
}