package net.kayn.fallen_traits.mixin.l2hostility;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.content.logic.TraitGenerator;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import net.kayn.fallen_traits.content.traits.logic.LegendaryWeightHolder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mixin(value = TraitGenerator.class, remap = false)
public abstract class TraitGeneratorMixin {

    @Shadow
    @Final
    private List<MobTrait> traitPool;

    @Shadow
    private int weights;

    @Shadow
    @Final
    private RandomSource rand;

    @Inject(method = "<init>(Ldev/xkmc/l2hostility/content/capability/mob/MobTraitCap;Lnet/minecraft/world/entity/LivingEntity;ILjava/util/HashMap;Ldev/xkmc/l2hostility/content/logic/MobDifficultyCollector;)V",
            at = @At("TAIL"))
    private void fallen_traits$boostLegendaryWeight(MobTraitCap cap, LivingEntity entity, int mobLevel,
                                                    HashMap<MobTrait, Integer> traits, MobDifficultyCollector ins, CallbackInfo ci) {
        double bonus = ((LegendaryWeightHolder) ins).fallen_traits$getLegendaryWeightBonus();
        if (bonus <= 0) return;

        List<MobTrait> legendary = new ArrayList<>();
        for (MobTrait trait : traitPool) {
            if (trait instanceof LegendaryTrait) {
                legendary.add(trait);
            }
        }

        for (MobTrait trait : legendary) {
            int extraCopies = (int) bonus;
            double frac = bonus - extraCopies;
            if (frac > 0 && rand.nextDouble() < frac) extraCopies++;
            for (int i = 0; i < extraCopies; i++) {
                traitPool.add(trait);
                weights += trait.getConfig().weight;
            }
        }
    }

}