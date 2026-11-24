package org.moliyy.tinkers_rainbow.smeltery;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

import org.jetbrains.annotations.Nullable;

import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.casting.ICastingRecipe;
import slimeknights.tconstruct.smeltery.block.entity.component.SmelteryComponentBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.controller.HeatingStructureBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.tank.SmelteryTank;

/**
 * Shared engine of the blocks that instantly pull casting output out of a smeltery.
 *
 * <p>Both the AE seared interface and the high speed cooler use this class, so the transfer mode,
 * numeric limit and the instant melting/casting logic only exist once. Subclasses provide the
 * {@link OutputSink} (ME network or internal slots) and the cast slot.</p>
 *
 * <p>Modes:</p>
 * <ul>
 *     <li>{@link TransferMode#AUTO}: move output every operation, capped by the limit per operation.</li>
 *     <li>{@link TransferMode#BLOCK}: move until exactly the limit has been moved in total, then stop until
 *     the configuration changes.</li>
 *     <li>{@link TransferMode#MANUAL}: only move when the player presses the return button.</li>
 * </ul>
 *
 * <p>A limit of {@code 0} pauses the block, {@link #LIMIT_UNLIMITED} moves everything available.</p>
 */
public abstract class AbstractSearedOutputBlockEntity extends SmelteryComponentBlockEntity
        implements Container, MenuProvider {

    public static final int LIMIT_PAUSED = 0;
    public static final int LIMIT_UNLIMITED = Integer.MAX_VALUE;
    /** Largest value a player may type into the GUI; higher values are clamped to this. */
    public static final int MAX_ENTERABLE_LIMIT = 1_000_000;

    public static final int ACTION_SET_LIMIT = 0;
    public static final int ACTION_SET_TRANSFER_MODE = 1;
    public static final int ACTION_MANUAL_TRANSFER = 2;
    public static final int ACTION_SET_OUTPUT_MODE = 3;

    protected static final int TICK_INTERVAL = 2;
    protected static final int CAST_BUDGET = 64;

    private static final String TAG_OUTPUT_MODE = "output_mode";
    private static final String TAG_TRANSFER_MODE = "transfer_mode";
    private static final String TAG_OUTPUT_LIMIT = "output_limit";
    private static final String TAG_BLOCK_FLUID = "block_transferred_fluid";
    private static final String TAG_BLOCK_ITEMS = "block_transferred_items";

    private final CastingRecipeCache recipeCache = new CastingRecipeCache();

    private OutputMode outputMode = OutputMode.FLUID;
    private TransferMode transferMode = TransferMode.MANUAL;
    private int outputLimit = LIMIT_PAUSED;
    /** Amount already moved while in {@link TransferMode#BLOCK}. */
    private int blockFluidTransferred = 0;
    private int blockItemsTransferred = 0;
    private int tickCounter = 0;
    private boolean formed = false;

    protected AbstractSearedOutputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        // the cooler only supports item output, so start with its declared default instead of fluid
        this.outputMode = defaultOutputMode();
    }

    // ------------------------------------------------------------------
    // subclass contract
    // ------------------------------------------------------------------

    /** Destination of the instant output, or {@code null} when it cannot accept anything right now. */
    @Nullable
    protected abstract OutputSink getOutputSink();

    /** Whether the block may currently operate (AE: node online; cooler: always when formed). */
    protected abstract boolean canOperate();

    /** The item placed in the cast slot (may be empty for fluid/basin output). */
    protected abstract ItemStack getCastStack();

    /** Online state used by the GUI status line. */
    public abstract boolean isOnline();

    /** Whether this block supports the given output kind (the cooler only supports item output). */
    protected boolean supportsOutputMode(OutputMode mode) {
        return true;
    }

    /** Output kind used when an unsupported one is loaded or requested. */
    protected OutputMode defaultOutputMode() {
        return OutputMode.FLUID;
    }

    // ------------------------------------------------------------------
    // state
    // ------------------------------------------------------------------

    @Nullable
    public HeatingStructureBlockEntity getMasterSmeltery() {
        BlockPos master = getMasterPos();
        Level level = getLevel();
        if (master == null || level == null) {
            return null;
        }
        BlockEntity blockEntity = level.getBlockEntity(master);
        return blockEntity instanceof HeatingStructureBlockEntity heating ? heating : null;
    }

    public void serverTick() {
        Level level = getLevel();
        if (level == null || level.isClientSide) {
            return;
        }
        this.tickCounter++;
        HeatingStructureBlockEntity smeltery = getMasterSmeltery();
        boolean nowFormed = smeltery != null;
        if (nowFormed != this.formed) {
            this.formed = nowFormed;
            this.setChanged();
        }
        updateExtraState();
        if (this.tickCounter % TICK_INTERVAL != 0 || !this.formed || !canOperate()) {
            return;
        }
        if (this.transferMode == TransferMode.AUTO || this.transferMode == TransferMode.BLOCK) {
            this.autoTransfer();
        }
    }

    /** Hook for subclasses that track extra state every tick (the AE interface updates its node state). */
    protected void updateExtraState() {
    }

    private void autoTransfer() {
        if (this.outputLimit == LIMIT_PAUSED) {
            return;
        }
        int cap = this.outputLimit == LIMIT_UNLIMITED ? Integer.MAX_VALUE : this.outputLimit;
        if (this.transferMode == TransferMode.BLOCK) {
            int moved = outputMode == OutputMode.FLUID ? this.blockFluidTransferred : this.blockItemsTransferred;
            cap -= moved;
            if (cap <= 0) {
                return; // exactly the requested amount has been transferred already
            }
        }
        int moved = transfer(cap);
        if (moved > 0 && this.transferMode == TransferMode.BLOCK) {
            if (outputMode == OutputMode.FLUID) {
                this.blockFluidTransferred += moved;
            } else {
                this.blockItemsTransferred += moved;
            }
            this.setChanged();
        }
    }

    /** Moves one manual batch; called by the return button payload. */
    public void manualTransfer() {
        if (this.transferMode != TransferMode.MANUAL || this.outputLimit == LIMIT_PAUSED) {
            return;
        }
        if (!canOperate() || !this.formed) {
            return;
        }
        int cap = this.outputLimit == LIMIT_UNLIMITED ? Integer.MAX_VALUE : this.outputLimit;
        transfer(cap);
    }

    /**
     * Moves output from the smeltery to the sink.
     *
     * @param cap maximum amount of fluid (mB) or items to move in this operation
     * @return the amount that was actually moved (mB for fluid mode, item count for casting modes)
     */
    private int transfer(int cap) {
        OutputSink sink = getOutputSink();
        HeatingStructureBlockEntity smeltery = getMasterSmeltery();
        Level level = getLevel();
        if (sink == null || smeltery == null || smeltery.getTank() == null || level == null) {
            return 0;
        }
        SmelteryTank<?> tank = smeltery.getTank();
        if (outputMode == OutputMode.FLUID) {
            return transferFluid(tank, sink, cap);
        }
        RecipeType<ICastingRecipe> recipeType = outputMode == OutputMode.BASIN
                ? TinkerRecipeTypes.CASTING_BASIN.get()
                : TinkerRecipeTypes.CASTING_TABLE.get();
        return transferCasts(level, tank, sink, recipeType, getCastStack(), cap);
    }

    /** Instant fluid extraction. */
    private int transferFluid(SmelteryTank<?> tank, OutputSink sink, int cap) {
        int remaining = cap;
        int moved = 0;
        for (FluidStack fluid : List.copyOf(tank.getFluids())) {
            if (remaining <= 0) {
                break;
            }
            if (fluid.isEmpty()) {
                continue;
            }
            FluidStack offer = fluid.copyWithAmount(Math.min(fluid.getAmount(), remaining));
            long accepted = sink.insertFluid(offer);
            if (accepted <= 0) {
                break; // sink is full: stop producing (lazy output)
            }
            tank.drain(offer.copyWithAmount((int) Math.min(accepted, offer.getAmount())), FluidAction.EXECUTE);
            moved += (int) Math.min(accepted, Integer.MAX_VALUE);
            if (cap != Integer.MAX_VALUE) {
                remaining -= (int) Math.min(accepted, Integer.MAX_VALUE);
            }
        }
        return moved;
    }

    /** Instant casting (table with cast, basin without). */
    private int transferCasts(Level level, SmelteryTank<?> tank, OutputSink sink,
                              RecipeType<ICastingRecipe> recipeType, ItemStack castStack, int cap) {
        int produced = 0;
        int budget = CAST_BUDGET;
        for (FluidStack tankFluid : List.copyOf(tank.getFluids())) {
            if (budget <= 0 || produced >= cap) {
                break;
            }
            if (tankFluid.isEmpty()) {
                continue;
            }
            ICastingRecipe recipe = this.recipeCache.find(level, recipeType, castStack, tankFluid);
            if (recipe == null) {
                continue;
            }
            while (budget > 0 && produced < cap) {
                ItemStack currentCast = castStack.isEmpty() ? ItemStack.EMPTY : getCastStack();
                FluidStack found = findFluid(tank, tankFluid, 1);
                if (found == null) {
                    break;
                }
                CastingRecipeCache.SimpleCastingContainer container =
                        new CastingRecipeCache.SimpleCastingContainer(currentCast.copy(), found.copy());
                if (!recipe.matches(container, level)) {
                    this.recipeCache.clear();
                    break;
                }
                int fluidAmount = recipe.getFluidAmount(container);
                if (fluidAmount <= 0 || found.getAmount() < fluidAmount) {
                    break;
                }
                boolean consumed = recipe.isConsumed(container);
                if (consumed && currentCast.isEmpty()) {
                    break;
                }
                ItemStack output = recipe.assemble(container, level.registryAccess());
                if (output.isEmpty()) {
                    break;
                }
                // never exceed the cap, but always allow one craft so a limit smaller than the recipe
                // output still returns something instead of dead-locking
                if (produced > 0 && produced + output.getCount() > cap) {
                    break;
                }
                int accepted = sink.insertItem(output.copy());
                if (accepted < output.getCount()) {
                    if (accepted > 0) {
                        // partial insert: keep the accepted part, the rest stays in the tank for next time
                        tank.drain(found.copyWithAmount(fluidAmount * accepted / output.getCount()),
                                FluidAction.EXECUTE);
                    }
                    break; // sink full / no space for the rest
                }
                tank.drain(found.copyWithAmount(fluidAmount), FluidAction.EXECUTE);
                if (consumed) {
                    extractCast();
                }
                produced += output.getCount();
                budget--;
            }
        }
        return produced;
    }

    @Nullable
    private static FluidStack findFluid(SmelteryTank<?> tank, FluidStack fluid, int needed) {
        for (FluidStack candidate : tank.getFluids()) {
            if (FluidStack.isSameFluidSameComponents(candidate, fluid) && candidate.getAmount() >= needed) {
                return candidate;
            }
        }
        return null;
    }

    /** Removes one item from the cast slot after a consumed-cast recipe. */
    protected abstract void extractCast();

    // ------------------------------------------------------------------
    // configuration (called from the GUI payload)
    // ------------------------------------------------------------------

    public void applyControlAction(int action, int value) {
        switch (action) {
            case ACTION_SET_LIMIT -> setOutputLimit(value);
            case ACTION_SET_TRANSFER_MODE -> setTransferMode(TransferMode.byId(value));
            case ACTION_MANUAL_TRANSFER -> manualTransfer();
            case ACTION_SET_OUTPUT_MODE -> setOutputMode(OutputMode.byId(value));
            default -> {
            }
        }
    }

    public OutputMode getOutputMode() {
        return outputMode;
    }

    public void setOutputMode(OutputMode mode) {
        if (!supportsOutputMode(mode)) {
            return;
        }
        if (this.outputMode != mode) {
            this.outputMode = mode;
            resetBlockCounters();
            this.recipeCache.clear();
            this.setChanged();
        }
    }

    public TransferMode getTransferMode() {
        return transferMode;
    }

    public void setTransferMode(TransferMode mode) {
        if (this.transferMode != mode) {
            this.transferMode = mode;
            resetBlockCounters();
            this.setChanged();
        }
    }

    public int getOutputLimit() {
        return outputLimit;
    }

    /** Clamps the limit: <= 0 pauses, {@link #LIMIT_UNLIMITED} means everything, otherwise capped. */
    public void setOutputLimit(int limit) {
        int clamped;
        if (limit <= 0) {
            clamped = LIMIT_PAUSED;
        } else if (limit >= LIMIT_UNLIMITED) {
            clamped = LIMIT_UNLIMITED;
        } else {
            clamped = Math.min(limit, MAX_ENTERABLE_LIMIT);
        }
        if (this.outputLimit != clamped) {
            this.outputLimit = clamped;
            resetBlockCounters();
            this.setChanged();
        }
    }

    private void resetBlockCounters() {
        this.blockFluidTransferred = 0;
        this.blockItemsTransferred = 0;
    }

    public boolean isFormed() {
        return formed;
    }

    // ------------------------------------------------------------------
    // GUI sync (both menus share this data layout)
    // ------------------------------------------------------------------

    public int getSyncData(int index) {
        return switch (index) {
            case SearedMenuData.OUTPUT_MODE -> outputMode.ordinal();
            case SearedMenuData.ONLINE -> isOnline() ? 1 : 0;
            case SearedMenuData.FORMED -> formed ? 1 : 0;
            case SearedMenuData.OUTPUT_LIMIT -> outputLimit;
            case SearedMenuData.TRANSFER_MODE -> transferMode.ordinal();
            default -> 0;
        };
    }

    /** Container data instance with a client side cache, reusable by both menus. */
    public ContainerData createContainerData() {
        return new ContainerData() {
            private final int[] cache = new int[SearedMenuData.COUNT];

            @Override
            public int get(int index) {
                Level level = getLevel();
                if (level != null && !level.isClientSide) {
                    return getSyncData(index);
                }
                return index >= 0 && index < cache.length ? cache[index] : 0;
            }

            @Override
            public void set(int index, int value) {
                if (index >= 0 && index < cache.length) {
                    cache[index] = value;
                }
            }

            @Override
            public int getCount() {
                return SearedMenuData.COUNT;
            }
        };
    }

    // ------------------------------------------------------------------
    // serialization
    // ------------------------------------------------------------------

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        OutputMode loadedMode = OutputMode.byId(tag.getInt(TAG_OUTPUT_MODE));
        this.outputMode = supportsOutputMode(loadedMode) ? loadedMode : defaultOutputMode();
        this.transferMode = TransferMode.byId(tag.getInt(TAG_TRANSFER_MODE));
        setOutputLimit(tag.contains(TAG_OUTPUT_LIMIT) ? tag.getInt(TAG_OUTPUT_LIMIT) : LIMIT_PAUSED);
        this.blockFluidTransferred = tag.getInt(TAG_BLOCK_FLUID);
        this.blockItemsTransferred = tag.getInt(TAG_BLOCK_ITEMS);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt(TAG_OUTPUT_MODE, outputMode.ordinal());
        tag.putInt(TAG_TRANSFER_MODE, transferMode.ordinal());
        tag.putInt(TAG_OUTPUT_LIMIT, outputLimit);
        tag.putInt(TAG_BLOCK_FLUID, blockFluidTransferred);
        tag.putInt(TAG_BLOCK_ITEMS, blockItemsTransferred);
    }

    // ------------------------------------------------------------------
    // MenuProvider / Container
    // ------------------------------------------------------------------

    @Override
    public abstract Component getDisplayName();

    @Nullable
    @Override
    public abstract AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player);

    @Override
    public abstract int getContainerSize();

    @Override
    public abstract boolean isEmpty();

    @Override
    public abstract ItemStack getItem(int slot);

    @Override
    public abstract ItemStack removeItem(int slot, int amount);

    @Override
    public abstract ItemStack removeItemNoUpdate(int slot);

    @Override
    public abstract void setItem(int slot, ItemStack stack);

    @Override
    public abstract boolean canPlaceItem(int slot, ItemStack stack);

    @Override
    public abstract void clearContent();

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(getBlockPos().getX() + 0.5D, getBlockPos().getY() + 0.5D,
                getBlockPos().getZ() + 0.5D) <= 64.0D;
    }
}
