package net.kayn.fallen_traits.content.item.curio;

import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import net.kayn.fallen_traits.content.traits.legendary.SizeTrait;
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
import net.minecraft.world.phys.Vec3;
import net.rtxyd.fallen.lib.runtime.forgemod.util.EntityCakyHandler;
import net.rtxyd.fallen.lib.util.IObjectCaky;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class LawOfScale extends CurseCurioItem {

    public static final String TARGET_KEY = "ft.law_of_scale_target";

    private static final UUID ATTACK_SPEED_ID = dev.xkmc.l2library.util.math.MathHelper.getUUIDFromString("fallen_traits_law_of_scale_attack_speed");
    private static final UUID CURRENT_HP_DAMAGE_ID = dev.xkmc.l2library.util.math.MathHelper.getUUIDFromString("fallen_traits_law_of_scale_current_hp_damage");

    public LawOfScale(Properties props) {
        super(props);
    }

    @Override
    public int getExtraLevel() {
        return FTConfig.COMMON.lawOfScaleExtraDifficulty.get();
    }

    @Override
    public void onHurtTarget(ItemStack stack, LivingEntity user,
                             AttackCache cache) {
        var event = cache.getLivingHurtEvent();
        if (event == null || event.getAmount() <= 0) return;

        LivingEntity target = cache.getAttackTarget();
        if (target == null) return;

        TargetData data = getData(user);
        data.targetId = target.getUUID();
        data.lastHitTick = user.level().getGameTime();

        float userSize = getEffectiveSize(user);
        float targetSize = getEffectiveSize(target);
        if (userSize <= 0 || targetSize <= 0) return;

        double ratio = userSize / targetSize;

        if (ratio > 1.0D) {
            double largerUnits = (ratio - 1.0D) / 0.5D;

            double damageBonus = largerUnits *
                    FTConfig.COMMON
                            .lawOfScaleAttackDamagePer50PercentLarger.get();

            if (damageBonus > 0) {
                cache.addHurtModifier(
                        DamageModifier.multBase((float) damageBonus)
                );
            }

            applyKnockback(user, target, ratio - 1.0D);
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if (wearer == null || wearer.level().isClientSide()) return;

        LivingEntity target = resolveTarget(wearer);
        double atkSpeedVal = 0;
        double currentHpDmgVal = 0;

        if (target != null) {
            float userScale = getEffectiveSize(wearer);
            float targetScale = getEffectiveSize(target);
            if (userScale > 0 && targetScale > 0) {
                double ratio = userScale / targetScale;
                if (ratio < 1) {
                    double targetLargerRatio = (targetScale / userScale) - 1;

                    double smallerUnits = (1 - ratio) / 0.5;
                    atkSpeedVal = smallerUnits * FTConfig.COMMON.lawOfScaleAttackSpeedPer50PercentSmaller.get();

                    currentHpDmgVal = Math.min(
                            FTConfig.COMMON.lawOfScaleMaxDamagePercent.get(),
                            targetLargerRatio * FTConfig.COMMON.lawOfScaleDamagePercentPerSizeRatio.get()
                    );
                }
            }
        }

        setOrRemoveModifier(wearer, Attributes.ATTACK_SPEED, ATTACK_SPEED_ID, "fallen_traits_law_of_scale_attack_speed",
                atkSpeedVal, AttributeModifier.Operation.MULTIPLY_TOTAL);
        setOrRemoveModifier(wearer, ALObjects.Attributes.CURRENT_HP_DAMAGE.get(), CURRENT_HP_DAMAGE_ID, "fallen_traits_law_of_scale_current_hp_damage",
                currentHpDmgVal, AttributeModifier.Operation.ADDITION);
    }

    private void applyKnockback(LivingEntity wearer, LivingEntity target, double sizeAdvantage) {
        double strength = Math.min(FTConfig.COMMON.lawOfScaleKnockbackMax.get(),
                FTConfig.COMMON.lawOfScaleKnockbackBase.get() + sizeAdvantage * FTConfig.COMMON.lawOfScaleKnockbackPerSizeRatio.get());
        Vec3 dir = target.position().subtract(wearer.position());
        if (dir.horizontalDistanceSqr() > 1.0E-4) {
            dir = new Vec3(dir.x, 0, dir.z).normalize().scale(strength);

            target.setDeltaMovement(target.getDeltaMovement().add(dir.x, 0.4, dir.z));
            target.hasImpulse = true;
        }
    }

    private float getEffectiveSize(LivingEntity entity) {
        float scale = SizeTrait.getScale(entity);
        if (scale <= 0) scale = 1.0f;
        return entity.getBbHeight() * scale;
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if (wearer == null) return;
        setOrRemoveModifier(wearer, Attributes.ATTACK_SPEED, ATTACK_SPEED_ID, "fallen_traits_law_of_scale_attack_speed", 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        setOrRemoveModifier(wearer, ALObjects.Attributes.CURRENT_HP_DAMAGE.get(), CURRENT_HP_DAMAGE_ID, "fallen_traits_law_of_scale_current_hp_damage", 0, AttributeModifier.Operation.ADDITION);
    }

    private TargetData getData(LivingEntity entity) {
        return EntityCakyHandler.resolveWith(entity, TARGET_KEY, IObjectCaky.Type.MANUAL, e -> new TargetData(), e -> 0);
    }

    private LivingEntity resolveTarget(LivingEntity wearer) {
        TargetData td = getData(wearer);
        if (td.targetId == null) return null;
        int timeout = FTConfig.COMMON.lawOfScaleTargetTimeoutTicks.get();
        if (wearer.level().getGameTime() - td.lastHitTick > timeout) {
            td.targetId = null;
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
        Component duration = Component.literal(FTConfig.COMMON.lawOfScaleTargetTimeoutTicks.get() / 20 + "s").withStyle(ChatFormatting.RED);

        list.add(Component.translatable(getDescriptionId() + ".desc_current_hp_damage",
                        Component.literal((int) Math.round(FTConfig.COMMON.lawOfScaleMaxDamagePercent.get() * 100) + "%").withStyle(ChatFormatting.RED),
                        duration)
                .withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(getDescriptionId() + ".desc_knockback").withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(getDescriptionId() + ".desc_attack_speed",
                        Component.literal((int) Math.round(FTConfig.COMMON.lawOfScaleAttackSpeedPer50PercentSmaller.get() * 100) + "%").withStyle(ChatFormatting.RED),
                        duration)
                .withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(
                getDescriptionId() + ".desc_damage",
                Component.literal((int) Math.round(FTConfig.COMMON.lawOfScaleAttackDamagePer50PercentLarger.get() * 100) + "%").withStyle(ChatFormatting.RED), duration
        ).withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(getDescriptionId() + ".desc_traits",
                        Component.literal(FTConfig.COMMON.lawOfScaleExtraTraitCount.get() + "").withStyle(ChatFormatting.RED))
                .withStyle(ChatFormatting.RED));
    }

    public static class TargetData {
        public UUID targetId;
        public long lastHitTick;
    }

}