package net.kayn.fallen_traits.mixin.l2hostility;

import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import net.kayn.fallen_traits.content.traits.logic.LegendaryWeightHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = MobDifficultyCollector.class, remap = false)
public abstract class MobDifficultyCollectorMixin implements LegendaryWeightHolder {

    @Unique
    private double fallen_traits$legendaryWeightBonus = 0;

    @Override
    public double fallen_traits$getLegendaryWeightBonus() {
        return fallen_traits$legendaryWeightBonus;
    }

    @Override
    public void fallen_traits$addLegendaryWeightBonus(double bonus) {
        fallen_traits$legendaryWeightBonus += bonus;
    }

}