package net.kayn.fallen_traits.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class FTConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final Common COMMON = new Common(BUILDER);
    public static final ForgeConfigSpec COMMON_SPEC = BUILDER.build();

    public static class Common {

        public final ForgeConfigSpec.DoubleValue rageDamageIncreasePerHitPerLevel;
        public final ForgeConfigSpec.DoubleValue rageMaxBonusPerLevel;

        public final ForgeConfigSpec.IntValue mimicSearchRadius;
        public final ForgeConfigSpec.BooleanValue mimicCopyOffhand;

        public final ForgeConfigSpec.IntValue cloneMaxAlive;
        public final ForgeConfigSpec.IntValue cloneSpawnIntervalTicks;
        public final ForgeConfigSpec.DoubleValue cloneHealth;
        public final ForgeConfigSpec.DoubleValue cloneExplosionDamage;
        public final ForgeConfigSpec.DoubleValue cloneExplosionRadius;
        public final ForgeConfigSpec.BooleanValue cloneGlowEnabled;
        public final ForgeConfigSpec.IntValue cloneLifetimeTicks;

        public final ForgeConfigSpec.IntValue cleanseIntervalBase;
        public final ForgeConfigSpec.IntValue cleanseIntervalStep;
        public final ForgeConfigSpec.IntValue cleanseIntervalMin;

        public final ForgeConfigSpec.DoubleValue daywalkerDamageBonusPerLevel;
        public final ForgeConfigSpec.DoubleValue daywalkerSpeedBonusPerLevel;

        public final ForgeConfigSpec.DoubleValue nightcrawlerDamageBonusPerLevel;
        public final ForgeConfigSpec.DoubleValue nightcrawlerSpeedBonusPerLevel;

        public final ForgeConfigSpec.DoubleValue furyDamageIncreasePerHit;
        public final ForgeConfigSpec.DoubleValue furyMaxDamageMultiplier;
        public final ForgeConfigSpec.IntValue furyStackTimeoutTicks;
        public final ForgeConfigSpec.IntValue furyExtraDifficulty;
        public final ForgeConfigSpec.DoubleValue furyLegendaryChanceBonus;

        public final ForgeConfigSpec.DoubleValue mimicEquipmentDropChance;
        public final ForgeConfigSpec.BooleanValue allowLustToDropMimicEquipment;

        public final ForgeConfigSpec.IntValue berserkCataclysmDecrementPerLevel;

        public Common(ForgeConfigSpec.Builder builder) {

            // traits (behavior granted directly by the trait itself)
            builder.push("traits");
            {
                builder.push("rage_trait");

                rageDamageIncreasePerHitPerLevel = builder
                        .comment("extra damage per hit, per trait level (0.1 = +10% per level)")
                        .defineInRange("rageDamageIncreasePerHitPerLevel", 0.1, 0, 10);

                rageMaxBonusPerLevel = builder
                        .comment("damage cap, per trait level (1.0 = up to +100% per level)")
                        .defineInRange("rageMaxBonusPerLevel", 1.0, 0, 100);

                builder.pop();
                builder.push("mimic_trait");

                mimicSearchRadius = builder
                        .comment("only players within this many blocks of the mob are considered for copying, keeps the search cheap")
                        .defineInRange("mimicSearchRadius", 32, 1, 256);

                mimicCopyOffhand = builder
                        .comment("also copy the offhand item, not just armor and main hand")
                        .define("mimicCopyOffhand", true);

                builder.pop();
                builder.push("clone_trait");

                cloneMaxAlive = builder
                        .comment("max number of clones a single mob can have alive at once")
                        .defineInRange("cloneMaxAlive", 2, 0, 16);

                cloneSpawnIntervalTicks = builder
                        .comment("minimum ticks between spawning new clones, only while the mob has a target")
                        .defineInRange("cloneSpawnIntervalTicks", 100, 1, 72000);

                cloneHealth = builder
                        .comment("flat max health for clones, low so any hit kills them regardless of the original's health/traits")
                        .defineInRange("cloneHealth", 1.0, 1, 1000);

                cloneExplosionDamage = builder
                        .comment("max damage dealt to entities at the center of a clone's death explosion, falls off linearly to 0 at the radius edge")
                        .defineInRange("cloneExplosionDamage", 6.0, 0, 1000);

                cloneExplosionRadius = builder
                        .comment("radius in blocks of the clone's death explosion, does not break blocks")
                        .defineInRange("cloneExplosionRadius", 3.0, 0.5, 32);

                cloneGlowEnabled = builder
                        .comment("clones get a cyan glowing outline so they're distinguishable from the real mob")
                        .define("cloneGlowEnabled", true);

                cloneLifetimeTicks = builder
                        .comment("clones are force-removed after this many ticks even if not killed, 0 disables this and relies on normal despawn rules")
                        .defineInRange("cloneLifetimeTicks", 1200, 0, 72000);

                builder.pop();
                builder.push("cleanse_trait");

                cleanseIntervalBase = builder
                        .comment("seconds between cleanses at trait level 1")
                        .defineInRange("cleanseIntervalBase", 15, 1, 3600);

                cleanseIntervalStep = builder
                        .comment("seconds the interval shrinks by per additional trait level")
                        .defineInRange("cleanseIntervalStep", 5, 0, 3600);

                cleanseIntervalMin = builder
                        .comment("lowest possible interval regardless of trait level")
                        .defineInRange("cleanseIntervalMin", 5, 1, 3600);

                builder.pop();
                builder.push("daywalker_trait");

                daywalkerDamageBonusPerLevel = builder
                        .comment("attack damage multiplier per level while it is day (0.1 = +10%)")
                        .defineInRange("daywalkerDamageBonusPerLevel", 0.1, 0, 10);

                daywalkerSpeedBonusPerLevel = builder
                        .comment("movement speed multiplier per level while it is day (0.1 = +10%)")
                        .defineInRange("daywalkerSpeedBonusPerLevel", 0.05, 0, 10);

                builder.pop();
                builder.push("nightcrawler_trait");

                nightcrawlerDamageBonusPerLevel = builder
                        .comment("attack damage multiplier per level while it is night (0.1 = +10%)")
                        .defineInRange("nightcrawlerDamageBonusPerLevel", 0.1, 0, 10);

                nightcrawlerSpeedBonusPerLevel = builder
                        .comment("movement speed multiplier per level while it is night (0.1 = +10%)")
                        .defineInRange("nightcrawlerSpeedBonusPerLevel", 0.05, 0, 10);

                builder.push("berserk_trait");

                berserkCataclysmDecrementPerLevel = builder
                        .comment("extra cooldown ticks burned per game tick, per trait level, for Cataclysm boss cooldowns")
                        .defineInRange("berserkCataclysmDecrementPerLevel", 60, 0, 10000);
            }
            builder.pop();

            // items (curios and their interactions with other mod items)
            builder.push("items");
            {
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
                builder.push("mimic_equipment");

                mimicEquipmentDropChance = builder
                        .comment("drop chance for equipment copied by Mimic when the mob dies normally, 0 means it never drops")
                        .defineInRange("mimicEquipmentDropChance", 0.0, 0, 1);

                allowLustToDropMimicEquipment = builder
                        .comment("if false (default), Curse of Lust will not force Mimic-copied equipment to drop,",
                                "preventing players from easily duping their own gear by killing a Mimic wearing it")
                        .define("allowLustToDropMimicEquipment", false);

                builder.pop();
            }
            builder.pop();
        }

    }

}