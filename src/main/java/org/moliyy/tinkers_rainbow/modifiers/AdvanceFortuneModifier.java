package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BlockHarvestModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class AdvanceFortuneModifier extends Modifier implements EnchantmentModifierHook, BlockHarvestModifierHook {
    private static final Random RANDOM = new Random();

    private static final List<OreDrop> EXTRA_DROPS = List.of(
            new OreDrop(Items.NETHERITE_SCRAP, 0.005),  // 0.5%
            new OreDrop(Items.EMERALD, 0.01),           // 1%
            new OreDrop(Items.DIAMOND, 0.015),          // 1.5%
            new OreDrop(Items.RAW_GOLD, 0.03),          // 3%
            new OreDrop(Items.REDSTONE, 0.05),          // 5%
            new OreDrop(Items.AMETHYST_SHARD, 0.06),    // 6%
            new OreDrop(Items.RAW_IRON, 0.08),          // 8%
            new OreDrop(Items.COAL, 0.10),              // 10%
            new OreDrop(Items.RAW_COPPER, 0.12)         // 12%
    );

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookbuilder) {
        hookbuilder.addHook(this, ModifierHooks.ENCHANTMENTS, ModifierHooks.BLOCK_HARVEST);

    }


    @Override
    public int updateEnchantmentLevel(IToolStackView tool, ModifierEntry modifier,
                                      Holder<Enchantment> enchantment, int level) {
        if (enchantment.is(Enchantments.FORTUNE)) {
            // 词条等级 = 额外时运等级
            return level + modifier.getLevel();
        }
        return level;
    }

    @Override
    public void updateEnchantments(IToolStackView tool, ModifierEntry modifier,
                                   HolderLookup.RegistryLookup<Enchantment> lookup,
                                   Map<Holder<Enchantment>, Integer> map) {
        Holder<Enchantment> fortune = lookup.getOrThrow(Enchantments.FORTUNE);
        map.merge(fortune, modifier.getLevel(), Integer::sum);
    }

    @Override
    public void finishHarvest(IToolStackView tool, ModifierEntry modifier,
                              ToolHarvestContext context, int harvested) {
        // 没有破坏任何方块，直接返回
        if (harvested <= 0) {
            return;
        }

        // 核心判定：只有方块在 c:ores 标签中才生效

        if (!context.getState().is(Tags.Blocks.ORES)) {
            return;
        }

        ServerLevel level = context.getWorld();
        if (level == null) {
            return;
        }

        int modifierLevel = modifier.getLevel();
        for (OreDrop drop : EXTRA_DROPS) {
            // 概率 = 基础概率 × 词条等级，等级越高越容易触发
            if (RANDOM.nextDouble() < drop.baseChance() * modifierLevel) {
                Block.popResource(level, context.getPos(), new ItemStack(drop.item()));
            }
        }
    }

    private record OreDrop(Item item, double baseChance) {}

}
