package net.kayn.fallen_traits.init;

import net.kayn.fallen_traits.FallenTraits;
import net.kayn.fallen_traits.content.enchantment.weapon.IncarceratingBladeEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FTEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, FallenTraits.MOD_ID);

    public static final RegistryObject<IncarceratingBladeEnchantment> INCARCERATING_BLADE =
            ENCHANTMENTS.register("incarcerating_blade",
                    () -> new IncarceratingBladeEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND}
                    ));

    public static void register() {
    }
}