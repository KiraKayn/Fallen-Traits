package net.kayn.fallen_traits.mixin.l2hostility;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.content.logic.TraitGenerator;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import net.kayn.fallen_traits.content.traits.logic.ExtraTraitHolder;
import net.kayn.fallen_traits.content.traits.logic.LegendaryWeightHolder;
import net.kayn.fallen_traits.content.traits.logic.OverMaxTraitHolder;
import net.kayn.fallen_traits.content.traits.logic.TraitCompatibility;
import net.kayn.fallen_traits.content.traits.logic.UnlimitedTraitCountHolder;
import net.kayn.fallen_traits.init.FTTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.tags.ITagManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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

    @Shadow
    @Final
    private MobDifficultyCollector ins;

    @Final
    @Mutable
    @Shadow
    private int maxTrait;

    @Shadow
    @Final
    private HashMap<MobTrait, Integer> traits;

    @Shadow
    @Final
    private LivingEntity entity;

    @Unique
    private MobTrait fallen_traits$currentTrait;

    @Inject(method = "<init>(Ldev/xkmc/l2hostility/content/capability/mob/MobTraitCap;Lnet/minecraft/world/entity/LivingEntity;ILjava/util/HashMap;Ldev/xkmc/l2hostility/content/logic/MobDifficultyCollector;)V",
            at = @At("TAIL"))
    private void fallen_traits$applyBonuses(MobTraitCap cap, LivingEntity entity, int mobLevel,
                                            HashMap<MobTrait, Integer> traits, MobDifficultyCollector ins, CallbackInfo ci) {
        double bonus = ((LegendaryWeightHolder) ins).fallen_traits$getLegendaryWeightBonus();
        if (bonus > 0) {
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

        int extraTraits = ((ExtraTraitHolder) ins).fallen_traits$getExtraTraitCount();
        if (extraTraits > 0 && maxTrait > 0) {
            maxTrait += extraTraits;
        }

        if (((UnlimitedTraitCountHolder) ins).fallen_traits$isUnlimitedTraitCount()) {
            maxTrait = -1;
        }
    }

    @ModifyVariable(method = "generate", at = @At(value = "STORE", ordinal = 0), index = 1)
    private MobTrait fallen_traits$capturePoppedTrait(MobTrait trait) {
        fallen_traits$currentTrait = trait;
        return trait;
    }

    @ModifyVariable(method = "generate", at = @At(value = "STORE", ordinal = 0), index = 3)
    private int fallen_traits$bumpMaxRank(int max) {
        if (((OverMaxTraitHolder) ins).fallen_traits$isOverMax()) {
            if (fallen_traits$currentTrait != null && !isInNoOverMaxTag(fallen_traits$currentTrait)) {
                return max + 1;
            }
        }
        return max;
    }

    @Inject(method = "generate", at = @At(value = "INVOKE", target = "Ljava/util/HashMap;entrySet()Ljava/util/Set;"))
    private void fallen_traits$resolveBeforeInit(CallbackInfo ci) {
        TraitCompatibility.resolveMap(this.entity, this.traits);
    }

    @Unique
    private static boolean isInNoOverMaxTag(MobTrait trait) {
        IForgeRegistry<MobTrait> registry = dev.xkmc.l2hostility.init.registrate.LHTraits.TRAITS.get();
        if (registry == null) return false;

        ResourceLocation traitId = registry.getKey(trait);
        if (traitId == null) return false;

        ITagManager<MobTrait> tagManager = registry.tags();
        if (tagManager == null) return false;

        var tag = tagManager.getTag(FTTags.NO_OVER_MAX);
        if (tag == null) return false;

        return tag.contains(trait);
    }
}