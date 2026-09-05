package net.kayn.fallen_traits.content.item.curio;

import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2library.util.math.MathHelper;
import net.kayn.fallen_traits.content.traits.legendary.SizeTrait;
import net.kayn.fallen_traits.init.FTConfig;
import net.kayn.fallen_traits.init.FTMiscs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class Feyweight extends CurseCurioItem {

    private static final UUID SIZE_ID = MathHelper.getUUIDFromString("fallen_traits_feyweight_size");
    private static final UUID MOVEMENT_SPEED_ID = MathHelper.getUUIDFromString("fallen_traits_feyweight_movement_speed");
    private static final UUID ATTACK_SPEED_ID = MathHelper.getUUIDFromString("fallen_traits_feyweight_attack_speed");
    private static final UUID DODGE_CHANCE_ID = MathHelper.getUUIDFromString("fallen_traits_feyweight_dodge_chance");

    public Feyweight(Properties props) {
        super(props);
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
                equipped -> equipped.getItem() instanceof TitansHeart
        ).isEmpty();
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if (wearer == null || wearer.level().isClientSide()) return;

        boolean sizeChanged = setOrRemoveModifier(wearer, FTMiscs.SIZE_SCALE.get(), SIZE_ID, "fallen_traits_feyweight_size",
                -FTConfig.COMMON.feyweightSizeReduction.get(), AttributeModifier.Operation.ADDITION);
        setOrRemoveModifier(wearer, Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_ID, "fallen_traits_feyweight_movement_speed",
                FTConfig.COMMON.feyweightMovementSpeed.get(), AttributeModifier.Operation.MULTIPLY_TOTAL);
        setOrRemoveModifier(wearer, Attributes.ATTACK_SPEED, ATTACK_SPEED_ID, "fallen_traits_feyweight_attack_speed",
                FTConfig.COMMON.feyweightAttackSpeed.get(), AttributeModifier.Operation.MULTIPLY_TOTAL);
        setOrRemoveModifier(wearer, ALObjects.Attributes.DODGE_CHANCE.get(), DODGE_CHANCE_ID, "fallen_traits_feyweight_dodge_chance",
                FTConfig.COMMON.feyweightDodgeChance.get(), AttributeModifier.Operation.ADDITION);

        if (sizeChanged) {
            wearer.refreshDimensions();
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if (wearer == null) return;

        boolean sizeChanged = setOrRemoveModifier(wearer, FTMiscs.SIZE_SCALE.get(), SIZE_ID, "fallen_traits_feyweight_size", 0, AttributeModifier.Operation.ADDITION);
        setOrRemoveModifier(wearer, Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_ID, "fallen_traits_feyweight_movement_speed", 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        setOrRemoveModifier(wearer, Attributes.ATTACK_SPEED, ATTACK_SPEED_ID, "fallen_traits_feyweight_attack_speed", 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        setOrRemoveModifier(wearer, ALObjects.Attributes.DODGE_CHANCE.get(), DODGE_CHANCE_ID, "fallen_traits_feyweight_dodge_chance", 0, AttributeModifier.Operation.ADDITION);

        if (sizeChanged) {
            wearer.refreshDimensions();
        }
    }

    @Override
    public void onDamage(ItemStack stack, LivingEntity user, LivingDamageEvent event) {
        if (event.getAmount() <= 0) return;

        Entity sourceEntity = event.getSource().getEntity();
        if (!(sourceEntity instanceof LivingEntity attacker)) return;
        if (attacker == user || !attacker.isAlive()) return;
        float wearerSize = getEffectiveSize(user);
        float attackerSize = getEffectiveSize(attacker);
        if (wearerSize <= 0 || attackerSize <= wearerSize) return;
        event.setAmount((float) (event.getAmount() * (1.0D + FTConfig.COMMON.feyweightLargerEnemyDamage.get())));
    }

    private float getEffectiveSize(LivingEntity entity) {
        float scale = SizeTrait.getScale(entity);
        if (scale <= 0) scale = 1.0f;
        return entity.getBbHeight() * scale;
    }

    private boolean setOrRemoveModifier(LivingEntity wearer, Attribute attr, UUID id, String name, double value, AttributeModifier.Operation op) {
        AttributeInstance ins = wearer.getAttribute(attr);
        if (ins == null) return false;

        AttributeModifier old = ins.getModifier(id);
        if (old != null && Double.compare(old.getAmount(), value) == 0 && old.getOperation() == op) {
            return false;
        }

        if (old != null) {
            ins.removeModifier(id);
        }
        if (value != 0) {
            ins.addTransientModifier(new AttributeModifier(id, name, value, op));
        }
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable(getDescriptionId() + ".desc_static",
                        Component.literal((int) Math.round(-FTConfig.COMMON.feyweightSizeReduction.get() * 100) + "%").withStyle(ChatFormatting.GOLD),
                        Component.literal((int) Math.round(FTConfig.COMMON.feyweightMovementSpeed.get() * 100) + "%").withStyle(ChatFormatting.GOLD),
                        Component.literal((int) Math.round(FTConfig.COMMON.feyweightAttackSpeed.get() * 100) + "%").withStyle(ChatFormatting.GOLD),
                        Component.literal((int) Math.round(FTConfig.COMMON.feyweightDodgeChance.get() * 100) + "%").withStyle(ChatFormatting.GOLD))
                .withStyle(ChatFormatting.GOLD));

        list.add(Component.translatable(getDescriptionId() + ".desc_vulnerability",
                        Component.literal((int) Math.round(FTConfig.COMMON.feyweightLargerEnemyDamage.get() * 100) + "%").withStyle(ChatFormatting.RED))
                .withStyle(ChatFormatting.RED));
    }

}