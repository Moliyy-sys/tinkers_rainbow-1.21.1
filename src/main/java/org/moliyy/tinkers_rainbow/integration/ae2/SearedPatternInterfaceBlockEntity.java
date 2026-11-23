package org.moliyy.tinkers_rainbow.integration.ae2;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
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
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.casting.ICastingContainer;
import slimeknights.tconstruct.library.recipe.casting.ICastingRecipe;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipe;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;
import slimeknights.tconstruct.library.recipe.melting.IMeltingRecipe;
import slimeknights.tconstruct.smeltery.block.entity.component.SmelteryComponentBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.controller.HeatingStructureBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.module.MeltingModuleInventory;
import slimeknights.tconstruct.smeltery.block.entity.tank.SmelteryTank;

/**
 * Block entity of the seared pattern interface.
 *
 * <p>The interface is both a smeltery servant (placed inside the smeltery wall, exactly like a drain) and an
 * in-world AE2 grid node. It performs two independent jobs:</p>
 * <ol>
 *     <li><b>Melting input:</b> the installed {@link SearedPatternItem} lists items that are pulled out of the
 *     ME network and inserted into the smeltery melting inventory. A batch is fed completely, then the
 *     interface waits until every item finished melting before the next batch starts. With blocking mode
 *     enabled the interface additionally refuses to start a batch while the tank holds fluids that the
 *     pattern does not expect, which prevents accidental alloys.</li>
 *     <li><b>Output:</b> depending on the selected {@link Mode} the interface either returns the smeltery
 *     fluid to the network as-is, casts the fluid into items using the basin recipe type, or casts the fluid
 *     into items using the casting table recipe type together with the cast placed in the cast slot. The
 *     whole conversion is instant, there is no cooling time.</li>
 * </ol>
 */
public class SearedPatternInterfaceBlockEntity extends SmelteryComponentBlockEntity
        implements Container, MenuProvider, IInWorldGridNodeHost, IActionHost {

    public static final int SLOT_PATTERN = 0;
    public static final int SLOT_CAST = 1;
    public static final int SLOT_SAMPLE = 2;
    public static final int SLOT_COUNT = 3;

    private static final int TICK_INTERVAL = 2;
    private static final int CAST_BUDGET = 64;
    private static final int FEED_BUDGET = 64;
    private static final int FEED_BUDGET_BLOCKING = 16;

    private static final String TAG_INVENTORY = "inventory";
    private static final String TAG_MODE = "mode";
    private static final String TAG_BLOCKING = "blocking";

    /** Selects how fluid is converted back into the network. */
    public enum Mode {
        /** Fluid is inserted into the ME network as fluid, cast entries of the pattern are processed as items. */
        NONE,
        /** Fluid is cast using casting basin recipes (no cast required), items are inserted into the network. */
        BASIN,
        /** Fluid is cast using casting table recipes and the cast in the cast slot. */
        TABLE;

        public static Mode byId(int id) {
            Mode[] values = values();
            return id >= 0 && id < values.length ? values[id] : NONE;
        }
    }

    /** State of the melting input batch. */
    public enum FeedState {
        /** Waiting for a chance to start a new batch. */
        IDLE,
        /** Items of the current batch are being pulled out of the network. */
        FEEDING,
        /** All items of the batch were inserted, waiting for them to melt. */
        WAITING
    }

    private final ItemStackHandler inventory = new ItemStackHandler(SLOT_COUNT) {
        @Override
        protected void onContentsChanged(int slot) {
            if (slot == SLOT_PATTERN) {
                resetBatch();
            }
            SearedPatternInterfaceBlockEntity.this.setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == SLOT_PATTERN) {
                return SearedPatternItem.isPattern(stack);
            }
            return super.isItemValid(slot, stack);
        }

        @Override
        public int getSlotLimit(int slot) {
            return slot == SLOT_PATTERN ? 1 : super.getSlotLimit(slot);
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

    private Mode mode = Mode.NONE;
    private boolean blocking = false;
    private FeedState feedState = FeedState.IDLE;
    private int[] fedCounts = new int[0];
    private int tickCounter = 0;
    private boolean online = false;
    private boolean formed = false;

    @Nullable
    private ICastingRecipe cachedRecipe;
    @Nullable
    private RecipeType<?> cachedRecipeType;
    @Nullable
    private AEItemKey cachedRecipeCast;
    @Nullable
    private AEFluidKey cachedRecipeFluid;

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

    /** Called by {@link SearedPatternInterfaceBlock} when the block is placed by a player. */
    public void setOwner(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            mainNode.setOwningPlayer(serverPlayer);
        }
    }

    public IManagedGridNode getMainNode() {
        return mainNode;
    }

    /** Resolves the smeltery this interface belongs to, if the multiblock is formed. */
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

    // ------------------------------------------------------------------
    // Ticking
    // ------------------------------------------------------------------

    public void serverTick() {
        Level level = getLevel();
        if (level == null || level.isClientSide) {
            return;
        }
        this.tickCounter++;

        HeatingStructureBlockEntity smeltery = getMasterSmeltery();
        boolean nowOnline = mainNode.isActive();
        boolean nowFormed = smeltery != null;
        if (nowOnline != this.online || nowFormed != this.formed) {
            this.online = nowOnline;
            this.formed = nowFormed;
            this.setChanged();
        }
        if (this.tickCounter % TICK_INTERVAL != 0 || !this.online) {
            return;
        }
        if (smeltery == null || smeltery.getTank() == null || smeltery.getMeltingInventory() == null) {
            return;
        }
        IGrid grid = mainNode.getGrid();
        if (grid == null || grid.getStorageService() == null) {
            return;
        }
        MEStorage storage = grid.getStorageService().getInventory();
        if (storage == null) {
            return;
        }
        IActionSource source = getActionSource();
        SmelteryTank<?> tank = smeltery.getTank();

        handleCasting(storage, tank, source);
        handleFluidReturn(storage, tank, source);
        handleFeeding(smeltery, tank, storage, source);
    }

    private IActionSource getActionSource() {
        if (this.actionSource == null) {
            this.actionSource = IActionSource.ofMachine(this);
        }
        return this.actionSource;
    }

    // ------------------------------------------------------------------
    // Output: fluid -> items / fluid
    // ------------------------------------------------------------------

    private void handleCasting(MEStorage storage, SmelteryTank<?> tank, IActionSource source) {
        if (mode == Mode.NONE) {
            handlePatternCasting(storage, tank, source);
            return;
        }
        RecipeType<ICastingRecipe> recipeType = mode == Mode.BASIN
                ? TinkerRecipeTypes.CASTING_BASIN.get()
                : TinkerRecipeTypes.CASTING_TABLE.get();
        ItemStack castStack = mode == Mode.TABLE ? inventory.getStackInSlot(SLOT_CAST) : ItemStack.EMPTY;
        castFluid(storage, tank, source, recipeType, castStack);
    }

    /** Uses the basin casting recipes stored in the pattern to turn fluid into items. */
    private void handlePatternCasting(MEStorage storage, SmelteryTank<?> tank, IActionSource source) {
        ItemStack patternStack = inventory.getStackInSlot(SLOT_PATTERN);
        if (!SearedPatternItem.isPattern(patternStack)) {
            return;
        }
        SearedPatternData data = SearedPatternItem.getData(patternStack);
        if (data.casts().isEmpty()) {
            return;
        }
        int budget = CAST_BUDGET;
        for (SearedPatternData.CastEntry cast : data.casts()) {
            while (budget > 0) {
                FluidStack found = findFluid(tank, cast.fluid(), cast.fluidAmount());
                if (found == null) {
                    break;
                }
                if (storage.insert(cast.result(), cast.resultCount(), Actionable.SIMULATE, source) < cast.resultCount()) {
                    break;
                }
                storage.insert(cast.result(), cast.resultCount(), Actionable.MODULATE, source);
                tank.drain(found.copyWithAmount(cast.fluidAmount()), FluidAction.EXECUTE);
                budget--;
            }
        }
    }

    /** Casts any tank fluid matching the given recipe type, optionally using the item in the cast slot. */
    private void castFluid(MEStorage storage, SmelteryTank<?> tank, IActionSource source,
                           RecipeType<ICastingRecipe> recipeType, ItemStack castStack) {
        Level level = getLevel();
        if (level == null) {
            return;
        }
        int budget = CAST_BUDGET;
        for (FluidStack tankFluid : List.copyOf(tank.getFluids())) {
            if (budget <= 0) {
                break;
            }
            if (tankFluid.isEmpty()) {
                continue;
            }
            ICastingRecipe recipe = findCastingRecipe(level, recipeType, castStack, tankFluid);
            if (recipe == null) {
                continue;
            }
            while (budget > 0) {
                ItemStack currentCast = castStack.isEmpty() ? ItemStack.EMPTY : inventory.getStackInSlot(SLOT_CAST);
                FluidStack found = findFluid(tank, tankFluid, 1);
                if (found == null) {
                    break;
                }
                CastingInventoryContainer container = new CastingInventoryContainer(currentCast.copy(), found.copy());
                if (!recipe.matches(container, level)) {
                    clearRecipeCache();
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
                AEItemKey resultKey = AEItemKey.of(output);
                if (resultKey == null) {
                    break;
                }
                if (storage.insert(resultKey, output.getCount(), Actionable.SIMULATE, source) < output.getCount()) {
                    break;
                }
                storage.insert(resultKey, output.getCount(), Actionable.MODULATE, source);
                tank.drain(found.copyWithAmount(fluidAmount), FluidAction.EXECUTE);
                if (consumed) {
                    inventory.extractItem(SLOT_CAST, 1, false);
                }
                budget--;
            }
        }
    }

    @Nullable
    private ICastingRecipe findCastingRecipe(Level level, RecipeType<ICastingRecipe> recipeType,
                                             ItemStack castStack, FluidStack fluid) {
        AEItemKey castKey = castStack.isEmpty() ? null : AEItemKey.of(castStack);
        AEFluidKey fluidKey = AEFluidKey.of(fluid);
        if (this.cachedRecipe != null && this.cachedRecipeType == recipeType
                && java.util.Objects.equals(this.cachedRecipeCast, castKey)
                && java.util.Objects.equals(this.cachedRecipeFluid, fluidKey)) {
            return this.cachedRecipe;
        }
        CastingInventoryContainer container = new CastingInventoryContainer(castStack.copy(), fluid.copy());
        Optional<RecipeHolder<ICastingRecipe>> holder = level.getRecipeManager()
                .getRecipeFor(recipeType, container, level);
        if (holder.isEmpty()) {
            clearRecipeCache();
            return null;
        }
        ICastingRecipe recipe = holder.get().value();
        // Only plain item casting recipes are automated: container filling and similar recipes would need to
        // transform the cast slot item itself, which this interface deliberately does not support.
        if (!(recipe instanceof ItemCastingRecipe)) {
            clearRecipeCache();
            return null;
        }
        this.cachedRecipe = recipe;
        this.cachedRecipeType = recipeType;
        this.cachedRecipeCast = castKey;
        this.cachedRecipeFluid = fluidKey;
        return recipe;
    }

    private void clearRecipeCache() {
        this.cachedRecipe = null;
        this.cachedRecipeType = null;
        this.cachedRecipeCast = null;
        this.cachedRecipeFluid = null;
    }

    /** Returns every fluid of the smeltery to the ME network (plain mode only). */
    private void handleFluidReturn(MEStorage storage, SmelteryTank<?> tank, IActionSource source) {
        if (mode != Mode.NONE) {
            return;
        }
        for (FluidStack fluid : List.copyOf(tank.getFluids())) {
            if (fluid.isEmpty()) {
                continue;
            }
            AEFluidKey key = AEFluidKey.of(fluid);
            if (key == null) {
                continue;
            }
            long inserted = storage.insert(key, fluid.getAmount(), Actionable.MODULATE, source);
            if (inserted > 0) {
                tank.drain(fluid.copyWithAmount((int) Math.min(inserted, fluid.getAmount())), FluidAction.EXECUTE);
            }
        }
    }

    // ------------------------------------------------------------------
    // Input: pattern items -> smeltery
    // ------------------------------------------------------------------

    private void handleFeeding(HeatingStructureBlockEntity smeltery, SmelteryTank<?> tank,
                               MEStorage storage, IActionSource source) {
        ItemStack patternStack = inventory.getStackInSlot(SLOT_PATTERN);
        if (!SearedPatternItem.isPattern(patternStack)) {
            resetBatch();
            return;
        }
        SearedPatternData data = SearedPatternItem.getData(patternStack);
        if (data.inputs().isEmpty()) {
            resetBatch();
            return;
        }
        MeltingModuleInventory melting = smeltery.getMeltingInventory();
        if (melting == null) {
            return;
        }
        if (this.fedCounts.length != data.inputs().size()) {
            resetBatch();
        }
        switch (this.feedState) {
            case IDLE -> {
                if (canStartBatch(tank, melting, data)) {
                    this.fedCounts = new int[data.inputs().size()];
                    this.feedState = FeedState.FEEDING;
                }
            }
            case FEEDING -> {
                if (feedBatch(tank, melting, data, storage, source)) {
                    this.feedState = FeedState.WAITING;
                }
            }
            case WAITING -> {
                if (isMeltingEmpty(melting)) {
                    resetBatch();
                }
            }
        }
    }

    private boolean canStartBatch(SmelteryTank<?> tank, MeltingModuleInventory melting, SearedPatternData data) {
        if (!isMeltingEmpty(melting)) {
            return false;
        }
        if (tank.getRemainingSpace() <= 0) {
            return false;
        }
        if (blocking) {
            for (FluidStack fluid : tank.getFluids()) {
                if (!isExpectedFluid(data, fluid)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isExpectedFluid(SearedPatternData data, FluidStack fluid) {
        for (SearedPatternData.MeltingEntry entry : data.inputs()) {
            if (entry.fluid().matches(fluid)) {
                return true;
            }
        }
        for (SearedPatternData.CastEntry cast : data.casts()) {
            if (cast.fluid().matches(fluid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Feeds remaining items of the current batch. Returns true once every entry reached its target count.
     */
    private boolean feedBatch(SmelteryTank<?> tank, MeltingModuleInventory melting, SearedPatternData data,
                              MEStorage storage, IActionSource source) {
        if (this.fedCounts.length != data.inputs().size()) {
            this.fedCounts = new int[data.inputs().size()];
        }
        int budget = blocking ? FEED_BUDGET_BLOCKING : FEED_BUDGET;
        boolean progress = true;
        while (budget > 0 && progress) {
            progress = false;
            for (int i = 0; i < data.inputs().size() && budget > 0; i++) {
                SearedPatternData.MeltingEntry entry = data.inputs().get(i);
                if (this.fedCounts[i] >= entry.count()) {
                    continue;
                }
                if (tank.getRemainingSpace() < entry.fluidAmount()) {
                    continue;
                }
                int slot = findEmptyMeltingSlot(melting);
                if (slot < 0) {
                    continue;
                }
                if (storage.extract(entry.item(), 1, Actionable.SIMULATE, source) <= 0) {
                    continue;
                }
                if (storage.extract(entry.item(), 1, Actionable.MODULATE, source) <= 0) {
                    continue;
                }
                ItemStack remainder = melting.insertItem(slot, entry.item().toStack(1), false);
                if (!remainder.isEmpty()) {
                    storage.insert(entry.item(), 1, Actionable.MODULATE, source);
                    continue;
                }
                this.fedCounts[i]++;
                budget--;
                progress = true;
            }
        }
        for (int i = 0; i < data.inputs().size(); i++) {
            if (this.fedCounts[i] < data.inputs().get(i).count()) {
                return false;
            }
        }
        return true;
    }

    private static int findEmptyMeltingSlot(MeltingModuleInventory melting) {
        for (int i = 0; i < melting.getSlots(); i++) {
            if (melting.getStackInSlot(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private static boolean isMeltingEmpty(MeltingModuleInventory melting) {
        for (int i = 0; i < melting.getSlots(); i++) {
            if (!melting.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void resetBatch() {
        this.feedState = FeedState.IDLE;
        this.fedCounts = new int[0];
    }

    // ------------------------------------------------------------------
    // Fluid helpers
    // ------------------------------------------------------------------

    @Nullable
    private static FluidStack findFluid(SmelteryTank<?> tank, AEFluidKey key, int minAmount) {
        for (FluidStack fluid : tank.getFluids()) {
            if (key.matches(fluid) && fluid.getAmount() >= minAmount) {
                return fluid;
            }
        }
        return null;
    }

    @Nullable
    private static FluidStack findFluid(SmelteryTank<?> tank, FluidStack template, int minAmount) {
        for (FluidStack fluid : tank.getFluids()) {
            if (FluidStack.isSameFluidSameComponents(fluid, template) && fluid.getAmount() >= minAmount) {
                return fluid;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------
    // Menu / GUI
    // ------------------------------------------------------------------

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.tinkers_rainbow.seared_pattern_interface");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new SearedPatternInterfaceMenu(id, playerInventory, this);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        if (this.mode != mode) {
            this.mode = mode;
            this.setChanged();
        }
    }

    public boolean isBlocking() {
        return blocking;
    }

    public void setBlocking(boolean blocking) {
        if (this.blocking != blocking) {
            this.blocking = blocking;
            resetBatch();
            this.setChanged();
        }
    }

    public void toggleBlocking() {
        setBlocking(!blocking);
    }

    public boolean isOnline() {
        return online;
    }

    public boolean isFormed() {
        return formed;
    }

    public FeedState getFeedState() {
        return feedState;
    }

    /**
     * Writes the item stack currently placed in the sample slot into the pattern in the pattern slot.
     *
     * @return true when the pattern changed
     */
    public boolean encodePatternFromSample() {
        Level level = getLevel();
        if (level == null) {
            return false;
        }
        ItemStack patternStack = inventory.getStackInSlot(SLOT_PATTERN);
        ItemStack sample = inventory.getStackInSlot(SLOT_SAMPLE);
        if (!SearedPatternItem.isPattern(patternStack) || patternStack.getCount() != 1 || sample.isEmpty()) {
            return false;
        }
        IMeltingContainer meltingContainer = new MeltingInventoryContainer(sample.copyWithCount(1));
        Optional<RecipeHolder<IMeltingRecipe>> holder = level.getRecipeManager()
                .getRecipeFor(TinkerRecipeTypes.MELTING.get(), meltingContainer, level);
        if (holder.isEmpty()) {
            return false;
        }
        IMeltingRecipe recipe = holder.get().value();
        FluidStack output = recipe.getOutput(meltingContainer);
        if (output.isEmpty()) {
            return false;
        }
        AEFluidKey fluidKey = AEFluidKey.of(output);
        AEItemKey itemKey = AEItemKey.of(sample);
        if (fluidKey == null || itemKey == null) {
            return false;
        }
        int count = Math.max(1, sample.getCount());
        SearedPatternData data = SearedPatternItem.getData(patternStack)
                .withInput(new SearedPatternData.MeltingEntry(itemKey, count, fluidKey, output.getAmount()));
        SearedPatternData.CastEntry cast = findBasinCast(level, output);
        if (cast != null) {
            data = data.withCast(cast);
        }
        ItemStack newPattern = patternStack.copy();
        SearedPatternItem.setData(newPattern, data);
        inventory.setStackInSlot(SLOT_PATTERN, newPattern);
        return true;
    }

    private static SearedPatternData.CastEntry findBasinCast(Level level, FluidStack fluid) {
        CastingInventoryContainer container = new CastingInventoryContainer(ItemStack.EMPTY, fluid.copy());
        Optional<RecipeHolder<ICastingRecipe>> holder = level.getRecipeManager()
                .getRecipeFor(TinkerRecipeTypes.CASTING_BASIN.get(), container, level);
        if (holder.isEmpty() || !(holder.get().value() instanceof ItemCastingRecipe recipe)) {
            return null;
        }
        int amount = recipe.getFluidAmount(container);
        ItemStack result = recipe.assemble(container, level.registryAccess());
        if (amount <= 0 || result.isEmpty()) {
            return null;
        }
        AEItemKey resultKey = AEItemKey.of(result);
        AEFluidKey fluidKey = AEFluidKey.of(fluid);
        if (resultKey == null || fluidKey == null) {
            return null;
        }
        return new SearedPatternData.CastEntry(fluidKey, amount, resultKey, result.getCount());
    }

    /** Clears the data of the pattern in the pattern slot, keeping the pattern item itself. */
    public boolean clearPattern() {
        ItemStack patternStack = inventory.getStackInSlot(SLOT_PATTERN);
        if (!SearedPatternItem.isPattern(patternStack)) {
            return false;
        }
        ItemStack newPattern = patternStack.copy();
        SearedPatternItem.setData(newPattern, SearedPatternData.EMPTY);
        inventory.setStackInSlot(SLOT_PATTERN, newPattern);
        return true;
    }

    /** Drops the stored items into the world, used when the block is removed. */
    public void dropContents() {
        Level level = getLevel();
        if (level == null || level.isClientSide) {
            return;
        }
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                Block.popResource(level, worldPosition, stack);
            }
        }
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    // ------------------------------------------------------------------
    // Container implementation (used by the menu)
    // ------------------------------------------------------------------

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
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
    public boolean stillValid(Player player) {
        return player.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D,
                worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
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
        this.mode = Mode.byId(tag.getInt(TAG_MODE));
        this.blocking = tag.getBoolean(TAG_BLOCKING);
        mainNode.loadFromNBT(tag);
        resetBatch();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(TAG_INVENTORY, inventory.serializeNBT(registries));
        tag.putInt(TAG_MODE, this.mode.ordinal());
        tag.putBoolean(TAG_BLOCKING, this.blocking);
        mainNode.saveToNBT(tag);
    }

    // ------------------------------------------------------------------
    // Helper containers
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

    private static final class MeltingInventoryContainer implements IMeltingContainer {

        private final ItemStack stack;

        private MeltingInventoryContainer(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public ItemStack getStack() {
            return stack;
        }

        @Override
        public IMeltingContainer.IOreRate getOreRate() {
            return Config.COMMON.smelteryOreRate;
        }
    }

    private static final class CastingInventoryContainer implements ICastingContainer {

        private final ItemStack stack;
        private final FluidStack fluid;

        private CastingInventoryContainer(ItemStack stack, FluidStack fluid) {
            this.stack = stack;
            this.fluid = fluid;
        }

        @Override
        public ItemStack getStack() {
            return stack;
        }

        @Override
        public Fluid getFluid() {
            return fluid.getFluid();
        }

        @Override
        public FluidStack getFluidStack() {
            return fluid;
        }
    }
}
