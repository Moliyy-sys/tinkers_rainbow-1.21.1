/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  lombok.Generated
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.neoforged.neoforge.fluids.FluidStack
 *  net.neoforged.neoforge.fluids.capability.IFluidHandler
 *  slimeknights.mantle.block.entity.MantleBlockEntity
 *  slimeknights.tconstruct.TConstruct
 *  slimeknights.tconstruct.common.TinkerTags$Items
 *  slimeknights.tconstruct.common.config.Config
 *  slimeknights.tconstruct.library.recipe.alloying.IMutableAlloyTank
 *  slimeknights.tconstruct.library.recipe.melting.IMeltingContainer$IOreRate
 *  slimeknights.tconstruct.smeltery.block.controller.ControllerBlock
 *  slimeknights.tconstruct.smeltery.block.entity.controller.HeatingStructureBlockEntity
 *  slimeknights.tconstruct.smeltery.block.entity.module.ByproductMeltingModuleInventory
 *  slimeknights.tconstruct.smeltery.block.entity.module.MeltingModuleInventory
 *  slimeknights.tconstruct.smeltery.block.entity.module.alloying.MultiAlloyingModule
 *  slimeknights.tconstruct.smeltery.block.entity.module.alloying.SmelteryAlloyTank
 *  slimeknights.tconstruct.smeltery.block.entity.multiblock.HeatingStructureMultiblock
 *  slimeknights.tconstruct.smeltery.block.entity.multiblock.HeatingStructureMultiblock$StructureData
 *  slimeknights.tconstruct.smeltery.block.entity.multiblock.SmelteryMultiblock
 *  slimeknights.tconstruct.smeltery.block.entity.tank.ISmelteryTankHandler$FluidChange
 */
package slimeknights.tconstruct.smeltery.block.entity.controller;

import javax.annotation.Nullable;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import slimeknights.mantle.block.entity.MantleBlockEntity;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.recipe.alloying.IMutableAlloyTank;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.block.controller.ControllerBlock;
import slimeknights.tconstruct.smeltery.block.entity.controller.HeatingStructureBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.module.ByproductMeltingModuleInventory;
import slimeknights.tconstruct.smeltery.block.entity.module.MeltingModuleInventory;
import slimeknights.tconstruct.smeltery.block.entity.module.alloying.MultiAlloyingModule;
import slimeknights.tconstruct.smeltery.block.entity.module.alloying.SmelteryAlloyTank;
import slimeknights.tconstruct.smeltery.block.entity.multiblock.HeatingStructureMultiblock;
import slimeknights.tconstruct.smeltery.block.entity.multiblock.SmelteryMultiblock;
import slimeknights.tconstruct.smeltery.block.entity.tank.ISmelteryTankHandler;

public class SmelteryBlockEntity
extends HeatingStructureBlockEntity {
    private static final int CAPACITY_PER_BLOCK = 1080;
    private static final int BLOCKS_PER_FUEL = 15;
    private static final Component NAME = TConstruct.makeTranslation((String)"gui", (String)"smeltery");
    private final SmelteryAlloyTank alloyTank;
    private final MultiAlloyingModule alloyingModule;

    public SmelteryBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType)TinkerSmeltery.smeltery.get(), pos, state, NAME);
        this.alloyTank = new SmelteryAlloyTank(this.tank);
        this.alloyingModule = new MultiAlloyingModule((MantleBlockEntity)this, (IMutableAlloyTank)this.alloyTank);
    }

    protected HeatingStructureMultiblock<?> createMultiblock() {
        return new SmelteryMultiblock(this);
    }

    protected MeltingModuleInventory createMeltingInventory() {
        return new ByproductMeltingModuleInventory((MantleBlockEntity)this, (IFluidHandler)this.tank, (IMeltingContainer.IOreRate)Config.COMMON.smelteryOreRate);
    }

    protected boolean isDebugItem(ItemStack stack) {
        return stack.is(TinkerTags.Items.SMELTERY_DEBUG);
    }

    protected void heat() {
        block13: {
            block12: {
                if (this.structure == null || this.level == null) {
                    return;
                }
                if (!this.structure.hasTanks()) break block12;
                boolean entityMelted = false;
                if (this.tick == 12) {
                    entityMelted = this.entityModule.interactWithEntities();
                }
                switch (this.tick % 4) {
                    case 0: {
                        if (this.fuelModule.hasFuel()) break;
                        if (entityMelted) {
                            this.fuelModule.findFuel(true);
                            break;
                        }
                        int possibleTemp = this.fuelModule.findFuel(false);
                        this.alloyTank.setTemperature(possibleTemp);
                        if (this.meltingInventory.canHeat(possibleTemp) || this.alloyingModule.canAlloy()) {
                            this.fuelModule.findFuel(true);
                            break;
                        }
                        break block13;
                    }
                    case 1: {
                        if (this.fuelModule.hasFuel()) {
                            this.meltingInventory.heatItems(this.fuelModule.getTemperature(), this.fuelModule.getRate());
                            break;
                        }
                        this.meltingInventory.coolItems();
                        break;
                    }
                    case 2: {
                        if (!this.fuelModule.hasFuel()) break;
                        this.alloyTank.setTemperature(this.fuelModule.getTemperature());
                        this.alloyingModule.doAlloy();
                        break;
                    }
                    case 3: {
                        boolean hasFuel = this.fuelModule.hasFuel();
                        BlockState state = this.getBlockState();
                        if ((Boolean)state.getValue((Property)ControllerBlock.ACTIVE) != hasFuel) {
                            this.level.setBlockAndUpdate(this.worldPosition, (BlockState)state.setValue((Property)ControllerBlock.ACTIVE, (Comparable)Boolean.valueOf(hasFuel)));
                        }
                        this.fuelModule.decreaseFuel(this.fuelRate);
                        break;
                    }
                }
                break block13;
            }
            this.cool();
        }
    }

    protected void setStructure(@Nullable HeatingStructureMultiblock.StructureData structure) {
        super.setStructure(structure);
        if (structure != null) {
            int dx = structure.getInnerX();
            int dy = structure.getInnerY();
            int dz = structure.getInnerZ();
            int size = dx * dy * dz;
            this.tank.setCapacity(1080 * size);
            this.meltingInventory.resize(size, this.dropItem);
            this.fuelRate = 1 + (2 * (dx * dy) + 2 * (dy * dz) + dx * dz) / 15;
        }
    }

    public void notifyFluidsChanged(ISmelteryTankHandler.FluidChange type, FluidStack fluid) {
        super.notifyFluidsChanged(type, fluid);
        if (type == ISmelteryTankHandler.FluidChange.ADDED) {
            this.alloyingModule.clearCachedRecipes();
        }
    }

    @Generated
    public MultiAlloyingModule getAlloyingModule() {
        return this.alloyingModule;
    }
}
