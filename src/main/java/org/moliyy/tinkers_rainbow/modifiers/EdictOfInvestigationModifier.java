package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierTraitHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nullable;

public class EdictOfInvestigationModifier extends Modifier implements ModifierTraitHook, ConditionalStatModifierHook, ModifierRemovalHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MODIFIER_TRAITS, ModifierHooks.CONDITIONAL_STAT, ModifierHooks.REMOVE);
    }

    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, TraitBuilder builder, boolean firstEncounter) {
        if (!firstEncounter) return;

        int boost = modifier.getLevel() + 1;
        for (ModifierEntry entry : context.getModifierList()) {
            if (entry.getModifier() == this) continue; // 跳过自己
            int newLevel = entry.getLevel() + boost;
        }
    }

    @Override
    public float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity living, FloatToolStat stat, float baseValue, float multiplier) {
        if (stat == ToolStats.ATTACK_DAMAGE) {
            return baseValue * 1.5F; // +50%
        }
        return baseValue;
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView tool, Modifier modifier) {
        return null;
    }
}