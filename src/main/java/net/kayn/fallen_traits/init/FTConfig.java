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

        public final ForgeConfigSpec.IntValue infernalTriggerDistanceBase;
        public final ForgeConfigSpec.IntValue infernalTriggerDistancePerLevel;
        public final ForgeConfigSpec.IntValue infernalTriggerDurationBaseTicks;
        public final ForgeConfigSpec.IntValue infernalTriggerDurationPerLevelTicks;
        public final ForgeConfigSpec.IntValue infernalEnrageDurationTicks;
        public final ForgeConfigSpec.IntValue infernalCooldownBaseTicks;
        public final ForgeConfigSpec.IntValue infernalCooldownPerLevelTicks;
        public final ForgeConfigSpec.DoubleValue infernalDamageBonusPerLevel;
        public final ForgeConfigSpec.DoubleValue infernalSpeedBonusPerLevel;
        public final ForgeConfigSpec.IntValue infernalLungeIntervalTicks;
        public final ForgeConfigSpec.DoubleValue infernalLungeStrength;
        public final ForgeConfigSpec.IntValue infernalActionbarRadius;

        public final ForgeConfigSpec.DoubleValue mimicEquipmentDropChance;
        public final ForgeConfigSpec.BooleanValue allowLustToDropMimicEquipment;

        public final ForgeConfigSpec.IntValue berserkCataclysmDecrementPerLevel;
        public final ForgeConfigSpec.DoubleValue berserkISSFactorPerLevel;

        public final ForgeConfigSpec.IntValue invulnBreakerTargetInvulnReductionTicks;
        public final ForgeConfigSpec.IntValue invulnBreakerWearerInvulnBonusTicks;
        public final ForgeConfigSpec.IntValue invulnBreakerExtraDifficulty;

        public final ForgeConfigSpec.IntValue furyInfernalExtraLevel;
        public final ForgeConfigSpec.DoubleValue furyInfernalDamagePerTraitLevel;
        public final ForgeConfigSpec.DoubleValue furyInfernalCritDamagePerLegendaryLevel;
        public final ForgeConfigSpec.IntValue furyInfernalAttackSpeedTraitThreshold;
        public final ForgeConfigSpec.DoubleValue furyInfernalAttackSpeedBase;
        public final ForgeConfigSpec.DoubleValue furyInfernalAttackSpeedPerBlock;
        public final ForgeConfigSpec.IntValue furyInfernalMoveSpeedTraitThreshold;
        public final ForgeConfigSpec.DoubleValue furyInfernalMoveSpeedBase;
        public final ForgeConfigSpec.DoubleValue furyInfernalMoveSpeedPerBlock;
        public final ForgeConfigSpec.IntValue furyInfernalMaxBonusBlocks;
        public final ForgeConfigSpec.IntValue furyInfernalTargetTimeoutTicks;

        public final ForgeConfigSpec.DoubleValue devourerDrainPercentPerLevel;
        public final ForgeConfigSpec.IntValue devourerRadiusPerLevel;

        public final ForgeConfigSpec.DoubleValue shredderPercentPerLevel;

        public final ForgeConfigSpec.DoubleValue titanSizeBase;
        public final ForgeConfigSpec.DoubleValue titanSizeStep;
        public final ForgeConfigSpec.DoubleValue titanHealthBase;
        public final ForgeConfigSpec.DoubleValue titanHealthStep;
        public final ForgeConfigSpec.DoubleValue titanKnockbackBase;
        public final ForgeConfigSpec.DoubleValue titanKnockbackStep;

        public final ForgeConfigSpec.DoubleValue dwarfSizeBase;
        public final ForgeConfigSpec.DoubleValue dwarfSizeStep;
        public final ForgeConfigSpec.DoubleValue dwarfSpeedBase;
        public final ForgeConfigSpec.DoubleValue dwarfSpeedStep;
        public final ForgeConfigSpec.DoubleValue dwarfDodgeBase;
        public final ForgeConfigSpec.DoubleValue dwarfDodgeStep;

        public final ForgeConfigSpec.IntValue titansHeartExtraDifficulty;
        public final ForgeConfigSpec.DoubleValue titansHeartSize;
        public final ForgeConfigSpec.DoubleValue titansHeartHealth;
        public final ForgeConfigSpec.DoubleValue titansHeartArmor;
        public final ForgeConfigSpec.DoubleValue titansHeartLegendaryChanceBonus;

        public final ForgeConfigSpec.IntValue lawOfScaleExtraDifficulty;
        public final ForgeConfigSpec.IntValue lawOfScaleExtraTraitCount;
        public final ForgeConfigSpec.DoubleValue lawOfScaleMaxDamagePercent;
        public final ForgeConfigSpec.DoubleValue lawOfScaleDamagePercentPerSizeRatio;
        public final ForgeConfigSpec.DoubleValue lawOfScaleKnockbackBase;
        public final ForgeConfigSpec.DoubleValue lawOfScaleKnockbackPerSizeRatio;
        public final ForgeConfigSpec.DoubleValue lawOfScaleKnockbackMax;
        public final ForgeConfigSpec.DoubleValue lawOfScaleAttackSpeedPer50PercentSmaller;
        public final ForgeConfigSpec.DoubleValue lawOfScaleAttackDamagePer50PercentLarger;
        public final ForgeConfigSpec.IntValue lawOfScaleTargetTimeoutTicks;

        public final ForgeConfigSpec.DoubleValue feyweightSizeReduction;
        public final ForgeConfigSpec.DoubleValue feyweightMovementSpeed;
        public final ForgeConfigSpec.DoubleValue feyweightAttackSpeed;
        public final ForgeConfigSpec.DoubleValue feyweightDodgeChance;
        public final ForgeConfigSpec.DoubleValue feyweightLargerEnemyDamage;

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

                berserkISSFactorPerLevel = builder
                        .comment("attack actions cooldown reduction factor, per trait level, for ISS boss cooldowns")
                        .defineInRange("berserkISSFactorPerLevel", 0.2, 0, 1.0);

                builder.push("infernal_trait");

                infernalTriggerDistanceBase = builder
                        .comment("distance (blocks) the player must exceed to start charging enrage at level 1, reduced per level by infernalTriggerDistancePerLevel")
                        .defineInRange("infernalTriggerDistanceBase", 60, 1, 512);

                infernalTriggerDistancePerLevel = builder
                        .comment("blocks subtracted from the trigger distance per trait level")
                        .defineInRange("infernalTriggerDistancePerLevel", 10, 0, 256);

                infernalTriggerDurationBaseTicks = builder
                        .comment("base ticks the player must stay past the trigger distance before enrage starts")
                        .defineInRange("infernalTriggerDurationBaseTicks", 20, 1, 72000);

                infernalTriggerDurationPerLevelTicks = builder
                        .comment("extra ticks required per trait level on top of the base duration")
                        .defineInRange("infernalTriggerDurationPerLevelTicks", 0, 0, 72000);

                infernalEnrageDurationTicks = builder
                        .comment("how long enrage lasts once triggered, regardless of trait level")
                        .defineInRange("infernalEnrageDurationTicks", 300, 20, 72000);

                infernalCooldownBaseTicks = builder
                        .comment("cooldown at level 1 after enrage ends before it can trigger again")
                        .defineInRange("infernalCooldownBaseTicks", 200, 0, 72000);

                infernalCooldownPerLevelTicks = builder
                        .comment("ticks subtracted from the cooldown per trait level above 1")
                        .defineInRange("infernalCooldownPerLevelTicks", 50, 0, 72000);

                infernalDamageBonusPerLevel = builder
                        .comment("bonus damage dealt while enraged, per trait level (1.0 = +100% per level)")
                        .defineInRange("infernalDamageBonusPerLevel", 1.0, 0, 10);

                infernalSpeedBonusPerLevel = builder
                        .comment("bonus movement speed while enraged, per trait level (1.0 = +100% per level)")
                        .defineInRange("infernalSpeedBonusPerLevel", 1.0, 0, 10);

                infernalLungeIntervalTicks = builder
                        .comment("ticks between forward lunges toward the target while enraged")
                        .defineInRange("infernalLungeIntervalTicks", 60, 1, 72000);

                infernalLungeStrength = builder
                        .comment("horizontal velocity impulse applied on each lunge")
                        .defineInRange("infernalLungeStrength", 1.5, 0, 20);

                infernalActionbarRadius = builder
                        .comment("radius in blocks in which players see the enrage actionbar warning")
                        .defineInRange("infernalActionbarRadius", 128, 1, 512);

                builder.pop();
                builder.push("devourer_trait");

                devourerDrainPercentPerLevel = builder
                        .comment("percent of a nearby player's current HP drained per interval, per trait level (0.1 = 10% per level)")
                        .defineInRange("devourerDrainPercentPerLevel", 0.1, 0, 1);

                devourerRadiusPerLevel = builder
                        .comment("blocks of range for both HP draining and healing theft, per trait level")
                        .defineInRange("devourerRadiusPerLevel", 10, 1, 128);

                builder.pop();
                builder.push("shredder_trait");

                shredderPercentPerLevel = builder
                        .comment("percent of a nearby player's current armor stolen, per trait level (0.1 = 10% per level)")
                        .defineInRange("shredderPercentPerLevel", 0.1, 0, 1);

                builder.pop();
                builder.push("titan_trait");

                titanSizeBase = builder
                        .comment("size increase at trait level 1 (0.5 = +50%)")
                        .defineInRange("titanSizeBase", 0.5, 0, 10);

                titanSizeStep = builder
                        .comment("additional size increase per level above 1")
                        .defineInRange("titanSizeStep", 0.25, 0, 10);

                titanHealthBase = builder
                        .comment("max health increase at trait level 1 (1.0 = +100%)")
                        .defineInRange("titanHealthBase", 1.0, 0, 20);

                titanHealthStep = builder
                        .comment("additional max health increase per level above 1")
                        .defineInRange("titanHealthStep", 0.5, 0, 20);

                titanKnockbackBase = builder
                        .comment("knockback resistance at trait level 1 (0.5 = 50%, vanilla caps total at 100%)")
                        .defineInRange("titanKnockbackBase", 0.5, 0, 1);

                titanKnockbackStep = builder
                        .comment("additional knockback resistance per level above 1")
                        .defineInRange("titanKnockbackStep", 0.25, 0, 1);

                builder.pop();
                builder.push("dwarf_trait");

                dwarfSizeBase = builder
                        .comment("size decrease at trait level 1 (0.15 = -15%)")
                        .defineInRange("dwarfSizeBase", 0.15, 0, 0.9);

                dwarfSizeStep = builder
                        .comment("additional size decrease per level above 1")
                        .defineInRange("dwarfSizeStep", 0.15, 0, 0.9);

                dwarfSpeedBase = builder
                        .comment("movement speed increase at trait level 1 (0.5 = +50%)")
                        .defineInRange("dwarfSpeedBase", 0.5, 0, 5);

                dwarfSpeedStep = builder
                        .comment("additional movement speed increase per level above 1")
                        .defineInRange("dwarfSpeedStep", 0.5, 0, 5);

                dwarfDodgeBase = builder
                        .comment("dodge chance at trait level 1 (0.1 = 10%)")
                        .defineInRange("dwarfDodgeBase", 0.1, 0, 1);

                dwarfDodgeStep = builder
                        .comment("additional dodge chance per level above 1")
                        .defineInRange("dwarfDodgeStep", 0.1, 0, 1);

                builder.pop();

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
                builder.push("invulnerability_breaker");

                invulnBreakerTargetInvulnReductionTicks = builder
                        .comment("ticks shaved off the hit target's post-hit invulnerability window per hit")
                        .defineInRange("invulnBreakerTargetInvulnReductionTicks", 5, 0, 20);

                invulnBreakerWearerInvulnBonusTicks = builder
                        .comment("extra ticks added to the wearer's own post-hit invulnerability window when hurt")
                        .defineInRange("invulnBreakerWearerInvulnBonusTicks", 5, 0, 20);

                invulnBreakerExtraDifficulty = builder
                        .comment("extra mob difficulty while worn")
                        .defineInRange("invulnBreakerExtraDifficulty", 100, 0, 10000);

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
                builder.push("fury_of_infernal");

                furyInfernalExtraLevel = builder
                        .comment("extra mob difficulty while worn")
                        .defineInRange("furyInfernalExtraLevel", 500, 0, 100000);

                furyInfernalDamagePerTraitLevel = builder
                        .comment("damage bonus per non-legendary trait level on the attacked mob (0.03 = +3% per level)")
                        .defineInRange("furyInfernalDamagePerTraitLevel", 0.03, 0, 10);

                furyInfernalCritDamagePerLegendaryLevel = builder
                        .comment("crit damage attribute bonus per legendary trait level on the current tracked target (0.05 = +5%)")
                        .defineInRange("furyInfernalCritDamagePerLegendaryLevel", 0.05, 0, 10);

                furyInfernalAttackSpeedTraitThreshold = builder
                        .comment("minimum trait count on the tracked target to gain the attack speed bonus")
                        .defineInRange("furyInfernalAttackSpeedTraitThreshold", 10, 1, 100);

                furyInfernalAttackSpeedBase = builder
                        .comment("base attack speed bonus once the threshold is met")
                        .defineInRange("furyInfernalAttackSpeedBase", 0.25, 0, 10);

                furyInfernalAttackSpeedPerBlock = builder
                        .comment("extra attack speed bonus per block of distance to the tracked target")
                        .defineInRange("furyInfernalAttackSpeedPerBlock", 0.05, 0, 10);

                furyInfernalMoveSpeedTraitThreshold = builder
                        .comment("minimum trait count on the tracked target to gain the movement speed bonus")
                        .defineInRange("furyInfernalMoveSpeedTraitThreshold", 20, 1, 100);

                furyInfernalMoveSpeedBase = builder
                        .comment("base movement speed bonus once the threshold is met")
                        .defineInRange("furyInfernalMoveSpeedBase", 0.25, 0, 10);

                furyInfernalMoveSpeedPerBlock = builder
                        .comment("extra movement speed bonus per block of distance to the tracked target")
                        .defineInRange("furyInfernalMoveSpeedPerBlock", 0.05, 0, 10);

                furyInfernalMaxBonusBlocks = builder
                        .comment("distance is capped at this many blocks for the attack/movement speed bonuses")
                        .defineInRange("furyInfernalMaxBonusBlocks", 50, 1, 10000);

                furyInfernalTargetTimeoutTicks = builder
                        .comment("tracked target is forgotten if no hit lands within this many ticks")
                        .defineInRange("furyInfernalTargetTimeoutTicks", 200, 1, 72000);

                builder.pop();
                builder.push("titans_heart");

                titansHeartExtraDifficulty = builder
                        .comment("extra mob difficulty while worn")
                        .defineInRange("titansHeartExtraDifficulty", 300, 0, 100000);

                titansHeartSize = builder
                        .comment("static size increase while worn (0.5 = +50%)")
                        .defineInRange("titansHeartSize", 0.5, 0, 100);

                titansHeartHealth = builder
                        .comment("static max health multiplier while worn (0.5 = +50%)")
                        .defineInRange("titansHeartHealth", 0.5, 0, 100);

                titansHeartArmor = builder
                        .comment("static armor multiplier while worn (0.5 = +50%)")
                        .defineInRange("titansHeartArmor", 0.5, 0, 100);

                titansHeartLegendaryChanceBonus = builder
                        .comment("bonus chance for spawned mobs to roll a legendary trait while worn")
                        .defineInRange("titansHeartLegendaryChanceBonus", 0.5, 0, 1);

                builder.pop();
                builder.push("law_of_scale");

                lawOfScaleExtraDifficulty = builder
                        .comment("extra mob difficulty while worn")
                        .defineInRange("lawOfScaleExtraDifficulty", 500, 0, 100000);

                lawOfScaleExtraTraitCount = builder
                        .comment("extra random traits mobs can spawn with while this curse is worn")
                        .defineInRange("lawOfScaleExtraTraitCount", 5, 0, 100);

                lawOfScaleMaxDamagePercent = builder
                        .comment("max bonus damage dealt to a larger enemy, as a percent of its current health (0.2 = 20%)")
                        .defineInRange("lawOfScaleMaxDamagePercent", 0.2, 0, 1);

                lawOfScaleDamagePercentPerSizeRatio = builder
                        .comment("bonus damage percent gained per full size ratio the enemy is larger than you")
                        .defineInRange("lawOfScaleDamagePercentPerSizeRatio", 0.2, 0, 10);

                lawOfScaleKnockbackBase = builder
                        .comment("base knockback strength applied to smaller enemies")
                        .defineInRange("lawOfScaleKnockbackBase", 0.3, 0, 10);

                lawOfScaleKnockbackPerSizeRatio = builder
                        .comment("extra knockback strength per full size ratio you are larger than the enemy")
                        .defineInRange("lawOfScaleKnockbackPerSizeRatio", 0.3, 0, 10);

                lawOfScaleKnockbackMax = builder
                        .comment("maximum knockback strength regardless of size advantage")
                        .defineInRange("lawOfScaleKnockbackMax", 2.0, 0, 20);

                lawOfScaleAttackSpeedPer50PercentSmaller = builder
                        .comment("attack speed multiplier gained for every 50% smaller you are than your current target (0.1 = +10%)")
                        .defineInRange("lawOfScaleAttackSpeedPer50PercentSmaller", 0.1, 0, 10);

                lawOfScaleAttackDamagePer50PercentLarger = builder
                        .comment("attack damage multiplier gained for every 50% larger you are than your current target (0.1 = +10%)")
                        .defineInRange("lawOfScaleAttackDamagePer50PercentLarger", 0.1, 0, 10);

                lawOfScaleTargetTimeoutTicks = builder
                        .comment("tracked target is forgotten if no hit lands within this many ticks")
                        .defineInRange("lawOfScaleTargetTimeoutTicks", 200, 1, 72000);

                builder.pop();
                builder.push("feyweight");

                feyweightSizeReduction = builder
                        .comment("size reduction while worn (0.5 = -50%)")
                        .defineInRange("feyweightSizeReduction", 0.5, 0, 0.99);

                feyweightMovementSpeed = builder
                        .comment("movement speed multiplier while worn (1.0 = +100%)")
                        .defineInRange("feyweightMovementSpeed", 1.0, 0, 100);

                feyweightAttackSpeed = builder
                        .comment("attack speed multiplier while worn (0.5 = +50%)")
                        .defineInRange("feyweightAttackSpeed", 0.5, 0, 100);

                feyweightDodgeChance = builder
                        .comment("additive dodge chance while worn (0.1 = +10%)")
                        .defineInRange("feyweightDodgeChance", 0.1, 0, 1);

                feyweightLargerEnemyDamage = builder
                        .comment("extra damage received from any larger living attacker (0.2 = +20%)")
                        .defineInRange("feyweightLargerEnemyDamage", 0.2, 0, 100);

                builder.pop();

            }
            builder.pop();
        }

    }

}