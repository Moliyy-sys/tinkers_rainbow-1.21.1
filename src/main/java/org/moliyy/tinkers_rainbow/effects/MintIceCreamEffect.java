package org.moliyy.tinkers_rainbow.effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class MintIceCreamEffect extends MobEffect {
    public MintIceCreamEffect() {
        super(MobEffectCategory.HARMFUL, 0xA5F2F3);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                ResourceLocation.fromNamespaceAndPath("tinkers_rainbow", "mint_slow"),
                -0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {

        if (entity.tickCount % 40 == 0) {

            entity.hurt(entity.damageSources().magic(), Math.max(1.0F, amplifier));
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}