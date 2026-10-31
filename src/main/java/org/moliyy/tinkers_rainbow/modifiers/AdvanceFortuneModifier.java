package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BlockBreakModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class AdvanceFortuneModifier extends Modifier implements EnchantmentModifierHook, BlockBreakModifierHook {
    private static final Random RANDOM = new Random();

    private static final List<FixedDrop> SPECIAL_DROPS = List.of(
            new FixedDrop(Items.NETHERITE_SCRAP, 0.01, 1),
            new FixedDrop(Items.EMERALD,         0.02, 2),
            new FixedDrop(Items.DIAMOND,         0.03, 3),
            new FixedDrop(Items.REDSTONE,        0.10, 4),
            new FixedDrop(Items.AMETHYST_SHARD,  0.12, 5),
            new FixedDrop(Items.COAL,            0.20, 6)
    );

    private static final Map<String, Double> CHANCE_MAP = Map.ofEntries(
            Map.entry("platinum", 0.01),
            Map.entry("osmium",   0.01),
            Map.entry("iridium",  0.01),
            Map.entry("gold",     0.06),
            Map.entry("silver",   0.06),
            Map.entry("iron",     0.16),
            Map.entry("lead",     0.16),
            Map.entry("nickel",   0.16),
            Map.entry("zinc",     0.16),
            Map.entry("tin",      0.16),
            Map.entry("copper",   0.24)
    );

    // 粗矿的数量映射
    private static final Map<String, Integer> NUMBER_MAP = Map.ofEntries(
            Map.entry("platinum", 1),
            Map.entry("osmium",   1),
            Map.entry("iridium",  1),
            Map.entry("gold",     1),
            Map.entry("silver",   1),
            Map.entry("iron",     2),
            Map.entry("lead",     2),
            Map.entry("nickel",   2),
            Map.entry("zinc",     2),
            Map.entry("tin",      2),
            Map.entry("copper",   3)
    );

    private static final double DEFAULT_CHANCE = 0.05;

    private static final int DEFAULT_NUMBER = 1;

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.ENCHANTMENTS, ModifierHooks.BLOCK_BREAK);
    }

    @Override
    public int updateEnchantmentLevel(IToolStackView tool, ModifierEntry modifier,
                                      Holder<Enchantment> enchantment, int level) {
        if (enchantment.is(Enchantments.FORTUNE)) {
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
    public void afterBlockBreak(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context) {
        BlockState state = context.getState();
        if (!state.is(Tags.Blocks.ORES)) return;

        ServerLevel level = context.getWorld();
        if (level == null) return;

        int modifierLevel = modifier.getLevel();
        if (modifierLevel <= 0) return;

        BlockPos pos = context.getPos();

        // 1) 特殊物品
        for (FixedDrop drop : SPECIAL_DROPS) {
            if (RANDOM.nextDouble() <= drop.baseChance() * modifierLevel) {
                int count = drop.baseNumber() * modifierLevel;
                if (count > 0) {
                    Block.popResource(level, pos, new ItemStack(drop.item(), count));
                }
            }
        }

        // 2) 自动收集所有 c:raw_materials/* 标签下的粗矿
        Registry<Item> itemRegistry = level.registryAccess().registryOrThrow(Registries.ITEM);
        itemRegistry.getTags()
                .filter(pair -> pair.getFirst().location().getNamespace().equals("c"))
                .filter(pair -> pair.getFirst().location().getPath().startsWith("raw_materials/"))
                .forEach(pair -> {
                    TagKey<Item> tag = pair.getFirst();
                    String oreType = tag.location().getPath().substring("raw_materials/".length());

                    double chance = CHANCE_MAP.getOrDefault(oreType, DEFAULT_CHANCE) * modifierLevel;
                    if (RANDOM.nextDouble() > chance) return;

                    HolderSet.Named<Item> holders = pair.getSecond();
                    if (holders.size() == 0) return;

                    List<Holder<Item>> items = holders.stream().toList();
                    Holder<Item> chosen = items.get(RANDOM.nextInt(items.size()));

                    int count = NUMBER_MAP.getOrDefault(oreType, DEFAULT_NUMBER) * modifierLevel;
                    if (count > 0) {
                        Block.popResource(level, pos, new ItemStack(chosen.value(), count));
                    }
                });
    }

    private record FixedDrop(Item item, double baseChance, int baseNumber) {}
}