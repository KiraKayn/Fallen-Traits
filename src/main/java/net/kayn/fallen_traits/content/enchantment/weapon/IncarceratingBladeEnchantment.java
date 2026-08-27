package net.kayn.fallen_traits.content.enchantment.weapon;

import dev.xkmc.l2complements.content.enchantment.weapon.AbstractBladeEnchantment;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import net.kayn.fallen_traits.init.FTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class IncarceratingBladeEnchantment extends AbstractBladeEnchantment {
    public IncarceratingBladeEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    protected MobEffectInstance getEffect(int pLevel) {
        return new MobEffectInstance(LCEffects.STONE_CAGE.get(),
                FTConfig.COMMON.incarceratingEnchantDuration.get() << pLevel - 1);
    }

    public ChatFormatting getColor() {
        return ChatFormatting.GOLD;
    }

    public int getMinLevel() {
        return 1;
    }

    public int getMaxLevel() {
        return 3;
    }
}