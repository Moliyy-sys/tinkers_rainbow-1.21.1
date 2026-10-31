package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.LongTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import org.moliyy.tinkers_rainbow.effects.RainbowEffects;

import java.util.List;

public class SupportSoundWaveModifier extends Modifier implements MeleeHitModifierHook {

    private static final ResourceLocation COOLDOWN_KEY = ResourceLocation.fromNamespaceAndPath("tinkers_rainbow", "sound_wave_cooldown");

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

        long lastUse = tool.getPersistentData().get(COOLDOWN_KEY, (tag, key) -> tag.getLong(key));
        long currentTime = attacker.level().getGameTime();

        if (currentTime - lastUse < 200) { // 10秒冷却
            return;
        }

        tool.getPersistentData().put(COOLDOWN_KEY, LongTag.valueOf(currentTime));

        Vec3 start = attacker.getEyePosition();
        Vec3 look = attacker.getLookAngle();
        Vec3 end = start.add(look.scale(15.0));

        AABB searchBox = new AABB(start, end).inflate(1.0);
        List<LivingEntity> hitEntities = attacker.level().getEntitiesOfClass(LivingEntity.class, searchBox,
                e -> e != attacker && e.isAlive() && e.getBoundingBox().inflate(0.5).clip(start, end).isPresent());

        float waveDamage = (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.5F;

        for (LivingEntity entity : hitEntities) {
            entity.addEffect(new MobEffectInstance(RainbowEffects.SUPPORT_DEBUFF, 400, 0));

            entity.invulnerableTime = 0;

            entity.hurt(attacker.damageSources().indirectMagic(attacker, attacker), waveDamage);
        }

        attacker.level().playSound(null, start.x, start.y, start.z,
                SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 1.0F, 1.0F);

        spawnSonicBoomParticles(attacker.level(), start, look);
    }

    private void spawnSonicBoomParticles(net.minecraft.world.level.Level level, Vec3 start, Vec3 look) {
        for (int i = 1; i <= 15; i++) {
            Vec3 pos = start.add(look.scale(i));
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
            }
        }
    }
}