package net.kayn.fallen_traits.events;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import net.kayn.fallen_traits.FallenTraits;
import net.kayn.fallen_traits.content.item.curio.RageGlove;
import net.kayn.fallen_traits.init.FTItems;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FallenTraits.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FTEvents {

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getAmount() <= 0) return;
        var entity = event.getEntity();
        if (CurioCompat.hasItemInCurio(entity, FTItems.RAGE_GLOVE.get())) {
            RageGlove.STACKS.remove(entity.getUUID());
        }
    }

}