package org.moliyy.tinkers_rainbow;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class Config {

    public static final ModConfigSpec SPEC;

    public static final Common COMMON;

    static {
        Pair<Common, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Common::new);

        COMMON = pair.getLeft();

        SPEC = pair.getRight();
    }

    public static class Common {

        public final ModConfigSpec.IntValue cooldownBarX;

        public final ModConfigSpec.IntValue cooldownBarY;

        public final ModConfigSpec.IntValue shieldBarX;

        public final ModConfigSpec.IntValue shieldBarY;


        public final ModConfigSpec.BooleanValue logDirtBlock;

        public final ModConfigSpec.IntValue magicNumber;

        public final ModConfigSpec.ConfigValue<String> magicNumberIntroduction;

        public final ModConfigSpec.ConfigValue<List<? extends String>> itemStrings;

        public final ModConfigSpec.DoubleValue showStrengthBonus;


        public final ModConfigSpec.IntValue repairCartCooldown;
        public final ModConfigSpec.IntValue honorAndFaithCooldown;
        public final ModConfigSpec.IntValue supportSoundWaveCooldown;
        public final ModConfigSpec.DoubleValue repairCartDamageMultiplier;
        public final ModConfigSpec.DoubleValue honorAndFaithDamageMultiplier;
        public final ModConfigSpec.DoubleValue supportSoundWaveDamageMultiplier;
        public final ModConfigSpec.DoubleValue edictAttackMultiplier;
        public final ModConfigSpec.IntValue cheesyBurgerGenCooldown;
        public final ModConfigSpec.IntValue cheesyBurgerEatCooldown;
        public final ModConfigSpec.IntValue cheesyBurgerHealPercentage;
        public final ModConfigSpec.IntValue dashShootCooldown;
        public final ModConfigSpec.DoubleValue dashShootDamageMultiplier;
        public final ModConfigSpec.IntValue onsenCooldown;
        public final ModConfigSpec.IntValue moonVeilDuration;
        public final ModConfigSpec.IntValue moonVeilCooldown;
        public final ModConfigSpec.DoubleValue moonVeilHealPercentage;
        public final ModConfigSpec.DoubleValue shieldHealthMultiplier;
        public final ModConfigSpec.IntValue shieldCooldown;
        public final ModConfigSpec.DoubleValue tacticalDamageReduction;
        public final ModConfigSpec.DoubleValue tacticalReflectPercentage;

        public final ModConfigSpec.IntValue strongSwordKillsPerGroup;
        public final ModConfigSpec.DoubleValue strongSwordDamagePerGroup;
        public final ModConfigSpec.DoubleValue strongSwordMaxDamage;
        Common(ModConfigSpec.Builder builder) {

            logDirtBlock = builder.comment("Whether to log the dirt block on common setup").define("logDirtBlock", true);
            magicNumber = builder.comment("A magic number").defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);
            magicNumberIntroduction = builder.comment("What you want the introduction message to be for the magic number").define("magicNumberIntroduction", "The magic number is... ");
            itemStrings = builder.comment("A list of items to log on common setup.").defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), () -> "", Common::validateItemName);
            showStrengthBonus = builder.comment("Attack bonus multiplier for 'Show My Strength' (1.05 = +5%)").defineInRange("show_strength_bonus", 1.05, 1.0, 10.0);


            builder.comment("Tinkers Rainbow Modifier Settings").push("modifiers");

            builder.push("cheesy_burger");
            cheesyBurgerGenCooldown = builder.comment("Cooldown for generating food in ticks (100 = 5s)").defineInRange("gen_cooldown", 100, 0, 72000);
            cheesyBurgerEatCooldown = builder.comment("Cooldown for auto-eating food in ticks (100 = 5s)").defineInRange("eat_cooldown", 100, 0, 72000);
            cheesyBurgerHealPercentage = builder.comment("Heal percentage when eating (20 = 20% of max health)").defineInRange("heal_percentage", 20, 0, 100);
            builder.pop();

            builder.push("repair_cart");
            repairCartCooldown = builder.comment("Cooldown in ticks (100 = 5s)").defineInRange("cooldown", 100, 0, 72000);
            repairCartDamageMultiplier = builder.comment("Damage multiplier based on player attack damage").defineInRange("damage_multiplier", 3.0, 0.0, 100.0);
            builder.pop();

            builder.push("honor_and_faith");
            honorAndFaithCooldown = builder.comment("Cooldown in ticks (160 = 8s)").defineInRange("cooldown", 160, 0, 72000);
            honorAndFaithDamageMultiplier = builder.comment("Damage multiplier based on player attack damage").defineInRange("damage_multiplier", 4.0, 0.0, 100.0);
            builder.pop();

            builder.push("support_sound_wave");
            supportSoundWaveCooldown = builder.comment("Cooldown in ticks (200 = 10s)").defineInRange("cooldown", 200, 0, 72000);
            supportSoundWaveDamageMultiplier = builder.comment("Damage multiplier based on player attack damage").defineInRange("damage_multiplier", 1.5, 0.0, 100.0);
            builder.pop();

            builder.push("edict_of_investigation");
            edictAttackMultiplier = builder.comment("Attack damage multiplier (1.5 = +50%)").defineInRange("attack_multiplier", 1.5, 1.0, 10.0);
            builder.pop();

            builder.push("dash_shoot");
            dashShootCooldown = builder.comment("Cooldown in ticks (200 = 10s)").defineInRange("cooldown", 200, 0, 72000);
            dashShootDamageMultiplier = builder.comment("Damage multiplier based on player attack damage").defineInRange("damage_multiplier", 3.0, 0.0, 100.0);
            builder.pop();

            builder.push("onsen");
            onsenCooldown = builder.comment("Cooldown in ticks (40 = 2s)").defineInRange("cooldown", 40, 0, 72000);
            builder.pop();

            builder.push("moon_veil");
            moonVeilDuration = builder.comment("Duration of the invincibility and healing state in ticks (200 = 10s)").defineInRange("duration", 200, 0, 72000);
            moonVeilCooldown = builder.comment("Cooldown in ticks (600 = 30s)").defineInRange("cooldown", 600, 0, 72000);
            moonVeilHealPercentage = builder.comment("Percentage of incoming damage converted to healing (0.2 = 20%)").defineInRange("heal_percentage", 0.2, 0.0, 10.0);
            builder.pop();

            builder.push("tactical_shield");
            shieldHealthMultiplier = builder.comment("Max shield = Max Health * this multiplier (4.3 = 430%)").defineInRange("health_multiplier", 4.3, 1.0, 100.0);
            shieldCooldown = builder.comment("Cooldown after shield breaks in ticks (2400 = 120s)").defineInRange("cooldown", 2400, 0, 72000);
            tacticalDamageReduction = builder.comment("Damage reduction while shield is active (0.4 = 40% reduction)").defineInRange("damage_reduction", 0.4, 0.0, 1.0);
            tacticalReflectPercentage = builder.comment("Reflect damage percentage based on max shield (0.1 = 10%)").defineInRange("reflect_percentage", 0.1, 0.0, 10.0);
            builder.pop();

            builder.pop();


            builder.comment("Cooldown Bar Settings").push("cooldown_bar");
            cooldownBarX = builder.comment("The X position of the cooldown bar on screen").defineInRange("cooldownBarX", -1, -1, 10000);
            cooldownBarY = builder.comment("The Y position of the cooldown bar on screen").defineInRange("cooldownBarY", -1, -1, 10000);
            builder.pop();

            builder.comment("Shield Bar Settings").push("shield_bar");
            shieldBarX = builder.comment("The X position of the shield bar on screen").defineInRange("shieldBarX", -1, -1, 10000);
            shieldBarY = builder.comment("The Y position of the shield bar on screen").defineInRange("shieldBarY", -1, -1, 10000);
            builder.pop();

            builder.push("strong_sword");
            strongSwordKillsPerGroup = builder.comment("Number of kills required to increase damage").defineInRange("kills_per_group", 10, 1, 10000);
            strongSwordDamagePerGroup = builder.comment("Damage increase per group of kills").defineInRange("damage_per_group", 0.1, 0.0, 100.0);
            strongSwordMaxDamage = builder.comment("Maximum damage bonus from this modifier").defineInRange("max_damage", 10.0, 0.0, 1000.0);
            builder.pop();
        }

        private static boolean validateItemName(final Object obj) {
            return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
        }
    }
}