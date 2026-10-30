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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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

    private static final String NBT_KEY = "fallen_traits_shredder_steals";
    private static final String MOD_NAME = "fallen_traits_shredder";

    public ShredderTrait(ChatFormatting style) {
        super(style);
    }

    private static UUID targetModId(UUID stealId) {
        return MathHelper.getUUIDFromString("shredder_target_" + stealId);
    }

    private static UUID selfModId(UUID stealId) {
        return MathHelper.getUUIDFromString("shredder_self_" + stealId);
    }

    private static int returnTicks(int level) {
        return FTConfig.COMMON.shredderReturnBaseTicks.get()
                + (level - 1) * FTConfig.COMMON.shredderReturnTicksPerLevel.get();
    }

    @Override
    public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        var event = cache.getLivingHurtEvent();
        if (event == null || event.getAmount() <= 0) return;
        if (!(traitCache.target instanceof Player player)) return;

        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);
        if (armor == null) return;
        double amount = armor.getValue() * level * FTConfig.COMMON.shredderPercentPerLevel.get();
        if (amount <= 0) return;

        UUID stealId = UUID.randomUUID();
        long until = player.level().getGameTime() + returnTicks(level);

        armor.addPermanentModifier(new AttributeModifier(targetModId(stealId), MOD_NAME, -amount, AttributeModifier.Operation.ADDITION));

        AttributeInstance selfArmor = attacker.getAttribute(Attributes.ARMOR);
        if (selfArmor != null) {
            selfArmor.addPermanentModifier(new AttributeModifier(selfModId(stealId), MOD_NAME, amount, AttributeModifier.Operation.ADDITION));
        }

        MobTraitCap cap = MobTraitCap.HOLDER.get(attacker);
        Data data = cap.getOrCreateData(getRegistryName(), Data::new);
        data.until.put(stealId, until);
        data.players.put(stealId, player.getUUID());

        CompoundTag root = player.getPersistentData();
        ListTag list = root.getList(NBT_KEY, Tag.TAG_COMPOUND);
        CompoundTag entry = new CompoundTag();
        entry.putUUID("id", stealId);
        entry.putLong("until", until);
        list.add(entry);
        root.put(NBT_KEY, list);
    }

    @Override
    public void tick(LivingEntity mob, int level) {
        if (mob.level().isClientSide() || mob.tickCount % 20 != 0) return;
        MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
        Data data = cap.getData(getRegistryName());
        if (data == null || data.until.isEmpty()) return;
        long time = mob.level().getGameTime();
        var it = data.until.entrySet().iterator();
        while (it.hasNext()) {
            var entry = it.next();
            if (entry.getValue() > time) continue;
            release(mob, entry.getKey(), data.players.remove(entry.getKey()));
            it.remove();
        }
    }

    @Override
    public void onDeath(int level, LivingEntity entity, LivingDeathEvent event) {
        if (entity.level().isClientSide()) return;
        MobTraitCap cap = MobTraitCap.HOLDER.get(entity);
        Data data = cap.getData(getRegistryName());
        if (data == null) return;
        for (UUID stealId : data.until.keySet()) {
            release(entity, stealId, data.players.get(stealId));
        }
        data.until.clear();
        data.players.clear();
    }

    private static void release(LivingEntity mob, UUID stealId, UUID playerId) {
        AttributeInstance selfArmor = mob.getAttribute(Attributes.ARMOR);
        if (selfArmor != null) {
            selfArmor.removeModifier(selfModId(stealId));
        }
        if (playerId == null || !(mob.level() instanceof ServerLevel sl)) return;
        Player player = sl.getServer().getPlayerList().getPlayer(playerId);
        if (player == null) return;
        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);
        if (armor != null) {
            armor.removeModifier(targetModId(stealId));
        }
    }

    public static void tickPlayer(Player player) {
        CompoundTag root = player.getPersistentData();
        if (!root.contains(NBT_KEY)) return;
        ListTag list = root.getList(NBT_KEY, Tag.TAG_COMPOUND);
        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);
        long time = player.level().getGameTime();
        for (int i = list.size() - 1; i >= 0; i--) {
            CompoundTag entry = list.getCompound(i);
            if (entry.getLong("until") > time) continue;
            if (armor != null) {
                armor.removeModifier(targetModId(entry.getUUID("id")));
            }
            list.remove(i);
        }
        if (list.isEmpty()) {
            root.remove(NBT_KEY);
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        Component percent = mapLevel(i -> Component.literal(
                        Math.round(i * FTConfig.COMMON.shredderPercentPerLevel.get() * 100) + "%")
                .withStyle(ChatFormatting.AQUA));
        Component seconds = mapLevel(i -> Component.literal(
                        Math.round(returnTicks(i) / 20.0) + "")
                .withStyle(ChatFormatting.AQUA));
        list.add(Component.translatable(getDescriptionId() + ".desc", percent, seconds).withStyle(ChatFormatting.GRAY));
    }

    @SerialClass
    public static class Data extends CapStorageData {

        @SerialClass.SerialField
        public final HashMap<UUID, Long> until = new HashMap<>();

        @SerialClass.SerialField
        public final HashMap<UUID, UUID> players = new HashMap<>();

    }

}