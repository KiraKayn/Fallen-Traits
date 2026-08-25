package net.kayn.fallen_traits.compat.jei;

import dev.xkmc.l2hostility.compat.jei.GLMRecipeCategory;
import dev.xkmc.l2hostility.compat.jei.ITraitLootRecipe;
import dev.xkmc.l2hostility.init.loot.TraitLootModifier;
import dev.xkmc.l2hostility.init.network.LootDataToClient;
import net.kayn.fallen_traits.FallenTraits;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class FallenTraitsJEIPlugin implements IModPlugin {

    public static final ResourceLocation ID = new ResourceLocation(FallenTraits.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<ITraitLootRecipe> omniscientRecipes = new ArrayList<>();

        for (ITraitLootRecipe recipe : LootDataToClient.LIST_CACHE) {
            if (recipe instanceof TraitLootModifier traitLoot) {
                omniscientRecipes.add(new OmniscientLootRecipe(traitLoot));
            }
        }

        if (!omniscientRecipes.isEmpty()) {
            GLMRecipeCategory category = new GLMRecipeCategory();
            registration.addRecipes(category.getRecipeType(), omniscientRecipes);
        }
    }
}