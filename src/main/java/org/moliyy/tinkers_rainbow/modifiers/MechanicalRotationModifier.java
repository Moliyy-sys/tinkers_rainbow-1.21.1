package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.moliyy.tinkers_rainbow.effects.RainbowEffects;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MechanicalRotationModifier extends Modifier implements MeleeHitModifierHook {

    private static final Map<UUID, Long> COOLDOWN_MAP = new ConcurrentHashMap<>();
    private static final int DURATION_TICKS = 60;
    private static final int COOLDOWN_TICKS = 100;

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier,
                              ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        if (target == null || target.level().isClientSide()) {
            return;
        }

        long currentTime = target.level().getGameTime();
        Long cooldownUntil = COOLDOWN_MAP.get(target.getUUID());

        if (cooldownUntil != null && currentTime < cooldownUntil) {
            return;
        }

        target.addEffect(new MobEffectInstance(
                RainbowEffects.MECHANICAL_ROTATION,
                DURATION_TICKS,
                0,
                false,
                false
        ));

        COOLDOWN_MAP.put(target.getUUID(), currentTime + COOLDOWN_TICKS);
    }
}
