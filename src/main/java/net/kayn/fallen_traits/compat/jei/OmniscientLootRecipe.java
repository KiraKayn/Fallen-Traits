package net.kayn.fallen_traits.compat.jei;

import dev.xkmc.l2hostility.compat.jei.ITraitLootRecipe;
import dev.xkmc.l2hostility.init.loot.TraitLootModifier;
import dev.xkmc.l2hostility.init.network.LootDataToClient;
import net.kayn.fallen_traits.init.FTItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OmniscientLootRecipe implements ITraitLootRecipe {

    private final TraitLootModifier delegate;
    private final List<ItemStack> originalCurios;

    public OmniscientLootRecipe(TraitLootModifier delegate) {
        this.delegate = delegate;
        this.originalCurios = delegate.getCurioRequired();
    }

    @Override
    public List<ItemStack> getResults() {
        return delegate.getResults();
    }

    @Override
    public List<ItemStack> getCurioRequired() {
        List<ItemStack> required = new ArrayList<>();
        required.add(FTItems.OMNISCIENT_LOOTING_CHARM.get().getDefaultInstance());
        return required;
    }

    @Override
    public List<ItemStack> getInputs() {
        return delegate.getInputs();
    }

    @Override
    public void addTooltip(List<Component> list) {
        list.add(Component.translatable("tooltip.fallen_traits.omniscient_looting_charm.jei")
                .withStyle(ChatFormatting.GOLD));
        delegate.addTooltip(list);
    }

    public List<ItemStack> getOriginalCurios() {
        return originalCurios;
    }
}