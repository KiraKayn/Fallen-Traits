package net.kayn.fallen_traits.init;

import net.kayn.fallen_traits.FallenTraits;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FTMiscs {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, FallenTraits.MOD_ID);

    public static final RegistryObject<Attribute> SIZE_SCALE = ATTRIBUTES.register("size",
            () -> new RangedAttribute("attribute.fallen_traits.size", 1.0, 0.01, 100).setSyncable(true));

    public static void register() {
    }

}