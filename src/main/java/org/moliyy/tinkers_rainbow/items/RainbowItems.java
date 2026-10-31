package org.moliyy.tinkers_rainbow.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import org.moliyy.tinkers_rainbow.blocks.RainbowBlocks;

public class RainbowItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TinkersRainbow.MODID);

    public static final DeferredItem<Item> BLUE_STEEL_INGOT =
            ITEMS.registerSimpleItem("blue_steel");

    public static final DeferredItem<Item> BLUE_STEEL_NUGGET =
            ITEMS.registerSimpleItem("blue_steel_nugget");

    public static final DeferredItem<Item> RAW_BLUE_STEEL =
            ITEMS.registerSimpleItem("raw_blue_steel");

    public static final DeferredItem<Item> AURORA_CRYSTAL =
            ITEMS.registerSimpleItem("aurora_crystal");

    public static final DeferredItem<Item> BLUE_STEEL_HEART_FRAGMENTS =
            ITEMS.registerSimpleItem("blue_steel_heart_fragments");

    public static final DeferredItem<Item> BLUE_STEEL_SCRAP =
            ITEMS.registerSimpleItem("blue_steel_scrap");

    public static final DeferredItem<Item> GENE_METAL =
            ITEMS.registerSimpleItem("gene_metal");

    public static final DeferredItem<Item> GENE_METAL_NUGGET =
            ITEMS.registerSimpleItem("gene_metal_nugget");

    public static final DeferredItem<Item> WAKAMO_INGOT =
            ITEMS.registerSimpleItem("wakamo_ingot");

    public static final DeferredItem<Item> STARCORE_INGOT =
            ITEMS.registerSimpleItem("starcore_ingot");

    public static final DeferredItem<Item> HIMARI_INGOT =
            ITEMS.registerSimpleItem("himari_ingot");

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

    public static final DeferredItem<BlockItem> BLUE_STEEL_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("blue_steel_ore", RainbowBlocks.BLUE_STEEL_ORE);

    public static final DeferredItem<BlockItem> BLUE_STEEL_DEEPSLATE_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("blue_steel_deepslate_ore", RainbowBlocks.BLUE_STEEL_DEEPSLATE_ORE);

    public static final DeferredItem<BlockItem> BLUE_STEEL_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("blue_steel_block", RainbowBlocks.BLUE_STEEL_BLOCK);


    public static final DeferredItem<BlockItem> BLUE_STEEL_DEBRIS_ITEM =
            ITEMS.registerSimpleBlockItem("blue_steel_debris", RainbowBlocks.BLUE_STEEL_DEBRIS);

}
