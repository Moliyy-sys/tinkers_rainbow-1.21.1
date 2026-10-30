package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import org.moliyy.tinkers_rainbow.effects.RainbowEffects;

public class SupportSoundWaveModifier extends Modifier implements MeleeHitModifierHook {

    private static final String COOLDOWN_KEY = "tinkers_rainbow.sound_wave_cooldown";

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier,
                              ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        LivingEntity attacker = context.getAttacker();

        if (target == null || attacker.level().isClientSide) {
            return;
        }

        CompoundTag data = attacker.getPersistentData();
        long lastUse = data.getLong(COOLDOWN_KEY);
        long currentTime = attacker.level().getGameTime();

        if (currentTime - lastUse < 200) {
            return;
        }

        data.putLong(COOLDOWN_KEY, currentTime);

        target.addEffect(new MobEffectInstance(RainbowEffects.SUPPORT_DEBUFF, 400, 0));

        Vec3 attackerPos = attacker.position();
        attacker.level().playSound(null, attackerPos.x, attackerPos.y, attackerPos.z,
                SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 1.0F, 1.0F);

        spawnSonicBoomParticles(attacker, target);
    }

    private void spawnSonicBoomParticles(LivingEntity attacker, LivingEntity target) {
        Vec3 start = attacker.getEyePosition();
        Vec3 direction = target.getEyePosition().subtract(start).normalize();

        for (int i = 1; i <= 15; i++) {
            Vec3 pos = start.add(direction.scale(i));
            ((net.minecraft.server.level.ServerLevel) attacker.level())
                    .sendParticles(ParticleTypes.SONIC_BOOM,
                            pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
        }
    }
}