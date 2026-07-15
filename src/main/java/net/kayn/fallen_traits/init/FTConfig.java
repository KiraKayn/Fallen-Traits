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

            rageDamageIncreasePerHitPerLevel = builder
                    .comment("extra damage per hit, per trait level (0.1 = +10% per level)")
                    .defineInRange("rageDamageIncreasePerHitPerLevel", 0.1, 0, 10);

            rageMaxBonusPerLevel = builder
                    .comment("damage cap, per trait level (1.0 = up to +100% per level)")
                    .defineInRange("rageMaxBonusPerLevel", 1.0, 0, 100);

            builder.pop();
            builder.push("rage_glove");

            furyDamageIncreasePerHit = builder
                    .comment("extra damage per hit while stacking")
                    .defineInRange("furyDamageIncreasePerHit", 0.25, 0, 10);

            furyMaxDamageMultiplier = builder
                    .comment("damage multiplier cap, 5.0 = 5x damage")
                    .defineInRange("furyMaxDamageMultiplier", 5.0, 1, 100);

            furyStackTimeoutTicks = builder
                    .comment("stacks reset if no hit lands within this many ticks")
                    .defineInRange("furyStackTimeoutTicks", 200, 1, 72000);

            furyExtraDifficulty = builder
                    .comment("extra mob difficulty while worn")
                    .defineInRange("furyExtraDifficulty", 50, 0, 10000);

            furyLegendaryChanceBonus = builder
                    .comment("bonus chance for spawned mobs to roll a legendary trait")
                    .defineInRange("furyLegendaryChanceBonus", 0.02, 0, 1);

            builder.pop();
        }

    }

}