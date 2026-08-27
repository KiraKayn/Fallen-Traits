package net.kayn.fallen_traits.content.item.curio;

import dev.xkmc.l2complements.content.item.curios.EffectValidItem;
import dev.xkmc.l2hostility.content.item.curio.core.MultiSlotItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DivineSingularity extends MultiSlotItem implements EffectValidItem {

    public DivineSingularity(Properties props) {
        super(props);
    }

    @Override
    public boolean isEffectValid(MobEffectInstance ins, ItemStack itemStack, LivingEntity livingEntity) {
        return ins.getEffect().isBeneficial();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GOLD));
    }
}