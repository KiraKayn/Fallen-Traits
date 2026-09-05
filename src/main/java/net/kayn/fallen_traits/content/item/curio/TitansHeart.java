package net.kayn.fallen_traits.content.item.curio;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.init.data.ArmorEffectConfig;
import dev.xkmc.l2damagetracker.init.data.L2DTLangData;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2library.util.math.MathHelper;
import net.kayn.fallen_traits.init.FTConfig;
import net.kayn.fallen_traits.init.FTMiscs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.rtxyd.fallen.lib.runtime.forgemod.util.EntityCakyHandler;
import net.rtxyd.fallen.lib.util.IObjectCaky;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

public class TitansHeart extends CurseCurioItem {

    public static final String BONUS_CAKY_KEY = "ft.titans_heart_bonus";

    private static final UUID HEALTH_ID = MathHelper.getUUIDFromString("fallen_traits_titans_heart_health");
    private static final UUID ARMOR_ID = MathHelper.getUUIDFromString("fallen_traits_titans_heart_armor");
    private static final UUID SIZE_ID = MathHelper.getUUIDFromString("fallen_traits_titans_heart_size");

    public TitansHeart(Properties props) {
        super(props);
    }

    @Override
    public int getExtraLevel() {
        return FTConfig.COMMON.titansHeartExtraDifficulty.get();
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
                equipped -> equipped.getItem() instanceof Feyweight
        ).isEmpty();
    }
    @Override
    public void onHurtTarget(ItemStack stack, LivingEntity user, AttackCache cache) {
        var event = cache.getLivingHurtEvent();
        if (event == null || event.getAmount() <= 0) return;
        LivingEntity target = cache.getAttackTarget();
        int diff = Math.max(0, DifficultyLevel.ofAny(target) - DifficultyLevel.ofAny(user));
        BonusData data = getData(user);
        data.diff = diff;
        data.lastHit = user.level().getGameTime();
    }

    private BonusData getData(LivingEntity user) {
        return EntityCakyHandler.resolveWith(user, BONUS_CAKY_KEY, IObjectCaky.Type.MANUAL, e -> new BonusData(), e -> 0);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if (wearer.level().isClientSide()) return;

        setOrRemoveModifier(wearer, Attributes.MAX_HEALTH, HEALTH_ID, "fallen_traits_titans_heart_health",
                FTConfig.COMMON.titansHeartHealth.get(), AttributeModifier.Operation.MULTIPLY_TOTAL);

        setOrRemoveModifier(wearer, Attributes.ARMOR, ARMOR_ID, "fallen_traits_titans_heart_armor",
                FTConfig.COMMON.titansHeartArmor.get(), AttributeModifier.Operation.MULTIPLY_TOTAL);

        setOrRemoveModifier(wearer, FTMiscs.SIZE_SCALE.get(), SIZE_ID, "fallen_traits_titans_heart_size",
                FTConfig.COMMON.titansHeartSize.get(), AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if (wearer == null) return;
        setOrRemoveModifier(wearer, Attributes.MAX_HEALTH, HEALTH_ID, "fallen_traits_titans_heart_health", 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        setOrRemoveModifier(wearer, Attributes.ARMOR, ARMOR_ID, "fallen_traits_titans_heart_armor", 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        setOrRemoveModifier(wearer, FTMiscs.SIZE_SCALE.get(), SIZE_ID, "fallen_traits_titans_heart_size", 0, AttributeModifier.Operation.ADDITION);
    }

    private void setOrRemoveModifier(LivingEntity wearer, Attribute attr, UUID id, String name, double value, AttributeModifier.Operation op) {
        AttributeInstance ins = wearer.getAttribute(attr);
        if (ins == null) return;
        if (ins.getModifier(id) != null) {
            ins.removeModifier(id);
        }
        if (value != 0) {
            ins.addTransientModifier(new AttributeModifier(id, name, value, op));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable(getDescriptionId() + ".desc_static",
                        Component.literal((int) Math.round(FTConfig.COMMON.titansHeartSize.get() * 100) + "%").withStyle(ChatFormatting.GOLD),
                        Component.literal((int) Math.round(FTConfig.COMMON.titansHeartHealth.get() * 100) + "%").withStyle(ChatFormatting.GOLD),
                        Component.literal((int) Math.round(FTConfig.COMMON.titansHeartArmor.get() * 100) + "%").withStyle(ChatFormatting.GOLD))
                .withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable("item.fallen_traits.legendary_trait_cheap",
                        Component.literal((int) Math.round(FTConfig.COMMON.titansHeartLegendaryChanceBonus.get() * 100) + "%").withStyle(ChatFormatting.RED))
                .withStyle(ChatFormatting.RED));
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(this);
        assert id != null;
        addTooltip(list, ArmorEffectConfig.get().getImmunity(id.toString()));
    }

    private void addTooltip(List<Component> list, Set<MobEffect> set) {
        if (set == null) return;
        TreeMap<ResourceLocation, MobEffect> map = new TreeMap<>();
        for (MobEffect e : set) {
            if (e == null) continue;
            ResourceLocation key = ForgeRegistries.MOB_EFFECTS.getKey(e);
            if (key == null) continue;
            map.put(key, e);
        }
        if (map.isEmpty()) return;
        MutableComponent comp = L2DTLangData.ARMOR_IMMUNE.get();
        boolean comma = false;
        for (MobEffect e : map.values()) {
            if (comma) comp = comp.append(", ");
            comp = comp.append(Component.translatable(e.getDescriptionId()).withStyle(ChatFormatting.RED));
            comma = true;
        }
        list.add(comp.withStyle(ChatFormatting.LIGHT_PURPLE));
    }

    public static class BonusData {
        public int diff = 0;
        public long lastHit = -1000000;
    }

}