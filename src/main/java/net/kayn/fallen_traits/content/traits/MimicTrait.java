package net.kayn.fallen_traits.content.traits;

import dev.xkmc.l2hostility.content.capability.mob.CapStorageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.PlayerFinder;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.kayn.fallen_traits.init.FTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MimicTrait extends LegendaryTrait {

    public MimicTrait(ChatFormatting style) {
        super(style);
    }

    @Override
    public void postInit(LivingEntity mob, int lv) {
        if (mob.level().isClientSide()) return;
        Player target = PlayerFinder.getNearestPlayer(mob.level(), mob);
        if (target == null) return;
        int radius = FTConfig.COMMON.mimicSearchRadius.get();
        if (target.distanceToSqr(mob) > (double) radius * radius) return;
        boolean copyOffhand = FTConfig.COMMON.mimicCopyOffhand.get();
        float dropChance = FTConfig.COMMON.mimicEquipmentDropChance.get().floatValue();
        MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
        Data data = cap.getOrCreateData(getRegistryName(), Data::new);
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot == EquipmentSlot.OFFHAND && !copyOffhand) continue;
            ItemStack stack = target.getItemBySlot(slot);
            if (stack.isEmpty()) continue;
            mob.setItemSlot(slot, stack.copy());
            if (mob instanceof Mob m) {
                m.setDropChance(slot, dropChance);
            }
            data.copiedSlots.add(slot.getName());
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        int radius = FTConfig.COMMON.mimicSearchRadius.get();

        Component radiusComponent = Component.literal(String.valueOf(radius))
                .withStyle(ChatFormatting.AQUA);

        list.add(Component.translatable(getDescriptionId() + ".desc", radiusComponent)
                .withStyle(ChatFormatting.GRAY));
    }

    @SerialClass
    public static class Data extends CapStorageData {

        @SerialClass.SerialField
        public final ArrayList<String> copiedSlots = new ArrayList<>();

    }

}