package org.moliyy.tinkers_rainbow.modifiers;

import net.neoforged.bus.api.IEventBus;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;


public class RainbowModifiers {

    public static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(TinkersRainbow.MODID);

    public static StaticModifier<Modifier> advance_Looting =
            MODIFIERS.register("advance_looting", AdvanceLootingModifier::new);

    public static StaticModifier<Modifier> advance_fortune =
            MODIFIERS.register("advance_fortune", AdvanceFortuneModifier::new);

    public static StaticModifier<Modifier> crimson_burst =
            MODIFIERS.register("crimson_burst", CrimsonBurstModifier::new);

    public static StaticModifier<Modifier> aurora_impact =
            MODIFIERS.register("aurora_impact", AuroraImpactModifier::new);

    public static StaticModifier<Modifier> aurora_protection =
            MODIFIERS.register("aurora_protection", AuroraProtectionModifier::new);

    public static StaticModifier<Modifier> star_source =
            MODIFIERS.register("star_source", StarSourceModifier::new);

    public static StaticModifier<Modifier> gene_boost =
            MODIFIERS.register("gene_boost", GeneBoostModifier::new);

    public static StaticModifier<Modifier> show_strength =
            MODIFIERS.register("show_strength", ShowStrengthModifier::new);

    public static StaticModifier<Modifier> dash_shoot =
            MODIFIERS.register("dash_shoot", DashShootModifier::new);

    public static StaticModifier<Modifier> onsen =
            MODIFIERS.register("onsen", OnsenModifier::new);

    public static final StaticModifier<MintIceCreamModifier> mint_ice_cream =
            MODIFIERS.register("mint_ice_cream", MintIceCreamModifier::new);

    public static final StaticModifier<HonorAndFaithModifier> honor_and_faith =
            MODIFIERS.register("honor_and_faith", HonorAndFaithModifier::new);

    public static final StaticModifier<RepairCartModifier> repair_cart =
            MODIFIERS.register("repair_cart", RepairCartModifier::new);

    public static final StaticModifier<SupportSoundWaveModifier> support_sound_wave =
            MODIFIERS.register("support_sound_wave", SupportSoundWaveModifier::new);

    public static final StaticModifier<CheesyBurgerModifier> cheesy_burger =
            MODIFIERS.register("cheesy_burger", CheesyBurgerModifier::new);

    public static final StaticModifier<EdictOfInvestigationModifier> edict_of_investigation =
            MODIFIERS.register("edict_of_investigation", EdictOfInvestigationModifier::new);

    public static final StaticModifier<MoonVeilModifier> moon_veil =
            MODIFIERS.register("moon_veil", MoonVeilModifier::new);

    public static final StaticModifier<TacticalShieldModifier> tactical_shield =
            MODIFIERS.register("tactical_shield", TacticalShieldModifier::new);

    public RainbowModifiers() {
    }

    public static void init(IEventBus modEventBus) {
        MODIFIERS.register(modEventBus);
    }
}
