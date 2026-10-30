package org.moliyy.tinkers_rainbow.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import org.moliyy.tinkers_rainbow.items.RainbowItems;

public class ModItemModelProvider extends ItemModelProvider{
    public ModItemModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, TinkersRainbow.MODID, exFileHelper);
    }

    @Override
    protected void registerModels() {

        basicItem(RainbowItems.BLUE_STEEL_INGOT.get());

        basicItem(RainbowItems.BLUE_STEEL_NUGGET.get());

        basicItem(RainbowItems.RAW_BLUE_STEEL.get());

        basicItem(RainbowItems.AURORA_CRYSTAL.get());

        basicItem(RainbowItems.BLUE_STEEL_HEART_FRAGMENTS.get());

        basicItem(RainbowItems.BLUE_STEEL_SCRAP.get());

        basicItem(RainbowItems.GENE_METAL.get());

        basicItem(RainbowItems.GENE_METAL_NUGGET.get());

        basicItem(RainbowItems.WAKAMO_INGOT.get());

        basicItem(RainbowItems.STARCORE_INGOT.get());

        basicItem(RainbowItems.HIMARI_INGOT.get());

        basicItem(RainbowItems.NODOKA_INGOT.get());

        basicItem(RainbowItems.SUMIRE_INGOT.get());

        withExistingParent("blue_steel_ore", modLoc("block/blue_steel_ore"));

        withExistingParent("blue_steel_deepslate_ore", modLoc("block/blue_steel_deepslate_ore"));

        withExistingParent("blue_steel_block", modLoc("block/blue_steel_block"));

        withExistingParent("blue_steel_debris", modLoc("block/blue_steel_debris"));

        withExistingParent("blue_steel_crate", modLoc("block/blue_steel_crate"));

    }
}
