package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.mining.BlockHarvestModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

public class DirtScumModifier extends Modifier implements BlockHarvestModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.BLOCK_HARVEST);
    }

    @Override
    public void finishHarvest(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context, int harvested) {
        if (harvested <= 0) return;

        var state = context.getState();
        if (state.is(Blocks.CLAY) || state.is(Blocks.GRAVEL) || state.is(Blocks.SAND)) {
            net.minecraft.world.level.block.Block.popResource(context.getWorld(), context.getPos(),
                    new ItemStack(TinkerSmeltery.searedBrick));
        }
    }
}