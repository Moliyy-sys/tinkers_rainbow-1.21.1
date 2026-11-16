package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class CrimsonBurstModifier extends Modifier implements MeleeHitModifierHook {

    private static final int POINTS_TO_BURST = 5;
    private static final int COOLDOWN_TICKS = 20 * 20; // 20秒

    private static final ResourceLocation KEY_ACCUM =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "crimson_accum");
    private static final ResourceLocation KEY_POINTS =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "crimson_points");

    private static final String KEY_COOLDOWN_UNTIL = "tinkers_rainbow:crimson_cooldown_until";

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier,
                              ToolAttackContext context, float damageDealt) {

        LivingEntity target = context.getLivingTarget();
        LivingEntity attacker = context.getAttacker();

        if (target == null || attacker == null || target.level().isClientSide()) {
            return;
        }

        long currentTick = target.level().getGameTime();
        long cooldownUntil = target.getPersistentData().getLong(KEY_COOLDOWN_UNTIL);

        if (currentTick < cooldownUntil) {
            return;
        }


        float actualDamage = Math.max(0, damageDealt);
        if (actualDamage == 0) {
            actualDamage = context.getBaseDamage();
        }

        float totalDamage = tool.getPersistentData().getFloat(KEY_ACCUM);
        int points = tool.getPersistentData().getInt(KEY_POINTS);

        totalDamage += actualDamage;
        points++;


        if (points >= POINTS_TO_BURST) {

            tool.getPersistentData().putFloat(KEY_ACCUM, 0);
            tool.getPersistentData().putInt(KEY_POINTS, 0);


            if (target.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER,
                        target.getX(), target.getY() + 0.5, target.getZ(),
                        1, 0, 0, 0, 0);
            }
            target.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                    SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0f, 1.0f);


            float explosionDamage = Math.max(totalDamage, context.getBaseDamage());


            target.invulnerableTime = 0;


            if (attacker instanceof Player player) {
                target.hurt(attacker.damageSources().playerAttack(player), explosionDamage);
            } else {
                target.hurt(attacker.damageSources().mobAttack(attacker), explosionDamage);
            }


            target.getPersistentData().putLong(KEY_COOLDOWN_UNTIL, currentTick + COOLDOWN_TICKS);
        } else {

            tool.getPersistentData().putFloat(KEY_ACCUM, totalDamage);
            tool.getPersistentData().putInt(KEY_POINTS, points);
        }
    }
}