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

            builder.push("onsenCooldown");
            onsenCooldown = builder.comment("Cooldown in ticks (40 = 2s)").defineInRange("cooldown", 40, 0, 72000);
            builder.pop();

            builder.pop();

            builder.comment("Cooldown Bar Settings").push("cooldown_bar");
            cooldownBarX = builder.comment("The X position of the cooldown bar on screen").defineInRange("cooldownBarX", -1, -1, 10000);
            cooldownBarY = builder.comment("The Y position of the cooldown bar on screen").defineInRange("cooldownBarY", -1, -1, 10000);
            builder.pop();
        }

        private static boolean validateItemName(final Object obj) {
            return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
        }
    }
}