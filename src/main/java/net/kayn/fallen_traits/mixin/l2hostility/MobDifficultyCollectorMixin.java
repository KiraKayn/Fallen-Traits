package net.kayn.fallen_traits.mixin.l2hostility;

import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import net.kayn.fallen_traits.content.traits.logic.ExtraTraitHolder;
import net.kayn.fallen_traits.content.traits.logic.LegendaryWeightHolder;
import net.kayn.fallen_traits.content.traits.logic.OverMaxTraitHolder;
import net.kayn.fallen_traits.content.traits.logic.UnlimitedTraitCountHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = MobDifficultyCollector.class, remap = false)
public abstract class MobDifficultyCollectorMixin implements LegendaryWeightHolder, ExtraTraitHolder, OverMaxTraitHolder, UnlimitedTraitCountHolder {

    @Unique
    private double fallen_traits$legendaryWeightBonus = 0;

    @Unique
    private int fallen_traits$extraTraitCount = 0;

    @Unique
    private boolean fallen_traits$overMax = false;

    @Unique
    private boolean fallen_traits$unlimitedTraitCount = false;

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

    @Override
    public boolean fallen_traits$isOverMax() {
        return fallen_traits$overMax;
    }

    @Override
    public void fallen_traits$setOverMax(boolean value) {
        fallen_traits$overMax = value;
    }

    @Override
    public boolean fallen_traits$isUnlimitedTraitCount() {
        return fallen_traits$unlimitedTraitCount;
    }

    @Override
    public void fallen_traits$setUnlimitedTraitCount(boolean value) {
        fallen_traits$unlimitedTraitCount = value;
    }

}