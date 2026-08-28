package net.kayn.fallen_traits.content.item.curio;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.init.data.ArmorEffectConfig;
import dev.xkmc.l2damagetracker.init.data.L2DTLangData;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.content.traits.base.AttributeTrait;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.base.SelfEffectTrait;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.kayn.fallen_traits.content.item.SpawnRateModifier;
import net.kayn.fallen_traits.init.FTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class WrathOfFenrir extends CurseCurioItem implements SpawnRateModifier {

    public static final String DISABLED_TRAITS_KEY = "ft.wrath_disabled_traits";
    private static final String TRAIT_ID = "trait";
    private static final String TRAIT_LEVEL = "level";
    private static final String EXPIRES_AT = "expires_at";

    public WrathOfFenrir(Properties props) {
        super(props);
    }

    @Override
    public int getExtraLevel() {
        return FTConfig.COMMON.wrathOfFenrirExtraDifficulty.get();
    }

    @Override
    public float getSpawnRateMultiplier() {
        return FTConfig.COMMON.wrathOfFenrirSpawnRateMultiplier.get().floatValue();
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        return CurioCompat.getItems(
                wearer,
                equipped -> equipped.getItem() == LHItems.CURSE_WRATH.get()
        ).isEmpty();
    }

    @Override
    public void onHurtTarget(ItemStack stack, LivingEntity user, AttackCache cache) {
        LivingEntity target = cache.getAttackTarget();
        if (target == null || target.level().isClientSide()) return;

        int difference = DifficultyLevel.ofAny(target) - DifficultyLevel.ofAny(user);
        if (difference > 0) {
            float rate = FTConfig.COMMON.wrathOfFenrirDamagePerLevelDiff.get().floatValue();
            cache.addHurtModifier(DamageModifier.multBase(difference * rate));
        }

        if (MobTraitCap.HOLDER.isProper(target)) {
            disableRandomTraits(target, MobTraitCap.HOLDER.get(target));
        }
    }

    private static void disableRandomTraits(LivingEntity target, MobTraitCap cap) {
        List<Map.Entry<MobTrait, Integer>> available = new ArrayList<>(cap.traits.entrySet());
        if (available.isEmpty()) return;

        int min = Math.min(FTConfig.COMMON.wrathOfFenrirMinTraitsDisabled.get(),
                FTConfig.COMMON.wrathOfFenrirMaxTraitsDisabled.get());
        int max = Math.max(FTConfig.COMMON.wrathOfFenrirMinTraitsDisabled.get(),
                FTConfig.COMMON.wrathOfFenrirMaxTraitsDisabled.get());
        int count = min + target.getRandom().nextInt(max - min + 1);
        count = Math.min(count, available.size());
        if (count <= 0) return;

        long now = target.level().getGameTime();
        long duration = Math.max(1L, FTConfig.COMMON.wrathOfFenrirTraitDisableDuration.get());
        long expiresAt = now + duration;

        CompoundTag persistent = target.getPersistentData();
        ListTag disabled = persistent.getList(DISABLED_TRAITS_KEY, Tag.TAG_COMPOUND);

        Collections.shuffle(available, new Random(target.getRandom().nextLong()));

        for (int i = 0; i < count; i++) {
            MobTrait trait = available.get(i).getKey();
            int level = available.get(i).getValue();
            ResourceLocation id = trait.getRegistryName();
            if (id == null) continue;

            CompoundTag saved = new CompoundTag();
            saved.putString(TRAIT_ID, id.toString());
            saved.putInt(TRAIT_LEVEL, level);
            saved.putLong(EXPIRES_AT, expiresAt);
            disabled.add(saved);

            cap.traits.remove(trait);

            if (trait instanceof SelfEffectTrait selfEffectTrait) {
                MobEffect effect = selfEffectTrait.effect.get();
                if (target.hasEffect(effect)) {
                    target.removeEffect(effect);
                }
            }

            if (trait instanceof AttributeTrait attributeTrait) {
                attributeTrait.initialize(target, 0);
            }
        }

        persistent.put(DISABLED_TRAITS_KEY, disabled);
        cap.syncToClient(target);
    }

    public static void restoreExpiredTraits(LivingEntity entity) {
        if (entity.level().isClientSide()) return;
        if (!MobTraitCap.HOLDER.isProper(entity)) return;

        CompoundTag persistent = entity.getPersistentData();
        if (!persistent.contains(DISABLED_TRAITS_KEY, Tag.TAG_LIST)) return;

        ListTag disabled = persistent.getList(DISABLED_TRAITS_KEY, Tag.TAG_COMPOUND);
        if (disabled.isEmpty()) {
            persistent.remove(DISABLED_TRAITS_KEY);
            return;
        }

        MobTraitCap cap = MobTraitCap.HOLDER.get(entity);
        long now = entity.level().getGameTime();
        boolean changed = false;

        for (int i = disabled.size() - 1; i >= 0; i--) {
            CompoundTag saved = disabled.getCompound(i);
            if (saved.getLong(EXPIRES_AT) > now) continue;

            ResourceLocation id = ResourceLocation.tryParse(saved.getString(TRAIT_ID));
            MobTrait trait = id == null ? null : LHTraits.TRAITS.get().getValue(id);
            int level = saved.getInt(TRAIT_LEVEL);

            if (trait != null && level > 0 && !cap.hasTrait(trait)) {
                cap.traits.put(trait, level);
                trait.initialize(entity, level);
                trait.postInit(entity, level);
                changed = true;
            }

            disabled.remove(i);
        }

        if (disabled.isEmpty()) {
            persistent.remove(DISABLED_TRAITS_KEY);
        } else {
            persistent.put(DISABLED_TRAITS_KEY, disabled);
        }

        if (changed && !entity.isRemoved()) {
            cap.syncToClient(entity);
        }
    }

    private void addTooltip(List<Component> list, Set<MobEffect> set) {
        if (set == null) return;
        TreeMap<ResourceLocation, MobEffect> map = new TreeMap<>();
        for (MobEffect effect : set) {
            if (effect == null) continue;
            ResourceLocation key = ForgeRegistries.MOB_EFFECTS.getKey(effect);
            if (key != null) map.put(key, effect);
        }
        if (map.isEmpty()) return;

        MutableComponent component = L2DTLangData.ARMOR_IMMUNE.get();
        boolean comma = false;
        for (MobEffect effect : map.values()) {
            if (comma) component = component.append(", ");
            component = component.append(Component.translatable(effect.getDescriptionId())
                    .withStyle(ChatFormatting.RED));
            comma = true;
        }
        list.add(component.withStyle(ChatFormatting.LIGHT_PURPLE));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> list, TooltipFlag flag) {
        int damageRate = (int) Math.round(
                FTConfig.COMMON.wrathOfFenrirDamagePerLevelDiff.get() * 100);
        int spawnRate = (int) Math.round(
                FTConfig.COMMON.wrathOfFenrirSpawnRateMultiplier.get() * 100);

        list.add(Component.translatable(getDescriptionId() + ".desc_damage",
                Component.literal(damageRate + "%").withStyle(ChatFormatting.RED)).withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(getDescriptionId() + ".desc_disable",
                        Component.literal(String.valueOf(FTConfig.COMMON.wrathOfFenrirMinTraitsDisabled.get())).withStyle(ChatFormatting.RED),
                        Component.literal(String.valueOf(FTConfig.COMMON.wrathOfFenrirMaxTraitsDisabled.get())).withStyle(ChatFormatting.RED),
                        Component.literal(FTConfig.COMMON.wrathOfFenrirTraitDisableDuration.get() / 20 + "s").withStyle(ChatFormatting.RED))
                .withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(getDescriptionId() + ".desc_spawn_rate",
                Component.literal("+" + spawnRate + "%").withStyle(ChatFormatting.RED)).withStyle(ChatFormatting.RED));

        ResourceLocation id = ForgeRegistries.ITEMS.getKey(this);
        if (id != null) {
            addTooltip(list, ArmorEffectConfig.get().getImmunity(id.toString()));
        }
    }
}