package net.kayn.fallen_traits.content.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FoiledItem extends Item {

    public FoiledItem(Properties props) {
        super(props);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

}