package net.kayn.fallen_traits.events;

import net.kayn.fallen_traits.FallenTraits;
import net.kayn.fallen_traits.init.FTMiscs;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FallenTraits.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FTAttributeEvents {

    @SubscribeEvent
    public static void onAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, FTMiscs.SIZE_SCALE.get());
    }

}