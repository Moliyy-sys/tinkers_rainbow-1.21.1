package org.moliyy.tinkers_rainbow.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.moliyy.tinkers_rainbow.blocks.RainbowBlocks;
import org.moliyy.tinkers_rainbow.items.RainbowItems;


import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ModLootTableProvider extends LootTableProvider {

    public ModLootTableProvider(PackOutput output,
                                CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Set.of(),
                List.of(new SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)),
                lookupProvider);
    }

    public static class BlockLoot extends BlockLootSubProvider {

        protected BlockLoot(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            add(RainbowBlocks.BLUE_STEEL_ORE.get(),
                    createOreDrop(RainbowBlocks.BLUE_STEEL_ORE.get(), RainbowItems.RAW_BLUE_STEEL.get()));
            add(RainbowBlocks.BLUE_STEEL_DEEPSLATE_ORE.get(),
                    createOreDrop(RainbowBlocks.BLUE_STEEL_DEEPSLATE_ORE.get(), RainbowItems.RAW_BLUE_STEEL.get()));

            dropSelf(RainbowBlocks.BLUE_STEEL_BLOCK.get());
            dropSelf(RainbowBlocks.BLUE_STEEL_DEBRIS.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return RainbowBlocks.BLOCKS.getEntries().stream()
                    .map(holder -> (Block) holder.get())
                    .collect(Collectors.toList());
        }
    }
}