package net.kayn.fallen_traits.content.traits;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.kayn.fallen_traits.init.FTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.ArrayList;
import java.util.List;

public class CleanseTrait extends MobTrait {

    public CleanseTrait(ChatFormatting style) {
        super(style);
    }

    private int getInterval(int level) {
        int base = FTConfig.COMMON.cleanseIntervalBase.get();
        int step = FTConfig.COMMON.cleanseIntervalStep.get();
        int min = FTConfig.COMMON.cleanseIntervalMin.get();
        return Math.max(min, base - (level - 1) * step) * 20;
    }

    @Override
    public void tick(LivingEntity mob, int level) {
        if (mob.level().isClientSide()) return;
        if (mob.tickCount % getInterval(level) != 0) return;
        LivingEntity target = mob instanceof Mob m ? m.getTarget() : null;
        List<MobEffectInstance> toStrip = new ArrayList<>();
        for (var ins : mob.getActiveEffects()) {
            if (ins.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                toStrip.add(ins);
            }
        }
        for (var ins : toStrip) {
            mob.removeEffect(ins.getEffect());
            if (target != null) {
                EffectUtil.addEffect(target, new MobEffectInstance(ins), EffectUtil.AddReason.NONE, mob);
            }
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
        list.add(mapLevel(e -> Component.literal((getInterval(e) / 20) + "s").withStyle(ChatFormatting.AQUA)));
    }

}