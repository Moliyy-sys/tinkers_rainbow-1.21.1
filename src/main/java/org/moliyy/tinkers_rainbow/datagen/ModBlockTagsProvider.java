package org.moliyy.tinkers_rainbow.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import org.moliyy.tinkers_rainbow.TinkersRainbow;
import org.moliyy.tinkers_rainbow.blocks.RainbowBlocks;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput output,
                                CompletableFuture<HolderLookup.Provider> lookupProvider,
                                @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, TinkersRainbow.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(RainbowBlocks.BLUE_STEEL_ORE.get())
                .add(RainbowBlocks.BLUE_STEEL_DEEPSLATE_ORE.get())
                .add(RainbowBlocks.BLUE_STEEL_BLOCK.get())
                .add(RainbowBlocks.RAW_BLUE_STEEL_BLOCK.get())
                .add(RainbowBlocks.BLUE_STEEL_DEBRIS.get());

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(RainbowBlocks.BLUE_STEEL_CRATE.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(RainbowBlocks.BLUE_STEEL_ORE.get())
                .add(RainbowBlocks.BLUE_STEEL_DEEPSLATE_ORE.get())
                .add(RainbowBlocks.BLUE_STEEL_BLOCK.get())
                .add(RainbowBlocks.RAW_BLUE_STEEL_BLOCK.get());

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(RainbowBlocks.BLUE_STEEL_DEBRIS.get());

        tag(Tags.Blocks.ORES)
                .add(RainbowBlocks.BLUE_STEEL_ORE.get())
                .add(RainbowBlocks.BLUE_STEEL_DEEPSLATE_ORE.get());

        tag(Tags.Blocks.STORAGE_BLOCKS)
                .add(RainbowBlocks.BLUE_STEEL_BLOCK.get())
                .add(RainbowBlocks.RAW_BLUE_STEEL_BLOCK.get());
    }
}