package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.moliyy.tinkers_rainbow.effects.RainbowEffects;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class AuroraImpactModifier extends Modifier
        implements MeleeDamageModifierHook, MeleeHitModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE, ModifierHooks.MELEE_HIT);
    }

    // ---------- 额外伤害 ----------

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier,
                                ToolAttackContext context, float baseDamage, float damage) {
        // 额外伤害 = 20% + (词条等级 × 10%)
        float bonusRatio = 0.2F + (modifier.getLevel() * 0.1F);
        return damage * (1.0F + bonusRatio);
    }

    // ---------- 眩晕 ----------

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier,
                              ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        if (target == null || target.level().isClientSide()) {
            return;
        }

        // 1.5 秒 = 30 tick；amplifier=0，隐藏粒子和图标
        target.addEffect(new MobEffectInstance(
                RainbowEffects.AURORA_STUN,
                30,     // duration
                0,      // amplifier
                false,  // ambient
                false,  // visible（不显示粒子）
                false   // showIcon（不显示图标）
        ));
    }
}