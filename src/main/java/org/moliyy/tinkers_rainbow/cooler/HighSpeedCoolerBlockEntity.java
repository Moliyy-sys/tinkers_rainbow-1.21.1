package org.moliyy.tinkers_rainbow.cooler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import org.jetbrains.annotations.Nullable;

import org.moliyy.tinkers_rainbow.smeltery.AbstractSearedOutputBlockEntity;
import org.moliyy.tinkers_rainbow.smeltery.OutputMode;
import org.moliyy.tinkers_rainbow.smeltery.OutputSink;

/**
 * Block entity of the high speed cooler.
 *
 * <p>Reuses the shared instant-output engine of {@link AbstractSearedOutputBlockEntity}. The cooler only
 * produces <b>items</b> (blocks from basin recipes and cast products from table recipes) into 27 internal
 * output slots, like a vanilla chest. When the slots are full the sink stops accepting, which makes the
 * transfer engine stop producing (lazy output).</p>
 */
public class HighSpeedCoolerBlockEntity extends AbstractSearedOutputBlockEntity {

    public static final int CAST_SLOT = 0;
    public static final int OUTPUT_SLOTS = 27;
    public static final int SLOT_COUNT = 1 + OUTPUT_SLOTS;

    private static final String TAG_CAST = "cast";
    private static final String TAG_OUTPUTS = "outputs";

    private final ItemStackHandler castSlot = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            HighSpeedCoolerBlockEntity.this.setChanged();
        }
    };

    private final ItemStackHandler outputs = new ItemStackHandler(OUTPUT_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            HighSpeedCoolerBlockEntity.this.setChanged();
        }
    };

    private final OutputSink sink = new OutputSink() {
        @Override
        public long insertFluid(FluidStack stack) {
            return 0; // the cooler only stores items
        }

        @Override
        public int insertItem(ItemStack stack) {
            return fillOutputs(stack);
        }
    };

    public HighSpeedCoolerBlockEntity(BlockPos pos, BlockState state) {
        super(CoolerRegistry.HIGH_SPEED_COOLER_BLOCK_ENTITY.get(), pos, state);
    }

    // ------------------------------------------------------------------
    // shared engine hooks
    // ------------------------------------------------------------------

    @Override
    protected OutputSink getOutputSink() {
        return this.sink;
    }

    @Override
    protected boolean canOperate() {
        return isFormed();
    }

    @Override
    public boolean isOnline() {
        return isFormed();
    }

    @Override
    protected ItemStack getCastStack() {
        return castSlot.getStackInSlot(0);
    }

    @Override
    protected void extractCast() {
        castSlot.extractItem(0, 1, false);
    }

    /** The cooler only returns blocks (basin) or cast products (table); fluid output is not supported. */
    @Override
    protected boolean supportsOutputMode(OutputMode mode) {
        return mode == OutputMode.BASIN || mode == OutputMode.TABLE;
    }

    @Override
    protected OutputMode defaultOutputMode() {
        return OutputMode.TABLE;
    }

    // ------------------------------------------------------------------
    // internal sink (lazy output: stop when full)
    // ------------------------------------------------------------------

    private int fillOutputs(ItemStack stack) {
        ItemStack remaining = stack.copy();
        for (int i = 0; i < outputs.getSlots() && !remaining.isEmpty(); i++) {
            remaining = outputs.insertItem(i, remaining, false);
        }
        return stack.getCount() - remaining.getCount();
    }

    // ------------------------------------------------------------------
    // capabilities for automation
    // ------------------------------------------------------------------

    /** Item handler exposing the output slots (extraction only). */
    public ItemStackHandler getOutputInventory() {
        return outputs;
    }

    public ItemStackHandler getCastInventory() {
        return castSlot;
    }

    // ------------------------------------------------------------------
    // menu / container
    // ------------------------------------------------------------------

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.tinkers_rainbow.high_speed_cooler");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new HighSpeedCoolerMenu(id, playerInventory, this);
    }

    public void dropContents() {
        Level level = getLevel();
        if (level == null || level.isClientSide) {
            return;
        }
        ItemStack cast = castSlot.getStackInSlot(0);
        if (!cast.isEmpty()) {
            Block.popResource(level, worldPosition, cast);
            castSlot.setStackInSlot(0, ItemStack.EMPTY);
        }
        for (int i = 0; i < outputs.getSlots(); i++) {
            ItemStack stack = outputs.getStackInSlot(i);
            if (!stack.isEmpty()) {
                Block.popResource(level, worldPosition, stack);
                outputs.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot == CAST_SLOT) {
            return castSlot.getStackInSlot(0);
        }
        int output = slot - 1;
        return output >= 0 && output < OUTPUT_SLOTS ? outputs.getStackInSlot(output) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot == CAST_SLOT) {
            return castSlot.extractItem(0, amount, false);
        }
        int output = slot - 1;
        return output >= 0 && output < OUTPUT_SLOTS ? outputs.extractItem(output, amount, false) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = getItem(slot);
        setItem(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot == CAST_SLOT) {
            castSlot.setStackInSlot(0, stack);
        } else {
            int output = slot - 1;
            if (output >= 0 && output < OUTPUT_SLOTS) {
                outputs.setStackInSlot(output, stack);
            }
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        // only the cast slot is a player input; the other slots are outputs
        return slot == CAST_SLOT;
    }

    @Override
    public void clearContent() {
        castSlot.setStackInSlot(0, ItemStack.EMPTY);
        for (int i = 0; i < OUTPUT_SLOTS; i++) {
            outputs.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    // ------------------------------------------------------------------
    // serialization
    // ------------------------------------------------------------------

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(TAG_CAST, net.minecraft.nbt.Tag.TAG_COMPOUND)) {
            castSlot.deserializeNBT(registries, tag.getCompound(TAG_CAST));
        }
        if (tag.contains(TAG_OUTPUTS, net.minecraft.nbt.Tag.TAG_COMPOUND)) {
            outputs.deserializeNBT(registries, tag.getCompound(TAG_OUTPUTS));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(TAG_CAST, castSlot.serializeNBT(registries));
        tag.put(TAG_OUTPUTS, outputs.serializeNBT(registries));
    }
}
