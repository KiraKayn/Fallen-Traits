package net.kayn.fallen_traits.content.traits;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.capability.mob.CapStorageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.kayn.fallen_traits.init.FTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class RageTrait extends LegendaryTrait {

    public RageTrait(ChatFormatting style) {
        super(style);
    }

    @Override
    public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        var event = cache.getLivingHurtEvent();
        if (event == null || event.getAmount() <= 0) return;
        MobTraitCap cap = MobTraitCap.HOLDER.get(attacker);
        Data data = cap.getOrCreateData(getRegistryName(), Data::new);
        String targetId = traitCache.target.getStringUUID();
        if (!targetId.equals(data.targetId)) {
            data.targetId = targetId;
            data.stacks = 0;
        }
        double inc = level * FTConfig.COMMON.rageDamageIncreasePerHitPerLevel.get();
        double max = level * FTConfig.COMMON.rageMaxBonusPerLevel.get();
        double factor = Math.min(1 + max, 1 + data.stacks * inc);
        cache.addHurtModifier(DamageModifier.multBase((float) factor));
        data.stacks++;
    }

    @Override
    public void onDamaged(int level, LivingEntity mob, AttackCache cache) {
        MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
        Data data = cap.getOrCreateData(getRegistryName(), Data::new);
        data.stacks = 0;
        data.targetId = "";
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
        list.add(mapLevel(i -> Component.literal(
                        Math.round(i * FTConfig.COMMON.rageDamageIncreasePerHitPerLevel.get() * 100) + "%/hit, up to +" +
                                Math.round(i * FTConfig.COMMON.rageMaxBonusPerLevel.get() * 100) + "%")
                .withStyle(ChatFormatting.RED)));
    }

    @SerialClass
    public static class Data extends CapStorageData {

        @SerialClass.SerialField
        public String targetId = "";

        @SerialClass.SerialField
        public int stacks = 0;

    }

}