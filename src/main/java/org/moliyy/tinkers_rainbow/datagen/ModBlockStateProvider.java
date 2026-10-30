package org.moliyy.tinkers_rainbow.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import org.moliyy.tinkers_rainbow.blocks.RainbowBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output,ExistingFileHelper exFileHelper) {
        super(output, TinkersRainbow.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(RainbowBlocks.BLUE_STEEL_DEEPSLATE_ORE.get());
        simpleBlock(RainbowBlocks.BLUE_STEEL_BLOCK.get());
        simpleBlock(RainbowBlocks.BLUE_STEEL_ORE.get());

    }
}
