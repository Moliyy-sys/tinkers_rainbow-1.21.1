package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class BigSisterModifier extends Modifier implements MeleeHitModifierHook {

    private static final ThreadLocal<Boolean> IS_SECOND_ATTACK = ThreadLocal.withInitial(() -> false);

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (IS_SECOND_ATTACK.get()) return;

        Player attacker = context.getPlayerAttacker();
        LivingEntity target = context.getLivingTarget();
        if (attacker == null || target == null || !target.isAlive()) return;

        ItemStack toolStack = attacker.getMainHandItem();
        if (!(toolStack.getItem() instanceof slimeknights.tconstruct.library.tools.item.IModifiable)) return;

        ToolStack toolStackView = ToolStack.from(toolStack);

        IS_SECOND_ATTACK.set(true);
        try {

            target.invulnerableTime = 0;

            ToolAttackContext extraContext = ToolAttackContext.attacker(attacker)
                    .target(target)
                    .hand(InteractionHand.MAIN_HAND)
                    .cooldown(1.0F)
                    .extraAttack()
                    .applyAttributes()
                    .build();


            ToolAttackUtil.performAttack(toolStackView, extraContext);
        } finally {
            IS_SECOND_ATTACK.set(false);
        }
    }
}