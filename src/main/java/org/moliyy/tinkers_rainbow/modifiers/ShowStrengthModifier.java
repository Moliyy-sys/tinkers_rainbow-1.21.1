package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.network.chat.Component;
import org.moliyy.tinkers_rainbow.Config;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ShowStrengthModifier extends Modifier implements MeleeDamageModifierHook {

    public Component getDisplayName(int level) {
        return super.getDisplayName();
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier,
                                ToolAttackContext context, float baseDamage, float damage) {

        // 额外加成 = baseDamage × (倍率 - 1.0)
        // 默认 1.05 → 额外 5% 基础伤害，最终 X + 1.05X = 2.05X
        float multiplier = Config.COMMON.showStrengthBonus.get().floatValue();
        return damage + baseDamage * multiplier;
    }

}
