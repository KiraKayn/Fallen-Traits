package net.kayn.fallen_traits.mixin.l2hostility;

import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import net.kayn.fallen_traits.content.traits.logic.ExtraTraitHolder;
import net.kayn.fallen_traits.content.traits.logic.LegendaryWeightHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = MobDifficultyCollector.class, remap = false)
public abstract class MobDifficultyCollectorMixin implements LegendaryWeightHolder, ExtraTraitHolder {

    @Unique
    private double fallen_traits$legendaryWeightBonus = 0;

    @Unique
    private int fallen_traits$extraTraitCount = 0;

    @Override
    public double fallen_traits$getLegendaryWeightBonus() {
        return fallen_traits$legendaryWeightBonus;
    }

    @Override
    public void fallen_traits$addLegendaryWeightBonus(double bonus) {
        fallen_traits$legendaryWeightBonus += bonus;
    }

    @Override
    public int fallen_traits$getExtraTraitCount() {
        return fallen_traits$extraTraitCount;
    }

    @Override
    public void fallen_traits$addExtraTraitCount(int amount) {
        fallen_traits$extraTraitCount += amount;
    }

}