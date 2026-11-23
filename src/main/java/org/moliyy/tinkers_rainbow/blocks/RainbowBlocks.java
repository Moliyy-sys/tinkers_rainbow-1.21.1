package org.moliyy.tinkers_rainbow.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.moliyy.tinkers_rainbow.TinkersRainbow;

import java.util.ArrayList;
import java.util.List;

public class RainbowBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(TinkersRainbow.MODID);

    /** All gem ore blocks, used for tags and worldgen registration checks. */
    public static final List<DeferredBlock<Block>> GEM_ORES = new ArrayList<>();


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

    public static final DeferredBlock<Block> BLUE_STEEL_DEBRIS =
            BLOCKS.register("blue_steel_debris",
                    () -> new Block(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLUE)
                            .strength(30.0F, 1200.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK)));

    // ------------------------------------------------------------------
    // gem ores
    // ------------------------------------------------------------------

    public static final DeferredBlock<Block> ELIGMA_ORE = registerOre("eligma_ore", MapColor.COLOR_PURPLE, false);
    public static final DeferredBlock<Block> DEEPSLATE_ELIGMA_ORE = registerOre("deepslate_eligma_ore", MapColor.DEEPSLATE, true);
    public static final DeferredBlock<Block> PYROXENES_ORE = registerOre("pyroxenes_ore", MapColor.COLOR_CYAN, false);
    public static final DeferredBlock<Block> DEEPSLATE_PYROXENES_ORE = registerOre("deepslate_pyroxenes_ore", MapColor.DEEPSLATE, true);
    public static final DeferredBlock<Block> ORANGE_GEM_ORE = registerOre("orange_gem_ore", MapColor.COLOR_ORANGE, false);
    public static final DeferredBlock<Block> DEEPSLATE_ORANGE_GEM_ORE = registerOre("deepslate_orange_gem_ore", MapColor.DEEPSLATE, true);
    public static final DeferredBlock<Block> CHROMATIC_NODE_ORE = registerOre("chromatic_node_ore", MapColor.COLOR_MAGENTA, false);
    public static final DeferredBlock<Block> DEEPSLATE_CHROMATIC_NODE_ORE = registerOre("deepslate_chromatic_node_ore", MapColor.DEEPSLATE, true);
    public static final DeferredBlock<Block> MARINE_ORB_ORE = registerOre("marine_orb_ore", MapColor.COLOR_BLUE, false);
    public static final DeferredBlock<Block> DEEPSLATE_MARINE_ORB_ORE = registerOre("deepslate_marine_orb_ore", MapColor.DEEPSLATE, true);

    private static DeferredBlock<Block> registerOre(String name, MapColor mapColor, boolean deepslate) {
        DeferredBlock<Block> block = BLOCKS.registerSimpleBlock(name, BlockBehaviour.Properties.of()
                .mapColor(mapColor)
                .strength(deepslate ? 4.5F : 3.0F, 3.0F)
                .requiresCorrectToolForDrops()
                .sound(deepslate ? SoundType.DEEPSLATE : SoundType.STONE));
        GEM_ORES.add(block);
        return block;
    }

}
