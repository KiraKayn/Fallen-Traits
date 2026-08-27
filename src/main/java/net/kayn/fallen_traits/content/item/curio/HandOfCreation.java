package net.kayn.fallen_traits.content.item.curio;

import com.google.common.collect.Multimap;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.kayn.fallen_traits.init.FTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.rtxyd.fallen.lib.runtime.forgemod.util.EntityCakyHandler;
import net.rtxyd.fallen.lib.util.IObjectCaky;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class HandOfCreation extends CurseCurioItem {

    public static final String STACK_KEY = "ft.hand_of_creation_stack";
    public static final String EFFECT_OWNER_KEY = "ft.effect_owner";

    public HandOfCreation(Properties props) {
        super(props);
    }

    @Override
    protected Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nullable LivingEntity wearer, UUID uuid) {
        Multimap<Attribute, AttributeModifier> map = super.getAttributeModifiers(wearer, uuid);
        CuriosApi.addSlotModifier(map, "ring", uuid, 5, AttributeModifier.Operation.ADDITION);
        CuriosApi.addSlotModifier(map, "charm", uuid, 2, AttributeModifier.Operation.ADDITION);
        return map;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        return CurioCompat.getItems(
                wearer,
                equipped -> {
                    Item item = equipped.getItem();
                    return item instanceof RageGlove ||
                            item instanceof InvulnerabilityBreaker ||
                            item == dev.xkmc.l2hostility.init.registrate.LHItems.FLAMING_THORN.get() ||
                            item == dev.xkmc.l2hostility.init.registrate.LHItems.IMAGINE_BREAKER.get() ||
                            item == dev.xkmc.l2hostility.init.registrate.LHItems.PLATINUM_STAR.get() ||
                            item == dev.xkmc.l2hostility.init.registrate.LHItems.INFINITY_GLOVE.get();
                }
        ).isEmpty();
    }

    @Override
    public void onHurtTarget(ItemStack stack, LivingEntity user, AttackCache cache) {
        var event = cache.getLivingHurtEvent();
        if (event == null || event.getAmount() <= 0) return;

        Stack data = getStack(user);
        long time = user.level().getGameTime();
        int timeout = FTConfig.COMMON.handOfCreationStackTimeoutTicks.get();
        if (time - data.lastHit > timeout) {
            data.count = 0;
        }
        double inc = FTConfig.COMMON.handOfCreationDamageIncreasePerHit.get();
        double max = FTConfig.COMMON.handOfCreationMaxDamageMultiplier.get();
        double factor = Math.min(max, 1 + data.count * inc);
        cache.addHurtModifier(DamageModifier.multTotal((float) factor));
        data.count++;
        data.lastHit = time;

        LivingEntity target = cache.getAttackTarget();
        String targetId = target.getStringUUID();
        if (!targetId.equals(data.flameTargetId)) {
            data.flameTargetId = targetId;
            data.flameLevel = 0;
        }
        int traitCount = MobTraitCap.HOLDER.isProper(target) ? MobTraitCap.HOLDER.get(target).traits.size() : 0;
        if (traitCount > 0) {
            data.flameLevel = Math.min(traitCount, data.flameLevel + 1);
            int flameTime = FTConfig.COMMON.handOfCreationFlameDurationTicks.get();
            EffectUtil.addEffect(target, new MobEffectInstance(LCEffects.FLAME.get(), flameTime, data.flameLevel - 1), EffectUtil.AddReason.FORCE, user);
            markEffectOwner(target, user);
        }
    }

    private static void markEffectOwner(LivingEntity target, LivingEntity owner) {
        EffectOwnerData data = EntityCakyHandler.resolveWith(target, EFFECT_OWNER_KEY, IObjectCaky.Type.MANUAL, e -> new EffectOwnerData(), e -> 0);
        data.ownerUUID = owner.getStringUUID();
        data.lastMarked = target.level().getGameTime();
    }

    @Nullable
    public static LivingEntity getEffectOwner(LivingEntity target, MobEffect effect) {
        EffectOwnerData data = EntityCakyHandler.resolveWith(target, EFFECT_OWNER_KEY, IObjectCaky.Type.MANUAL, e -> new EffectOwnerData(), e -> 0);
        if (data.ownerUUID == null || data.ownerUUID.isEmpty()) return null;

        long timeout = FTConfig.COMMON.handOfCreationFlameDurationTicks.get() + 20;
        if (target.level().getGameTime() - data.lastMarked > timeout) {
            data.ownerUUID = null;
            return null;
        }

        if (!(target.level() instanceof net.minecraft.server.level.ServerLevel sl)) return null;
        Entity entity = sl.getEntity(UUID.fromString(data.ownerUUID));
        return entity instanceof LivingEntity le ? le : null;
    }

    public void onWearerHurt(LivingEntity wearer) {
        int bonus = FTConfig.COMMON.handOfCreationWearerInvulnBonusTicks.get();
        wearer.invulnerableTime += bonus;
    }

    private Stack getStack(LivingEntity user) {
        return EntityCakyHandler.resolveWith(user, STACK_KEY, IObjectCaky.Type.MANUAL, e -> new Stack(), e -> 0);
    }

    @Override
    public double getLootFactor(ItemStack stack, PlayerDifficulty player, MobTraitCap mob) {
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        int inc = (int) Math.round(FTConfig.COMMON.handOfCreationDamageIncreasePerHit.get() * 100);
        int maxMult = (int) Math.round(FTConfig.COMMON.handOfCreationMaxDamageMultiplier.get() * 100);
        list.add(Component.translatable(getDescriptionId() + ".desc_intro").withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(getDescriptionId() + ".desc_rage",
                Component.literal(inc + "%").withStyle(ChatFormatting.RED),
                Component.literal(maxMult + "%").withStyle(ChatFormatting.RED)).withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(getDescriptionId() + ".desc_invuln_break").withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(getDescriptionId() + ".desc_invuln_bonus",
                Component.literal(FTConfig.COMMON.handOfCreationWearerInvulnBonusTicks.get() + "").withStyle(ChatFormatting.RED)).withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(getDescriptionId() + ".desc_flame").withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(getDescriptionId() + ".desc_magic").withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(getDescriptionId() + ".desc_no_drop").withStyle(ChatFormatting.RED));
        list.add(Component.translatable(getDescriptionId() + ".desc_max_traits").withStyle(ChatFormatting.RED));
        list.add(Component.translatable(getDescriptionId() + ".desc_over_max").withStyle(ChatFormatting.RED));
        list.add(Component.translatable(getDescriptionId() + ".desc_no_trait_cap").withStyle(ChatFormatting.RED));
    }

    public static class Stack {
        public int count = 0;
        public long lastHit = 0;
        public String flameTargetId = "";
        public int flameLevel = 0;
    }

    public static class EffectOwnerData {
        public String ownerUUID = "";
        public long lastMarked = 0;
    }

}