/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  lombok.Generated
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.Vec3i
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.NbtUtils
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityTicker
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.AABB
 *  net.neoforged.neoforge.client.model.data.ModelData
 *  net.neoforged.neoforge.fluids.FluidStack
 *  net.neoforged.neoforge.fluids.capability.IFluidHandler
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.ItemHandlerHelper
 *  slimeknights.mantle.block.entity.IRetexturedBlockEntity
 *  slimeknights.mantle.block.entity.MantleBlockEntity
 *  slimeknights.mantle.block.entity.NameableBlockEntity
 *  slimeknights.mantle.util.BlockEntityHelper
 *  slimeknights.mantle.util.RetexturedHelper
 *  slimeknights.tconstruct.common.network.TinkerNetwork
 *  slimeknights.tconstruct.library.client.model.ModelProperties
 *  slimeknights.tconstruct.smeltery.block.controller.ControllerBlock
 *  slimeknights.tconstruct.smeltery.block.controller.SmelteryControllerBlock
 *  slimeknights.tconstruct.smeltery.block.entity.module.EntityMeltingModule
 *  slimeknights.tconstruct.smeltery.block.entity.module.MultitankFuelModule
 *  slimeknights.tconstruct.smeltery.block.entity.multiblock.HeatingStructureMultiblock
 *  slimeknights.tconstruct.smeltery.block.entity.multiblock.HeatingStructureMultiblock$StructureData
 *  slimeknights.tconstruct.smeltery.block.entity.multiblock.MultiblockResult
 *  slimeknights.tconstruct.smeltery.block.entity.multiblock.MultiblockStructureData
 *  slimeknights.tconstruct.smeltery.block.entity.tank.IDisplayFluidListener
 *  slimeknights.tconstruct.smeltery.block.entity.tank.ISmelteryTankHandler$FluidChange
 *  slimeknights.tconstruct.smeltery.menu.HeatingStructureContainerMenu
 *  slimeknights.tconstruct.smeltery.network.StructureErrorPositionPacket
 *  slimeknights.tconstruct.smeltery.network.StructureUpdatePacket
 */
package slimeknights.tconstruct.smeltery.block.entity.controller;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import slimeknights.mantle.block.entity.IRetexturedBlockEntity;
import slimeknights.mantle.block.entity.MantleBlockEntity;
import slimeknights.mantle.block.entity.NameableBlockEntity;
import slimeknights.mantle.util.BlockEntityHelper;
import slimeknights.mantle.util.RetexturedHelper;
import slimeknights.tconstruct.common.multiblock.IMasterLogic;
import slimeknights.tconstruct.common.multiblock.IServantLogic;
import slimeknights.tconstruct.common.network.TinkerNetwork;
import slimeknights.tconstruct.library.client.model.ModelProperties;
import slimeknights.tconstruct.smeltery.block.controller.ControllerBlock;
import slimeknights.tconstruct.smeltery.block.controller.SmelteryControllerBlock;
import slimeknights.tconstruct.smeltery.block.entity.module.EntityMeltingModule;
import slimeknights.tconstruct.smeltery.block.entity.module.MeltingModuleInventory;
import slimeknights.tconstruct.smeltery.block.entity.module.MultitankFuelModule;
import slimeknights.tconstruct.smeltery.block.entity.multiblock.HeatingStructureMultiblock;
import slimeknights.tconstruct.smeltery.block.entity.multiblock.MultiblockResult;
import slimeknights.tconstruct.smeltery.block.entity.multiblock.MultiblockStructureData;
import slimeknights.tconstruct.smeltery.block.entity.tank.IDisplayFluidListener;
import slimeknights.tconstruct.smeltery.block.entity.tank.ISmelteryTankHandler;
import slimeknights.tconstruct.smeltery.block.entity.tank.SmelteryTank;
import slimeknights.tconstruct.smeltery.menu.HeatingStructureContainerMenu;
import slimeknights.tconstruct.smeltery.network.StructureErrorPositionPacket;
import slimeknights.tconstruct.smeltery.network.StructureUpdatePacket;

public abstract class HeatingStructureBlockEntity
extends NameableBlockEntity
implements IMasterLogic,
ISmelteryTankHandler,
IRetexturedBlockEntity {
    private static final String TAG_STRUCTURE = "multiblock";
    private static final String TAG_TANK = "tank";
    private static final String TAG_INVENTORY = "inventory";
    private static final String TAG_ERROR_POS = "lastError";
    public static final BlockEntityTicker<HeatingStructureBlockEntity> SERVER_TICKER = (level, pos, state, self) -> self.serverTick(level, pos, state);
    public static final BlockEntityTicker<HeatingStructureBlockEntity> CLIENT_TICKER = (level, pos, state, self) -> self.clientTick(level, pos, state);
    private final HeatingStructureMultiblock<?> multiblock = this.createMultiblock();
    @Nullable
    private BlockPos errorPos;
    private int errorVisibleFor = 0;
    private boolean addedFluidListeners = false;
    @Nullable
    protected HeatingStructureMultiblock.StructureData structure;
    protected final SmelteryTank<HeatingStructureBlockEntity> tank = new SmelteryTank<HeatingStructureBlockEntity>(this);
    protected final MeltingModuleInventory meltingInventory = this.createMeltingInventory();
    protected final MultitankFuelModule fuelModule = new MultitankFuelModule((MantleBlockEntity)this, () -> this.structure != null ? this.structure.getTanks() : Collections.emptyList());
    protected int fuelRate = 1;
    protected final EntityMeltingModule entityModule = new EntityMeltingModule((MantleBlockEntity)this, this.tank, this::canMeltEntities, this::insertIntoInventory, () -> this.structure == null ? null : this.structure.getBounds());
    protected int tick = 0;
    private int expandCounter = 0;
    private boolean structureUpdateQueued = false;
    private boolean fluidUpdateQueued = false;
    private AABB defaultBounds;
    @Nonnull
    private Block texture = Blocks.AIR;
    private final List<WeakReference<IDisplayFluidListener>> fluidDisplayListeners = new ArrayList<WeakReference<IDisplayFluidListener>>();
    protected final Consumer<ItemStack> dropItem = this::dropItem;
    private FluidStack displayFluid = FluidStack.EMPTY;

    protected HeatingStructureBlockEntity(BlockEntityType<? extends HeatingStructureBlockEntity> type, BlockPos pos, BlockState state, Component name) {
        super(type, pos, state, name);
    }

    protected abstract HeatingStructureMultiblock<?> createMultiblock();

    protected abstract MeltingModuleInventory createMeltingInventory();

    protected abstract void heat();

    protected void cool() {
        switch (this.tick % 4) {
            case 1: {
                if (this.fuelModule.hasFuel()) break;
                this.meltingInventory.coolItems();
                break;
            }
            case 3: {
                if (!this.fuelModule.hasFuel() || this.fuelRate <= 0) break;
                this.fuelModule.decreaseFuel(this.fuelRate);
            }
        }
    }

    private void updateErrorPos() {
        BlockPos oldErrorPos = this.errorPos;
        this.errorPos = this.multiblock.getLastResult().getPos();
        if (!Objects.equals(oldErrorPos, this.errorPos)) {
            TinkerNetwork.getInstance().sendToClientsAround((Object)new StructureErrorPositionPacket(this.worldPosition, this.errorPos), (LevelAccessor)this.level, this.worldPosition);
        }
    }

    private void updateFluidListeners(HeatingStructureMultiblock.StructureData newStructure) {
        if (this.level != null) {
            FluidStack fluid;
            this.fluidDisplayListeners.clear();
            newStructure.forEachContained(sPos -> {
                BlockEntity patt0$temp;
                if (this.level.hasChunkAt((BlockPos)sPos) && (patt0$temp = this.level.getBlockEntity((BlockPos)sPos)) instanceof IDisplayFluidListener) {
                    IDisplayFluidListener listener = (IDisplayFluidListener)patt0$temp;
                    this.fluidDisplayListeners.add(new WeakReference<IDisplayFluidListener>(listener));
                }
            });
            if (!this.fluidDisplayListeners.isEmpty() && !(fluid = this.tank.getFluidInTank(0)).isEmpty()) {
                this.updateListeners(fluid.copy());
            }
        }
    }

    protected void clientTick(Level level, BlockPos pos, BlockState state) {
        if (this.errorVisibleFor > 0) {
            --this.errorVisibleFor;
        }
        if (!this.addedFluidListeners) {
            this.addedFluidListeners = true;
            if (this.structure != null) {
                this.updateFluidListeners(this.structure);
            }
        }
    }

    protected void serverTick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) {
            if (this.errorVisibleFor > 0) {
                --this.errorVisibleFor;
            }
            return;
        }
        if (!state.hasProperty((Property)ControllerBlock.IN_STRUCTURE)) {
            return;
        }
        if (this.structureUpdateQueued) {
            this.checkStructure();
            this.structureUpdateQueued = false;
        }
        if (this.structure != null && ((Boolean)state.getValue((Property)SmelteryControllerBlock.IN_STRUCTURE)).booleanValue()) {
            if (this.tick == 0) {
                ++this.expandCounter;
                if (this.expandCounter >= 10 && this.structure.getInnerY() < this.multiblock.getMaxHeight()) {
                    this.expandCounter = 0;
                    if (this.multiblock.canExpand(this.structure, level)) {
                        this.updateStructure();
                    } else {
                        this.updateErrorPos();
                    }
                }
            } else if (this.tick % 4 == 0 && !this.multiblock.isInnerBlock(level, this.structure.getNextInsideCheck())) {
                this.updateStructure();
            }
            this.heat();
            if (this.tick % 4 == 3 && this.fluidUpdateQueued) {
                this.fluidUpdateQueued = false;
                this.tank.syncFluids();
            }
        } else {
            if (this.tick == 0) {
                this.updateStructure();
            }
            this.cool();
        }
        this.tick = (this.tick + 1) % 20;
    }

    protected void dropItem(ItemStack stack) {
        assert (this.level != null);
        if (!this.level.isClientSide && !stack.isEmpty()) {
            double x = (double)(this.level.random.nextFloat() * 0.5f) + 0.25;
            double y = (double)(this.level.random.nextFloat() * 0.5f) + 0.25;
            double z = (double)(this.level.random.nextFloat() * 0.5f) + 0.25;
            BlockPos pos = this.worldPosition.relative((Direction)this.getBlockState().getValue((Property)ControllerBlock.FACING));
            ItemEntity itementity = new ItemEntity(this.level, (double)pos.getX() + x, (double)pos.getY() + y, (double)pos.getZ() + z, stack);
            itementity.setDefaultPickUpDelay();
            this.level.addFreshEntity((Entity)itementity);
        }
    }

    public void onLoad() {
        super.onLoad();
        if (this.level != null && !this.level.isClientSide && this.structure != null) {
            this.structure.forEachContained(pos -> {
                BlockEntity patt0$temp = this.level.getBlockEntity((BlockPos)pos);
                if (patt0$temp instanceof IServantLogic) {
                    IServantLogic servant = (IServantLogic)patt0$temp;
                    servant.onMasterLoad(this);
                }
            });
        }
    }

    @Override
    public <T extends BlockEntity> void onServantLoad(T servant) {
        if (this.structure != null && this.structure.getTanks().contains(servant.getBlockPos())) {
            this.fuelModule.ensureTankPresent(servant);
        }
    }

    @Override
    @Nullable
    public IFluidHandler getFluidHandler() {
        return this.structure == null ? null : this.tank;
    }

    public void updateStructure() {
        this.structureUpdateQueued = true;
    }

    protected void setStructure(@Nullable HeatingStructureMultiblock.StructureData structure) {
        this.structure = structure;
        this.fuelModule.clearFluidListeners();
    }

    protected void checkStructure() {
        boolean formed;
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        boolean wasFormed = (Boolean)this.getBlockState().getValue((Property)ControllerBlock.IN_STRUCTURE);
        HeatingStructureMultiblock.StructureData oldStructure = this.structure;
        HeatingStructureMultiblock.StructureData newStructure = this.multiblock.detectMultiblock(this.level, this.worldPosition, (Direction)this.getBlockState().getValue((Property)BlockStateProperties.HORIZONTAL_FACING));
        boolean bl = formed = newStructure != null;
        if (formed != wasFormed) {
            BlockState newState = (BlockState)this.getBlockState().setValue((Property)ControllerBlock.IN_STRUCTURE, (Comparable)Boolean.valueOf(formed));
            if (!formed) {
                newState = (BlockState)newState.setValue((Property)ControllerBlock.ACTIVE, (Comparable)Boolean.valueOf(false));
            }
            this.level.setBlockAndUpdate(this.worldPosition, newState);
        }
        if (formed) {
            TinkerNetwork.getInstance().sendToClientsAround((Object)new StructureUpdatePacket(this.worldPosition, newStructure.getMinPos(), newStructure.getMaxPos(), newStructure.getTanks()), (LevelAccessor)this.level, this.worldPosition);
            newStructure.assignMaster((MantleBlockEntity)this, (MultiblockStructureData)oldStructure);
            this.setStructure(newStructure);
        } else {
            if (oldStructure != null) {
                oldStructure.clearMaster((MantleBlockEntity)this);
            }
            this.setStructure(null);
        }
        this.updateErrorPos();
        this.expandCounter = 0;
    }

    public void invalidateStructure() {
        if (this.structure != null) {
            this.structure.clearMaster((MantleBlockEntity)this);
            this.structure = null;
            this.errorPos = null;
        }
    }

    @Override
    public void notifyChange(BlockPos pos, BlockState state) {
        if (this.structure == null) {
            return;
        }
        assert (this.level != null);
        if (this.multiblock.shouldUpdate(this.level, (MultiblockStructureData)this.structure, pos, state)) {
            this.updateStructure();
        }
    }

    public MultiblockResult getStructureResult() {
        return this.multiblock.getLastResult();
    }

    @Override
    public void updateFluidsFromPacket(List<FluidStack> fluids) {
        this.tank.setFluids(fluids);
    }

    private void updateListeners(FluidStack fluid) {
        Iterator<WeakReference<IDisplayFluidListener>> iterator = this.fluidDisplayListeners.iterator();
        while (iterator.hasNext()) {
            IDisplayFluidListener listener = (IDisplayFluidListener)iterator.next().get();
            if (listener == null) {
                iterator.remove();
                continue;
            }
            listener.notifyDisplayFluidUpdated(fluid);
        }
    }

    @Nonnull
    public ModelData getModelData() {
        return RetexturedHelper.getModelDataBuilder((Block)this.getTexture()).with(ModelProperties.FLUID_STACK, (Object)this.displayFluid).build();
    }

    private void updateDisplayFluid(FluidStack fluid) {
        if (this.level != null && this.level.isClientSide) {
            this.displayFluid = fluid.copy();
            this.requestModelDataUpdate();
            BlockState state = this.getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, 48);
            this.updateListeners(this.displayFluid);
        }
    }

    @Override
    public void notifyFluidsChanged(ISmelteryTankHandler.FluidChange type, FluidStack fluid) {
        if (type == ISmelteryTankHandler.FluidChange.ORDER_CHANGED) {
            this.updateDisplayFluid(fluid);
        }
        this.fluidUpdateQueued = true;
        this.setChangedFast();
    }

    public AABB getRenderBoundingBox() {
        if (this.structure != null) {
            return this.structure.getBounds();
        }
        if (this.defaultBounds == null) {
            this.defaultBounds = new AABB((double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), (double)(this.worldPosition.getX() + 1), (double)(this.worldPosition.getY() + 1), (double)(this.worldPosition.getZ() + 1));
        }
        return this.defaultBounds;
    }

    private boolean canMeltEntities() {
        if (this.fuelModule.hasFuel()) {
            return true;
        }
        return this.fuelModule.findFuel(false) > 0;
    }

    private ItemStack insertIntoInventory(ItemStack stack) {
        return ItemHandlerHelper.insertItem((IItemHandler)this.meltingInventory, (ItemStack)stack, (boolean)false);
    }

    @Nullable
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new HeatingStructureContainerMenu(id, inv, this);
    }

    public void setStructureSize(BlockPos minPos, BlockPos maxPos, List<BlockPos> tanks) {
        this.setStructure(this.multiblock.createClient(minPos, maxPos, tanks));
        this.fuelModule.clearFluidListeners();
        if (this.structure == null) {
            this.fluidDisplayListeners.clear();
        } else {
            this.updateFluidListeners(this.structure);
        }
    }

    public void setErrorPos(@Nullable BlockPos errorPos) {
        this.errorPos = errorPos;
        if (errorPos != null && this.level != null) {
            this.errorVisibleFor = 200;
        }
    }

    public boolean isHighlightError() {
        return this.errorVisibleFor > 0;
    }

    protected abstract boolean isDebugItem(ItemStack var1);

    public boolean showDebugBlockBorder(Player player) {
        return this.isDebugItem(player.getMainHandItem()) || this.isDebugItem(player.getOffhandItem()) || this.isDebugItem(player.getItemBySlot(EquipmentSlot.HEAD));
    }

    public String getTextureName() {
        return RetexturedHelper.getTextureName((Block)this.texture);
    }

    public void updateTexture(String name) {
        Block oldTexture = this.texture;
        this.texture = RetexturedHelper.getBlock((String)name);
        if (oldTexture != this.texture) {
            this.setChangedFast();
            RetexturedHelper.onTextureUpdated((BlockEntity)this);
        }
    }

    protected boolean shouldSyncOnUpdate() {
        return true;
    }

    public void load(CompoundTag nbt, HolderLookup.Provider registries) {
        super.load(nbt, registries);
        if (nbt.contains(TAG_TANK, 10)) {
            this.tank.read(nbt.getCompound(TAG_TANK), registries);
            FluidStack first = this.tank.getFluidInTank(0);
            if (!first.isEmpty()) {
                this.updateDisplayFluid(first);
            }
        }
        if (nbt.contains(TAG_INVENTORY, 10)) {
            this.meltingInventory.readFromTag(nbt.getCompound(TAG_INVENTORY), registries);
        }
        if (nbt.contains(TAG_STRUCTURE, 10)) {
            this.setStructure(this.multiblock.readFromTag(nbt.getCompound(TAG_STRUCTURE), this.worldPosition));
        }
        if (nbt.contains(TAG_ERROR_POS, 11)) {
            this.errorPos = NbtUtils.readBlockPos((CompoundTag)nbt, (String)TAG_ERROR_POS).map(pos -> pos.offset((Vec3i)this.worldPosition)).orElse(null);
        }
        this.fuelModule.readFromTag(nbt);
        if (nbt.contains("texture", 8)) {
            this.texture = RetexturedHelper.getBlock((String)nbt.getString("texture"));
            RetexturedHelper.onTextureUpdated((BlockEntity)this);
        }
    }

    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        if (this.structure != null) {
            compound.put(TAG_STRUCTURE, (Tag)this.structure.writeToTag(this.worldPosition));
        }
        this.fuelModule.writeToTag(compound);
    }

    protected void saveSynced(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveSynced(compound, registries);
        compound.put(TAG_TANK, (Tag)this.tank.write(new CompoundTag(), registries));
        compound.put(TAG_INVENTORY, (Tag)this.meltingInventory.writeToTag(registries));
        if (this.texture != Blocks.AIR) {
            compound.putString("texture", this.getTextureName());
        }
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag nbt = super.getUpdateTag(registries);
        if (this.structure != null) {
            nbt.put(TAG_STRUCTURE, (Tag)this.structure.writeClientTag(this.worldPosition));
        }
        if (this.errorPos != null) {
            nbt.put(TAG_ERROR_POS, NbtUtils.writeBlockPos((BlockPos)this.errorPos.subtract((Vec3i)this.worldPosition)));
        }
        return nbt;
    }

    @Nullable
    public static <HAVE extends HeatingStructureBlockEntity, RET extends BlockEntity> BlockEntityTicker<RET> getTicker(Level level, BlockEntityType<RET> expected, BlockEntityType<HAVE> have) {
        return BlockEntityHelper.castTicker(expected, have, level.isClientSide ? CLIENT_TICKER : SERVER_TICKER);
    }

    @Nullable
    @Generated
    public BlockPos getErrorPos() {
        return this.errorPos;
    }

    @Nullable
    @Generated
    public HeatingStructureMultiblock.StructureData getStructure() {
        return this.structure;
    }

    @Generated
    public SmelteryTank<HeatingStructureBlockEntity> getTank() {
        return this.tank;
    }

    @Generated
    public MeltingModuleInventory getMeltingInventory() {
        return this.meltingInventory;
    }

    @Generated
    public MultitankFuelModule getFuelModule() {
        return this.fuelModule;
    }

    @Nonnull
    @Generated
    public Block getTexture() {
        return this.texture;
    }
}
