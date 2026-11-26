package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.moliyy.tinkers_rainbow.effects.RainbowEffects;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class TideModifier extends Modifier implements MeleeHitModifierHook {

    private static final int DROWNING_DURATION = 200;

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

        target.hurt(target.damageSources().drown(), 1.0F);

        if (context.getAttacker() instanceof Player player && player.isInWater()) {
            target.addEffect(new MobEffectInstance(
                    RainbowEffects.TIDE_DROWNING,
                    DROWNING_DURATION,
                    0,
                    false,
                    false
            ));
        }
    }
}
