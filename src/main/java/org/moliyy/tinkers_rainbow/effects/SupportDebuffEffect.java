package org.moliyy.tinkers_rainbow.effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SupportDebuffEffect extends MobEffect {
    public SupportDebuffEffect() {
        super(MobEffectCategory.HARMFUL, 0x99CCFF);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE,
                ResourceLocation.fromNamespaceAndPath("tinkers_rainbow", "sound_wave_atk_down"),
                -0.3D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ARMOR,
                ResourceLocation.fromNamespaceAndPath("tinkers_rainbow", "sound_wave_armor_down"),
                -0.3D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}