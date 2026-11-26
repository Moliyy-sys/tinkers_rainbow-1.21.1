package org.moliyy.tinkers_rainbow.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class TideDrowningEffect extends MobEffect {

    public TideDrowningEffect() {
        super(MobEffectCategory.HARMFUL, 0x1E90FF); // 海蓝色
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.tickCount % 20 == 0) {
            entity.hurt(entity.damageSources().drown(), 1.0F);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}