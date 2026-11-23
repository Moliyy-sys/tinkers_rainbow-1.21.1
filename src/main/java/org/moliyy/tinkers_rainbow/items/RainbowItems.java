package org.moliyy.tinkers_rainbow.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import org.moliyy.tinkers_rainbow.blocks.RainbowBlocks;
import org.moliyy.tinkers_rainbow.entity.RainbowEntityTypes;

public class RainbowItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(TinkersRainbow.MODID);

    public static final DeferredItem<Item> BLUE_STEEL_INGOT =
            ITEMS.registerSimpleItem("blue_steel");

    public static final DeferredItem<Item> BLUE_STEEL_NUGGET =
            ITEMS.registerSimpleItem("blue_steel_nugget");

    public static final DeferredItem<Item> RAW_BLUE_STEEL =
            ITEMS.registerSimpleItem("raw_blue_steel");

    public static final DeferredItem<Item> AURORA_CRYSTAL =
            ITEMS.registerSimpleItem("aurora_crystal");

    public static final DeferredItem<Item> BLUE_STEEL_SCRAP =
            ITEMS.registerSimpleItem("blue_steel_scrap");

    public static final DeferredItem<Item> GENE_METAL =
            ITEMS.registerSimpleItem("gene_metal");

    public static final DeferredItem<Item> GENE_METAL_NUGGET =
            ITEMS.registerSimpleItem("gene_metal_nugget");

    public static final DeferredItem<Item> STARCORE_INGOT =
            ITEMS.registerSimpleItem("starcore_ingot");

    public static final DeferredItem<Item> PYROXENES =
            ITEMS.registerSimpleItem("pyroxenes");

    public static final DeferredItem<Item> ELIGMA =
            ITEMS.registerSimpleItem("eligma");

    public static final DeferredItem<Item> ORANGE_GEM =
            ITEMS.registerSimpleItem("orange_gem");

    public static final DeferredItem<Item> CHROMATIC_NODE =
            ITEMS.registerSimpleItem("chromatic_node");

    public static final DeferredItem<Item> MARINE_ORB =
            ITEMS.registerSimpleItem("marine_orb");




    public static final DeferredItem<Item> WAKAMO_INGOT =
            ITEMS.registerSimpleItem("wakamo_ingot");

    public static final DeferredItem<Item> HIMARI_INGOT =
            ITEMS.registerSimpleItem("himari_ingot");

    public static final DeferredItem<Item> RIO_INGOT =
            ITEMS.registerSimpleItem("rio_ingot");

    public static final DeferredItem<Item> NODOKA_INGOT =
            ITEMS.registerSimpleItem("nodoka_ingot");

    public static final DeferredItem<Item> SUMIRE_INGOT =
            ITEMS.registerSimpleItem("sumire_ingot");

    public static final DeferredItem<Item> AOBA_INGOT =
            ITEMS.registerSimpleItem("aoba_ingot");

    public static final DeferredItem<Item> HIYORI_INGOT =
            ITEMS.registerSimpleItem("hiyori_ingot");

    public static final DeferredItem<Item> IZUMI_INGOT =
            ITEMS.registerSimpleItem("izumi_ingot");

    public static final DeferredItem<Item> KISAKI_INGOT =
            ITEMS.registerSimpleItem("kisaki_ingot");

    public static final DeferredItem<Item> MINE_INGOT =
            ITEMS.registerSimpleItem("mine_ingot");

    public static final DeferredItem<Item> AIRI_INGOT =
            ITEMS.registerSimpleItem("airi_ingot");

    public static final DeferredItem<Item> KURUMI_INGOT =
            ITEMS.registerSimpleItem("kurumi_ingot");

    public static final DeferredItem<Item> TSUKUYO_INGOT =
            ITEMS.registerSimpleItem("tsukuyo_ingot");

    public static final DeferredItem<Item> JURI_INGOT =
            ITEMS.registerSimpleItem("juri_ingot");

    public static final DeferredItem<BlockItem> BLUE_STEEL_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("blue_steel_ore", RainbowBlocks.BLUE_STEEL_ORE);

    public static final DeferredItem<BlockItem> BLUE_STEEL_DEEPSLATE_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("blue_steel_deepslate_ore", RainbowBlocks.BLUE_STEEL_DEEPSLATE_ORE);

    public static final DeferredItem<BlockItem> BLUE_STEEL_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("blue_steel_block", RainbowBlocks.BLUE_STEEL_BLOCK);


    public static final DeferredItem<BlockItem> BLUE_STEEL_DEBRIS_ITEM =
            ITEMS.registerSimpleBlockItem("blue_steel_debris", RainbowBlocks.BLUE_STEEL_DEBRIS);

    public static final DeferredItem<BlockItem> ELIGMA_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("eligma_ore", RainbowBlocks.ELIGMA_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_ELIGMA_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("deepslate_eligma_ore", RainbowBlocks.DEEPSLATE_ELIGMA_ORE);
    public static final DeferredItem<BlockItem> PYROXENES_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("pyroxenes_ore", RainbowBlocks.PYROXENES_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_PYROXENES_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("deepslate_pyroxenes_ore", RainbowBlocks.DEEPSLATE_PYROXENES_ORE);
    public static final DeferredItem<BlockItem> ORANGE_GEM_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("orange_gem_ore", RainbowBlocks.ORANGE_GEM_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_ORANGE_GEM_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("deepslate_orange_gem_ore", RainbowBlocks.DEEPSLATE_ORANGE_GEM_ORE);
    public static final DeferredItem<BlockItem> CHROMATIC_NODE_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("chromatic_node_ore", RainbowBlocks.CHROMATIC_NODE_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_CHROMATIC_NODE_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("deepslate_chromatic_node_ore", RainbowBlocks.DEEPSLATE_CHROMATIC_NODE_ORE);
    public static final DeferredItem<BlockItem> MARINE_ORB_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("marine_orb_ore", RainbowBlocks.MARINE_ORB_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_MARINE_ORB_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("deepslate_marine_orb_ore", RainbowBlocks.DEEPSLATE_MARINE_ORB_ORE);

    public static final DeferredItem<SpawnEggItem> MUFFIN_SPAWN_EGG =
            ITEMS.register("muffin_spawn_egg",
            () -> new SpawnEggItem(
                    RainbowEntityTypes.MUFFIN.get(), 0x8A2BE2,
                    0x32CD32, new Item.Properties())
    );

}
