package net.kayn.fallen_traits.content.item.curio;

import com.google.common.collect.Multimap;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import net.kayn.fallen_traits.init.FTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class FuryOfInfernal extends CurseCurioItem {

    private static final UUID ATTACK_SPEED_ID = dev.xkmc.l2library.util.math.MathHelper.getUUIDFromString("fallen_traits_fury_attack_speed");
    private static final UUID MOVE_SPEED_ID = dev.xkmc.l2library.util.math.MathHelper.getUUIDFromString("fallen_traits_fury_move_speed");
    private static final UUID CRIT_DAMAGE_ID = dev.xkmc.l2library.util.math.MathHelper.getUUIDFromString("fallen_traits_fury_crit_damage");

    private static final Map<UUID, TargetData> TARGETS = new WeakHashMap<>();

    public FuryOfInfernal(Properties props) {
        super(props);
    }

    @Override
    public int getExtraLevel() {
        return FTConfig.COMMON.furyInfernalExtraLevel.get();
    }

    @Override
    protected Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nullable LivingEntity wearer, UUID uuid) {
        Multimap<Attribute, AttributeModifier> map = super.getAttributeModifiers(wearer, uuid);
        CuriosApi.addSlotModifier(map, "hostility_curse", uuid, 1, AttributeModifier.Operation.ADDITION);
        return map;
    }

    @Override
    public void onHurtTarget(ItemStack stack, LivingEntity user, AttackCache cache) {
        var event = cache.getLivingHurtEvent();
        if (event == null || event.getAmount() <= 0) return;
        LivingEntity target = cache.getAttackTarget();
        TARGETS.put(user.getUUID(), new TargetData(target.getUUID(), user.level().getGameTime()));

        if (!MobTraitCap.HOLDER.isProper(target)) return;
        MobTraitCap cap = MobTraitCap.HOLDER.get(target);
        int nonLegendaryLevels = 0;
        for (var e : cap.traits.entrySet()) {
            if (!(e.getKey() instanceof LegendaryTrait)) {
                nonLegendaryLevels += e.getValue();
            }
        }
        if (nonLegendaryLevels > 0) {
            double rate = nonLegendaryLevels * FTConfig.COMMON.furyInfernalDamagePerTraitLevel.get();
            cache.addHurtModifier(DamageModifier.multBase((float) (1 + rate)));
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if (wearer == null || wearer.level().isClientSide()) return;

        LivingEntity target = resolveTarget(wearer);
        int totalLevels = 0;
        int legendaryLevels = 0;
        if (target != null && MobTraitCap.HOLDER.isProper(target)) {
            MobTraitCap cap = MobTraitCap.HOLDER.get(target);
            for (var e : cap.traits.entrySet()) {
                totalLevels += e.getValue();
                if (e.getKey() instanceof LegendaryTrait) {
                    legendaryLevels += e.getValue();
                }
            }
        }
        double distance = target != null ? wearer.distanceTo(target) : 0;
        double cappedDistance = Math.min(distance, FTConfig.COMMON.furyInfernalMaxBonusBlocks.get());

        double atkSpeedVal = totalLevels >= FTConfig.COMMON.furyInfernalAttackSpeedTraitThreshold.get()
                ? FTConfig.COMMON.furyInfernalAttackSpeedBase.get() + cappedDistance * FTConfig.COMMON.furyInfernalAttackSpeedPerBlock.get()
                : 0;
        setOrRemoveModifier(wearer, Attributes.ATTACK_SPEED, ATTACK_SPEED_ID, "fallen_traits_fury_attack_speed",
                atkSpeedVal, AttributeModifier.Operation.MULTIPLY_TOTAL);

        double moveSpeedVal = totalLevels >= FTConfig.COMMON.furyInfernalMoveSpeedTraitThreshold.get()
                ? FTConfig.COMMON.furyInfernalMoveSpeedBase.get() + cappedDistance * FTConfig.COMMON.furyInfernalMoveSpeedPerBlock.get()
                : 0;
        setOrRemoveModifier(wearer, Attributes.MOVEMENT_SPEED, MOVE_SPEED_ID, "fallen_traits_fury_move_speed",
                moveSpeedVal, AttributeModifier.Operation.MULTIPLY_TOTAL);

        double critDmgVal = legendaryLevels * FTConfig.COMMON.furyInfernalCritDamagePerLegendaryLevel.get();
        setOrRemoveModifier(wearer, ALObjects.Attributes.CRIT_DAMAGE.get(), CRIT_DAMAGE_ID, "fallen_traits_fury_crit_damage",
                critDmgVal, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if (wearer == null) return;
        setOrRemoveModifier(wearer, Attributes.ATTACK_SPEED, ATTACK_SPEED_ID, "fallen_traits_fury_attack_speed", 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        setOrRemoveModifier(wearer, Attributes.MOVEMENT_SPEED, MOVE_SPEED_ID, "fallen_traits_fury_move_speed", 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        setOrRemoveModifier(wearer, ALObjects.Attributes.CRIT_DAMAGE.get(), CRIT_DAMAGE_ID, "fallen_traits_fury_crit_damage", 0, AttributeModifier.Operation.ADDITION);
    }

    private LivingEntity resolveTarget(LivingEntity wearer) {
        TargetData td = TARGETS.get(wearer.getUUID());
        if (td == null) return null;
        int timeout = FTConfig.COMMON.furyInfernalTargetTimeoutTicks.get();
        if (wearer.level().getGameTime() - td.lastHitTick > timeout) {
            TARGETS.remove(wearer.getUUID());
            return null;
        }
        if (!(wearer.level() instanceof ServerLevel sl)) return null;
        Entity e = sl.getEntity(td.targetId);
        return e instanceof LivingEntity le && le.isAlive() ? le : null;
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
        list.add(Component.translatable(getDescriptionId() + ".desc_damage",
                        Component.literal((int) Math.round(FTConfig.COMMON.furyInfernalDamagePerTraitLevel.get() * 100) + "%").withStyle(ChatFormatting.RED))
                .withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(getDescriptionId() + ".desc_crit",
                        Component.literal((int) Math.round(FTConfig.COMMON.furyInfernalCritDamagePerLegendaryLevel.get() * 100) + "%").withStyle(ChatFormatting.RED))
                .withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(getDescriptionId() + ".desc_attack_speed",
                        Component.literal(FTConfig.COMMON.furyInfernalAttackSpeedTraitThreshold.get() + "").withStyle(ChatFormatting.RED),
                        Component.literal((int) Math.round(FTConfig.COMMON.furyInfernalAttackSpeedBase.get() * 100) + "%").withStyle(ChatFormatting.RED),
                        Component.literal((int) Math.round(FTConfig.COMMON.furyInfernalAttackSpeedPerBlock.get() * 100) + "%").withStyle(ChatFormatting.RED))
                .withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(getDescriptionId() + ".desc_move_speed",
                        Component.literal(FTConfig.COMMON.furyInfernalMoveSpeedTraitThreshold.get() + "").withStyle(ChatFormatting.RED),
                        Component.literal((int) Math.round(FTConfig.COMMON.furyInfernalMoveSpeedBase.get() * 100) + "%").withStyle(ChatFormatting.RED),
                        Component.literal((int) Math.round(FTConfig.COMMON.furyInfernalMoveSpeedPerBlock.get() * 100) + "%").withStyle(ChatFormatting.RED))
                .withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(getDescriptionId() + ".desc_compat").withStyle(ChatFormatting.RED));
        list.add(Component.translatable(getDescriptionId() + ".desc_frequency").withStyle(ChatFormatting.RED));
    }

    private static class TargetData {
        final UUID targetId;
        final long lastHitTick;

        TargetData(UUID targetId, long lastHitTick) {
            this.targetId = targetId;
            this.lastHitTick = lastHitTick;
        }
    }

}