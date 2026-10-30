package org.moliyy.tinkers_rainbow.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.moliyy.tinkers_rainbow.TinkersRainbow;

public class RainbowBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TinkersRainbow.MODID);


    public static final DeferredBlock<Block> BLUE_STEEL_ORE = BLOCKS.registerSimpleBlock(
            "blue_steel_ore", BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE).strength(3.0F, 3.0F)
                    .requiresCorrectToolForDrops().sound(SoundType.STONE));


    public static final DeferredBlock<Block> BLUE_STEEL_DEEPSLATE_ORE = BLOCKS.registerSimpleBlock(
            "blue_steel_deepslate_ore", BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F)
                    .requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE));


    public static final DeferredBlock<Block> BLUE_STEEL_BLOCK = BLOCKS.registerSimpleBlock(
            "blue_steel_block", BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL).strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops().sound(SoundType.METAL));


    public static final DeferredBlock<Block> RAW_BLUE_STEEL_BLOCK = BLOCKS.registerSimpleBlock(
            "raw_blue_steel_block", BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE).strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops().sound(SoundType.STONE));


    public static final DeferredBlock<Block> BLUE_STEEL_DEBRIS =
            BLOCKS.register("blue_steel_debris",
                    () -> new Block(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLUE)
                            .strength(30.0F, 1200.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK)));

}
