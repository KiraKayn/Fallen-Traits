package net.kayn.fallen_traits.content.item.curio;

import net.kayn.fallen_traits.content.item.LootingCharmWrapper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OmniscientLootingCharm extends LootingCharmWrapper {
    public OmniscientLootingCharm(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable("tooltip.fallen_traits.omniscient_looting_charm")
                .withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable("tooltip.fallen_traits.omniscient_looting_charm.desc")
                .withStyle(ChatFormatting.RED));
    }
}