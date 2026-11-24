package org.moliyy.tinkers_rainbow.integration.ae2;

import java.util.EnumSet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import org.jetbrains.annotations.Nullable;

import appeng.api.config.Actionable;
import appeng.api.networking.GridHelper;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridNodeListener;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.IInWorldGridNodeHost;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.MEStorage;
import slimeknights.tconstruct.smeltery.block.entity.controller.HeatingStructureBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.module.MeltingModuleInventory;
import slimeknights.tconstruct.smeltery.block.entity.tank.SmelteryTank;

import org.moliyy.tinkers_rainbow.smeltery.AbstractSearedOutputBlockEntity;
import org.moliyy.tinkers_rainbow.smeltery.OutputMode;
import org.moliyy.tinkers_rainbow.smeltery.OutputSink;

/**
 * Block entity of the seared pattern interface.
 *
 * <p>Pure I/O bridge between the ME network and a smeltery. All transfer logic (output mode, transfer
 * mode, numeric limit and the instant casting) lives in {@link AbstractSearedOutputBlockEntity}; this
 * class only provides the ME network sink, the cast slot and the item/fluid input capabilities.</p>
 */
public class SearedPatternInterfaceBlockEntity extends AbstractSearedOutputBlockEntity
        implements IInWorldGridNodeHost, IActionHost {

    /** Only the cast slot is stored by this block. */
    public static final int SLOT_CAST = 0;
    public static final int SLOT_COUNT = 1;

    private static final String TAG_INVENTORY = "inventory";

    private final ItemStackHandler inventory = new ItemStackHandler(SLOT_COUNT) {
        @Override
        protected void onContentsChanged(int slot) {
            SearedPatternInterfaceBlockEntity.this.setChanged();
        }
    };

    private final IManagedGridNode mainNode = GridHelper.createManagedNode(this, NodeListener.INSTANCE)
            .setVisualRepresentation(new ItemStack(Ae2Integration.SEARED_PATTERN_INTERFACE.get()))
            .setInWorldNode(true)
            .setTagName("node")
            .setExposedOnSides(EnumSet.allOf(Direction.class))
            .setIdlePowerUsage(0.5);

    @Nullable
    private IActionSource actionSource;

    private boolean online = false;

    public SearedPatternInterfaceBlockEntity(BlockPos pos, BlockState state) {
        super(Ae2Integration.SEARED_PATTERN_INTERFACE_BLOCK_ENTITY.get(), pos, state);
    }

    // ------------------------------------------------------------------
    // Lifecycle / grid node
    // ------------------------------------------------------------------

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide) {
            GridHelper.onFirstTick(this, be -> be.createNode());
        }
    }

    private void createNode() {
        Level level = getLevel();
        if (level != null && !level.isClientSide && !isRemoved()) {
            mainNode.create(level, worldPosition);
        }
    }

    @Override
    public appeng.api.util.AECableType getCableConnectionType(Direction dir) {
        return appeng.api.util.AECableType.SMART;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        mainNode.destroy();
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        mainNode.destroy();
    }

    @Override
    public IGridNode getGridNode(Direction direction) {
        return mainNode.getNode();
    }

    @Override
    public IGridNode getActionableNode() {
        return mainNode.getNode();
    }

    /** Called by the block when placed, transfers ownership of the AE node. */
    public void setOwner(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            mainNode.setOwningPlayer(serverPlayer);
        }
    }

    public IManagedGridNode getMainNode() {
        return mainNode;
    }

    // ------------------------------------------------------------------
    // Shared output engine hooks
    // ------------------------------------------------------------------

    @Override
    public boolean isOnline() {
        return online;
    }

    @Override
    protected boolean canOperate() {
        return this.online;
    }

    @Override
    protected ItemStack getCastStack() {
        return inventory.getStackInSlot(SLOT_CAST);
    }

    @Override
    protected void extractCast() {
        inventory.extractItem(SLOT_CAST, 1, false);
    }

    @Nullable
    @Override
    protected OutputSink getOutputSink() {
        MEStorage storage = getNetworkStorage();
        if (storage == null) {
            return null;
        }
        return new AeOutputSink(storage, getActionSource());
    }

    @Nullable
    private MEStorage getNetworkStorage() {
        IGrid grid = mainNode.getGrid();
        if (grid == null || grid.getStorageService() == null) {
            return null;
        }
        return grid.getStorageService().getInventory();
    }

    private IActionSource getActionSource() {
        if (this.actionSource == null) {
            this.actionSource = IActionSource.ofMachine(this);
        }
        return this.actionSource;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    // ------------------------------------------------------------------
    // Input: external item / fluid handlers proxy into the smeltery
    // ------------------------------------------------------------------

    /** Item handler capability: items are inserted into the smeltery melting inventory and melted. */
    public IItemHandler getItemInputHandler() {
        return ITEM_INPUT;
    }

    private final IItemHandler ITEM_INPUT = new IItemHandler() {
        @Nullable
        private MeltingModuleInventory melting() {
            HeatingStructureBlockEntity smeltery = getMasterSmeltery();
            return smeltery == null ? null : smeltery.getMeltingInventory();
        }

        @Override
        public int getSlots() {
            MeltingModuleInventory melting = melting();
            return melting == null ? 0 : melting.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            MeltingModuleInventory melting = melting();
            return melting == null ? ItemStack.EMPTY : melting.getStackInSlot(slot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            MeltingModuleInventory melting = melting();
            if (melting == null) {
                return stack;
            }
            int target = slot;
            if (!melting.validSlot(target)) {
                target = -1;
                for (int i = 0; i < melting.getSlots(); i++) {
                    if (melting.getStackInSlot(i).isEmpty() && melting.isItemValid(i, stack)) {
                        target = i;
                        break;
                    }
                }
                if (target < 0) {
                    return stack;
                }
            }
            return melting.insertItem(target, stack, simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            MeltingModuleInventory melting = melting();
            return melting == null ? 0 : melting.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            MeltingModuleInventory melting = melting();
            return melting != null && melting.isItemValid(slot, stack);
        }
    };

    /** Fluid handler capability: fluid is inserted into the smeltery tank (output uses the network). */
    public IFluidHandler getFluidInputHandler() {
        return FLUID_INPUT;
    }

    private final IFluidHandler FLUID_INPUT = new IFluidHandler() {
        @Nullable
        private SmelteryTank<?> tank() {
            HeatingStructureBlockEntity smeltery = getMasterSmeltery();
            return smeltery == null ? null : smeltery.getTank();
        }

        @Override
        public int getTanks() {
            SmelteryTank<?> tank = tank();
            return tank == null ? 0 : tank.getTanks();
        }

        @Override
        public FluidStack getFluidInTank(int tankIndex) {
            SmelteryTank<?> tank = tank();
            return tank == null ? FluidStack.EMPTY : tank.getFluidInTank(tankIndex);
        }

        @Override
        public int getTankCapacity(int tankIndex) {
            SmelteryTank<?> tank = tank();
            return tank == null ? 0 : tank.getTankCapacity(tankIndex);
        }

        @Override
        public boolean isFluidValid(int tankIndex, FluidStack stack) {
            SmelteryTank<?> tank = tank();
            return tank != null && tank.isFluidValid(tankIndex, stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            SmelteryTank<?> tank = tank();
            return tank == null ? 0 : tank.fill(resource, action);
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            return FluidStack.EMPTY;
        }
    };

    // ------------------------------------------------------------------
    // Menu / container
    // ------------------------------------------------------------------

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.tinkers_rainbow.seared_pattern_interface");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new SearedPatternInterfaceMenu(id, playerInventory, this);
    }

    public void dropContents() {
        Level level = getLevel();
        if (level == null || level.isClientSide) {
            return;
        }
        for (int i = 0; i < SLOT_COUNT; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                Block.popResource(level, worldPosition, stack);
            }
        }
        for (int i = 0; i < SLOT_COUNT; i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        return inventory.getStackInSlot(SLOT_CAST).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return inventory.extractItem(slot, amount, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = inventory.getStackInSlot(slot);
        inventory.setStackInSlot(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory.setStackInSlot(slot, stack);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return inventory.isItemValid(slot, stack);
    }

    @Override
    public void clearContent() {
        inventory.setStackInSlot(SLOT_CAST, ItemStack.EMPTY);
    }

    // ------------------------------------------------------------------
    // Serialization
    // ------------------------------------------------------------------

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(TAG_INVENTORY, net.minecraft.nbt.Tag.TAG_COMPOUND)) {
            inventory.deserializeNBT(registries, tag.getCompound(TAG_INVENTORY));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(TAG_INVENTORY, inventory.serializeNBT(registries));
    }

    // ------------------------------------------------------------------
    // Grid node listener
    // ------------------------------------------------------------------

    private static final class NodeListener implements IGridNodeListener<SearedPatternInterfaceBlockEntity> {

        static final NodeListener INSTANCE = new NodeListener();

        @Override
        public void onSaveChanges(SearedPatternInterfaceBlockEntity owner, IGridNode node) {
            owner.setChanged();
        }

        @Override
        public void onStateChanged(SearedPatternInterfaceBlockEntity owner, IGridNode node,
                                   IGridNodeListener.State state) {
            boolean wasOnline = owner.online;
            owner.online = owner.getMainNode().isActive();
            if (wasOnline != owner.online) {
                owner.setChanged();
            }
        }
    }
}
