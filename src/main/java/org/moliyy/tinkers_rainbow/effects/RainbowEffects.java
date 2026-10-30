package org.moliyy.tinkers_rainbow.effects;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.moliyy.tinkers_rainbow.TinkersRainbow;

public class RainbowEffects {

    private RainbowEffects() {}

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, TinkersRainbow.MODID);


    public static final Holder<MobEffect> AURORA_STUN = MOB_EFFECTS.register("aurora_stun",
            () -> new AuroraStunEffect()
                    .addAttributeModifier(
                            Attributes.MOVEMENT_SPEED,
                            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "aurora_stun"),
                            -1.0,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
    );

    public static final Holder<MobEffect> MINT_ICE_CREAM =
            MOB_EFFECTS.register("mint_ice_cream", MintIceCreamEffect::new);


    public static final Holder<MobEffect> SUPPORT_DEBUFF =
            MOB_EFFECTS.register("support_debuff", SupportDebuffEffect::new);
}
