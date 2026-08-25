package net.kayn.fallen_traits.content.item.curio;

import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PhasewalkersRing extends CurseCurioItem {

    public PhasewalkersRing(Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GOLD));
    }
}