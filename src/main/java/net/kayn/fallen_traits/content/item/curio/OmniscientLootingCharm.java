package net.kayn.fallen_traits.content.item.curio;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.kayn.fallen_traits.content.item.LootingCharmWrapper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class OmniscientLootingCharm extends LootingCharmWrapper {
    public OmniscientLootingCharm(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();

        boolean hasDuplicate = !CurioCompat.getItems(
                wearer, equipped -> equipped.getItem() == this
        ).isEmpty();
        if (hasDuplicate) return false;

        return CurioCompat.getItems(
                wearer,
                equipped -> {
                    Item item = equipped.getItem();
                    return item == LHItems.LOOT_1.get() ||
                            item == LHItems.LOOT_2.get() ||
                            item == LHItems.LOOT_3.get() ||
                            item == LHItems.LOOT_4.get();
                }
        ).isEmpty();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable("tooltip.fallen_traits.omniscient_looting_charm")
                .withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable("tooltip.fallen_traits.omniscient_looting_charm.desc")
                .withStyle(ChatFormatting.RED));
    }
}