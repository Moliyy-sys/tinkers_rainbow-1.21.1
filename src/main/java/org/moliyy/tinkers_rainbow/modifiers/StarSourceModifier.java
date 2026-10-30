package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;


public class StarSourceModifier extends Modifier implements MeleeDamageModifierHook, InventoryTickModifierHook {

    private static final int NIGHT_REPAIR_INTERVAL = 20;

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE, ModifierHooks.INVENTORY_TICK);
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier,
                                ToolAttackContext context, float baseDamage, float damage) {
        int level = modifier.getLevel();

        int durabilityCost = level;

        if (tool.isBroken() || tool.getCurrentDurability() < durabilityCost) {
            return damage;
        }

        LivingEntity attacker = context.getAttacker();
        if (attacker == null || attacker.level().isClientSide()) {
            return damage;
        }

        ToolDamageUtil.damage(tool, durabilityCost, attacker, null);

        return damage + (1.2F * level);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier,
                                Level world, LivingEntity holder,
                                int itemSlot, boolean isSelected,boolean isCorrectSlot,
                                @Nullable ItemStack stack) {
        if (world.isClientSide()) {
            return;
        }

        if (!isNight(world)) {
            return;
        }

        if (tool.isBroken() || tool.getDamage() == 0) {
            return;
        }

        if (world.getGameTime() % NIGHT_REPAIR_INTERVAL != 0) {
            return;
        }

        if (tool instanceof ToolStack toolStack) {

            int currentDamage = toolStack.getDamage();

            if (currentDamage > 0) {

                toolStack.setDamage(Math.max(0, currentDamage - 1));

            }
        }
    }

    private static boolean isNight(Level world) {
        long dayTime = world.getDayTime() % 24000L;
        return dayTime >= 13000L && dayTime < 23000L;
    }

}
