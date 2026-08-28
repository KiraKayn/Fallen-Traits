package net.kayn.fallen_traits.mixin.l2hostility;

import dev.xkmc.l2hostility.content.traits.base.AttributeTrait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = AttributeTrait.class, remap = false)
public interface AttributeTraitAccessor {

    @Accessor("entries")
    AttributeTrait.AttributeEntry[] fallen_traits$getEntries();

}