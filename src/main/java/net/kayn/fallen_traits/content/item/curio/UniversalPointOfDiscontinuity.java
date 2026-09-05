package net.kayn.fallen_traits.content.item.curio;

import dev.xkmc.l2hostility.content.item.curio.core.MultiSlotItem;
import net.kayn.fallen_traits.content.traits.legendary.SizeTrait;
import net.kayn.fallen_traits.init.FTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class UniversalPointOfDiscontinuity extends MultiSlotItem {

    private static final UUID ASYMPTOTE_SLOW_ID = dev.xkmc.l2library.util.math.MathHelper.getUUIDFromString("fallen_traits_upd_asymptote_slow");


    public UniversalPointOfDiscontinuity(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            cycleMode(stack);
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (!stack.getOrCreateTag().contains("mode")) {
            stack.getOrCreateTag().putInt("mode", 3);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if (wearer == null) return;

        int range = FTConfig.COMMON.updAsymptoteRange.get();
        List<LivingEntity> list = wearer.level().getEntities(
                EntityTypeTest.forClass(LivingEntity.class),
                wearer.getBoundingBox().inflate(range),
                ex -> ex != wearer && ex.isAlive()
        );

        for (LivingEntity e : list) {
            var movementAttr = e.getAttribute(Attributes.MOVEMENT_SPEED);
            if (movementAttr != null) {
                movementAttr.removeModifier(ASYMPTOTE_SLOW_ID);
            }
        }
    }

    private void cycleMode(ItemStack stack) {
        int currentMode = getMode(stack);
        int nextMode = (currentMode + 1) % 4;
        setMode(stack, nextMode);
    }

    private int getMode(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        return tag.getInt("mode");
    }

    private void setMode(ItemStack stack, int mode) {
        var tag = stack.getOrCreateTag();
        tag.putInt("mode", mode);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if (wearer == null || wearer.level().isClientSide()) return;

        int mode = getMode(stack);

        switch (mode) {
            case 0 -> applyRepelling(wearer);
            case 1 -> applyAsymptote(wearer);
            case 2 -> applyPulling(wearer);
            case 3 -> { /* Disabled */ }
        }
    }

    private void applyRepelling(LivingEntity wearer) {
        int range = FTConfig.COMMON.updRepellingRange.get();
        double strengthBase = FTConfig.COMMON.updRepellingStrength.get();

        List<LivingEntity> list = wearer.level().getEntities(
                EntityTypeTest.forClass(LivingEntity.class),
                wearer.getBoundingBox().inflate(range),
                ex -> ex != wearer && ex.isAlive() && !isImmune(ex)
        );

        for (LivingEntity e : list) {
            double dist = wearer.distanceTo(e) / range;
            if (dist > 1.0) continue;

            double sizeFactor = getSizeFactor(wearer, e);
            double strength = (1.0 - dist) * strengthBase * sizeFactor;
            Vec3 dir = e.position().subtract(wearer.position());
            if (dir.horizontalDistanceSqr() > 1.0E-4) {
                dir = new Vec3(dir.x, 0, dir.z).normalize();
                e.push(dir.x * strength, 0, dir.z * strength);
            }
        }
    }

    private void applyAsymptote(LivingEntity wearer) {
        int range = FTConfig.COMMON.updAsymptoteRange.get();
        double slowStrength = FTConfig.COMMON.updAsymptoteSlowStrength.get();

        List<LivingEntity> list = wearer.level().getEntities(
                EntityTypeTest.forClass(LivingEntity.class),
                wearer.getBoundingBox().inflate(range),
                ex -> ex != wearer && ex.isAlive() && !isImmune(ex)
        );

        for (LivingEntity e : list) {
            double dist = wearer.distanceTo(e) / range;
            if (dist > 1.0) continue;

            double sizeFactor = getSizeFactor(wearer, e);
            double effectiveSlow = Math.min(1.0, slowStrength * sizeFactor);
            double slowMultiplier = 1.0 - (1.0 - dist) * effectiveSlow;

            var movementAttr = e.getAttribute(Attributes.MOVEMENT_SPEED);
            if (movementAttr != null) {
                movementAttr.removeModifier(ASYMPTOTE_SLOW_ID);
                if (slowMultiplier < 1.0) {
                    movementAttr.addTransientModifier(new AttributeModifier(
                            ASYMPTOTE_SLOW_ID,
                            "fallen_traits_upd_asymptote_slow",
                            slowMultiplier - 1.0,
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
                }
            }
        }
    }

    private void applyPulling(LivingEntity wearer) {
        int range = FTConfig.COMMON.updPullingRange.get();
        double strengthBase = FTConfig.COMMON.updPullingStrength.get();

        List<LivingEntity> list = wearer.level().getEntities(
                EntityTypeTest.forClass(LivingEntity.class),
                wearer.getBoundingBox().inflate(range),
                ex -> ex != wearer && ex.isAlive() && !isImmune(ex)
        );

        for (LivingEntity e : list) {
            double dist = wearer.distanceTo(e) / range;
            if (dist > 1.0) continue;

            double sizeFactor = getSizeFactor(wearer, e);
            double strength = (1.0 - dist) * dist * strengthBase * sizeFactor * -4.0;
            Vec3 dir = e.position().subtract(wearer.position());
            if (dir.horizontalDistanceSqr() > 1.0E-4) {
                dir = new Vec3(dir.x, 0, dir.z).normalize();
                e.push(dir.x * strength, 0, dir.z * strength);
            }
        }
    }

    private double getSizeFactor(LivingEntity wearer, LivingEntity target) {
        float wearerSize = getEffectiveSize(wearer);
        float targetSize = getEffectiveSize(target);
        if (wearerSize <= 0 || targetSize <= 0) return 1.0;
        return wearerSize / targetSize;
    }

    private float getEffectiveSize(LivingEntity entity) {
        float scale = SizeTrait.getScale(entity);
        if (scale <= 0) scale = 1.0f;
        return entity.getBbHeight() * scale;
    }

    private boolean isImmune(LivingEntity entity) {
        if (entity instanceof Player pl) {
            return pl.getAbilities().instabuild || pl.isSpectator();
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        int mode = getMode(stack);

        switch (mode) {
            case 0 -> {
                list.add(Component.translatable(getDescriptionId() + ".mode.0").withStyle(ChatFormatting.GOLD));
                list.add(Component.translatable(getDescriptionId() + ".desc_repelling",
                        Component.literal(FTConfig.COMMON.updRepellingRange.get() + "").withStyle(ChatFormatting.RED),
                        Component.literal((int) Math.round(FTConfig.COMMON.updRepellingStrength.get() * 100) + "%").withStyle(ChatFormatting.RED)
                ).withStyle(ChatFormatting.GOLD));
            }
            case 1 -> {
                list.add(Component.translatable(getDescriptionId() + ".mode.1").withStyle(ChatFormatting.GOLD));
                list.add(Component.translatable(getDescriptionId() + ".desc_asymptote",
                        Component.literal((int) Math.round(FTConfig.COMMON.updAsymptoteSlowStrength.get() * 100) + "%").withStyle(ChatFormatting.RED),
                        Component.literal(FTConfig.COMMON.updAsymptoteRange.get() + "").withStyle(ChatFormatting.RED)
                ).withStyle(ChatFormatting.GOLD));
            }
            case 2 -> {
                list.add(Component.translatable(getDescriptionId() + ".mode.2").withStyle(ChatFormatting.GOLD));
                list.add(Component.translatable(getDescriptionId() + ".desc_pulling",
                        Component.literal(FTConfig.COMMON.updPullingRange.get() + "").withStyle(ChatFormatting.RED),
                        Component.literal((int) Math.round(FTConfig.COMMON.updPullingStrength.get() * 100) + "%").withStyle(ChatFormatting.RED)
                ).withStyle(ChatFormatting.GOLD));
            }
            case 3 -> {
                list.add(Component.translatable(getDescriptionId() + ".mode.3").withStyle(ChatFormatting.GOLD));
                list.add(Component.translatable(getDescriptionId() + ".desc_disabled").withStyle(ChatFormatting.GOLD));
            }
        }

        list.add(Component.translatable(getDescriptionId() + ".desc_use").withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}