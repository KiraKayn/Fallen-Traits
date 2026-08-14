package net.kayn.fallen_traits.content.traits.basic;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.content.capability.mob.CapStorageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.kayn.fallen_traits.init.FTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ShredderTrait extends MobTrait {

    public ShredderTrait(ChatFormatting style) {
        super(style);
    }

    @Override
    public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        var event = cache.getLivingHurtEvent();
        if (event == null || event.getAmount() <= 0) return;
        if (!(traitCache.target instanceof Player player)) return;

        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);
        if (armor == null) return;
        double percent = level * FTConfig.COMMON.shredderPercentPerLevel.get();
        double amount = armor.getValue() * percent;
        if (amount <= 0) return;

        MobTraitCap cap = MobTraitCap.HOLDER.get(attacker);
        Data data = cap.getOrCreateData(getRegistryName(), Data::new);
        double total = data.stolen.merge(player.getUUID(), amount, Double::sum);

        UUID targetId = MathHelper.getUUIDFromString("shredder_target_" + attacker.getUUID() + "_" + player.getUUID());
        var targetMod = new AttributeModifier(targetId, "fallen_traits_shredder", -total, AttributeModifier.Operation.ADDITION);
        if (armor.hasModifier(targetMod)) armor.removeModifier(targetId);
        armor.addPermanentModifier(targetMod);

        AttributeInstance selfArmor = attacker.getAttribute(Attributes.ARMOR);
        if (selfArmor != null) {
            UUID selfId = MathHelper.getUUIDFromString("shredder_self_" + attacker.getUUID() + "_" + player.getUUID());
            var selfMod = new AttributeModifier(selfId, "fallen_traits_shredder", total, AttributeModifier.Operation.ADDITION);
            if (selfArmor.hasModifier(selfMod)) selfArmor.removeModifier(selfId);
            selfArmor.addPermanentModifier(selfMod);
        }
    }

    @Override
    public void onDeath(int level, LivingEntity entity, LivingDeathEvent event) {
        if (entity.level().isClientSide()) return;
        MobTraitCap cap = MobTraitCap.HOLDER.get(entity);
        Data data = cap.getData(getRegistryName());
        if (data == null) return;
        if (entity.level() instanceof ServerLevel sl) {
            for (UUID playerId : data.stolen.keySet()) {
                Player player = sl.getServer().getPlayerList().getPlayer(playerId);
                if (player == null) continue;
                AttributeInstance armor = player.getAttribute(Attributes.ARMOR);
                if (armor != null) {
                    armor.removeModifier(MathHelper.getUUIDFromString("shredder_target_" + entity.getUUID() + "_" + playerId));
                }
            }
        }
        data.stolen.clear();
    }

    @Override
    public void addDetail(List<Component> list) {
        Component percent = mapLevel(i -> Component.literal(
                        Math.round(i * FTConfig.COMMON.shredderPercentPerLevel.get() * 100) + "%")
                .withStyle(ChatFormatting.AQUA));
        list.add(Component.translatable(getDescriptionId() + ".desc", percent).withStyle(ChatFormatting.GRAY));
    }

    @SerialClass
    public static class Data extends CapStorageData {

        @SerialClass.SerialField
        public final HashMap<UUID, Double> stolen = new HashMap<>();

    }

}