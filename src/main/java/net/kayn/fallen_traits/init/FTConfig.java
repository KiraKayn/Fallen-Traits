package net.kayn.fallen_traits.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class FTConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final Common COMMON = new Common(BUILDER);
    public static final ForgeConfigSpec COMMON_SPEC = BUILDER.build();

    public static class Common {

        public final ForgeConfigSpec.DoubleValue rageDamageIncreasePerHitPerLevel;
        public final ForgeConfigSpec.DoubleValue rageMaxBonusPerLevel;

        public final ForgeConfigSpec.DoubleValue furyDamageIncreasePerHit;
        public final ForgeConfigSpec.DoubleValue furyMaxDamageMultiplier;
        public final ForgeConfigSpec.IntValue furyStackTimeoutTicks;
        public final ForgeConfigSpec.IntValue furyExtraDifficulty;
        public final ForgeConfigSpec.DoubleValue furyLegendaryChanceBonus;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("rage_trait");

            // extra damage per hit, per trait level (0.1 = +10% per level)
            rageDamageIncreasePerHitPerLevel = builder.defineInRange("rageDamageIncreasePerHitPerLevel", 0.1, 0, 10);
            // damage cap, per trait level (1.0 = up to +100% per level)
            rageMaxBonusPerLevel = builder.defineInRange("rageMaxBonusPerLevel", 1.0, 0, 100);

            builder.pop();
            builder.push("rage_glove");

            // extra damage per hit while stacking
            furyDamageIncreasePerHit = builder.defineInRange("furyDamageIncreasePerHit", 0.25, 0, 10);
            // damage multiplier cap, 5.0 = 5x damage
            furyMaxDamageMultiplier = builder.defineInRange("furyMaxDamageMultiplier", 5.0, 1, 100);
            // stacks reset if no hit lands within this many ticks
            furyStackTimeoutTicks = builder.defineInRange("furyStackTimeoutTicks", 200, 1, 72000);
            // extra mob difficulty while worn
            furyExtraDifficulty = builder.defineInRange("furyExtraDifficulty", 50, 0, 10000);
            // bonus chance for spawned mobs to roll a legendary trait
            furyLegendaryChanceBonus = builder.defineInRange("furyLegendaryChanceBonus", 0.02, 0, 1);

            builder.pop();
        }

    }

}