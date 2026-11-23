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
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.WorldlyContainer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.item.crafting.RecipeInput
 *  net.minecraft.world.item.crafting.RecipeManager
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityTicker
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.neoforged.neoforge.fluids.FluidStack
 *  net.neoforged.neoforge.fluids.capability.IFluidHandler
 *  net.neoforged.neoforge.fluids.capability.IFluidHandler$FluidAction
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.ItemHandlerHelper
 *  net.neoforged.neoforge.items.wrapper.SidedInvWrapper
 *  slimeknights.mantle.fluid.FluidTransferHelper
 *  slimeknights.mantle.recipe.helper.RecipeHelper
 *  slimeknights.mantle.util.BlockEntityHelper
 *  slimeknights.tconstruct.TConstruct
 *  slimeknights.tconstruct.common.Sounds
 *  slimeknights.tconstruct.common.network.TinkerNetwork
 *  slimeknights.tconstruct.library.recipe.casting.ICastingContainer
 *  slimeknights.tconstruct.library.recipe.molding.IMoldingContainer
 *  slimeknights.tconstruct.library.recipe.molding.MoldingRecipe
 *  slimeknights.tconstruct.shared.block.entity.TableBlockEntity
 *  slimeknights.tconstruct.smeltery.block.AbstractCastingBlock
 *  slimeknights.tconstruct.smeltery.block.entity.inventory.CastingContainerWrapper
 *  slimeknights.tconstruct.smeltery.block.entity.inventory.MoldingContainerWrapper
 *  slimeknights.tconstruct.smeltery.block.entity.tank.CastingFluidHandler
 *  slimeknights.tconstruct.smeltery.network.FluidUpdatePacket
 *  slimeknights.tconstruct.smeltery.network.FluidUpdatePacket$IFluidPacketReceiver
 */
package slimeknights.tconstruct.smeltery.block.entity;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import slimeknights.mantle.fluid.FluidTransferHelper;
import slimeknights.mantle.recipe.helper.RecipeHelper;
import slimeknights.mantle.util.BlockEntityHelper;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.common.network.TinkerNetwork;
import slimeknights.tconstruct.library.recipe.casting.ICastingContainer;
import slimeknights.tconstruct.library.recipe.casting.ICastingRecipe;
import slimeknights.tconstruct.library.recipe.molding.IMoldingContainer;
import slimeknights.tconstruct.library.recipe.molding.MoldingRecipe;
import slimeknights.tconstruct.shared.block.entity.TableBlockEntity;
import slimeknights.tconstruct.smeltery.block.AbstractCastingBlock;
import slimeknights.tconstruct.smeltery.block.entity.inventory.CastingContainerWrapper;
import slimeknights.tconstruct.smeltery.block.entity.inventory.MoldingContainerWrapper;
import slimeknights.tconstruct.smeltery.block.entity.tank.CastingFluidHandler;
import slimeknights.tconstruct.smeltery.network.FluidUpdatePacket;

public abstract class CastingBlockEntity
extends TableBlockEntity
implements WorldlyContainer,
FluidUpdatePacket.IFluidPacketReceiver {
    public static final int INPUT = 0;
    public static final int OUTPUT = 1;
    private static final String TAG_TANK = "tank";
    private static final String TAG_TIMER = "timer";
    private static final String TAG_RECIPE = "recipe";
    private static final String TAG_REDSTONE = "redstone";
    private static final Component NAME = TConstruct.makeTranslation((String)"gui", (String)"casting");
    public static final BlockEntityTicker<CastingBlockEntity> SERVER_TICKER = (level, pos, state, self) -> self.serverTick(level, pos);
    public static final BlockEntityTicker<CastingBlockEntity> CLIENT_TICKER = (level, pos, state, self) -> self.clientTick(level, pos);
    private final CastingFluidHandler tank = new CastingFluidHandler(this);
    private final RecipeType<ICastingRecipe> castingType;
    private final CastingContainerWrapper castingInventory;
    private int timer;
    private int coolingTime = -1;
    private ICastingRecipe currentRecipe;
    private ResourceLocation recipeName;
    private ICastingRecipe lastCastingRecipe;
    private ItemStack lastOutput = null;
    private final boolean requireCast;
    private final TagKey<Item> emptyCastTag;
    private final RecipeType<MoldingRecipe> moldingType;
    private final MoldingContainerWrapper moldingInventory;
    private MoldingRecipe lastMoldingRecipe;
    private boolean lastRedstone = false;
    private int lastAnalogSignal;

    protected CastingBlockEntity(BlockEntityType<?> beType, BlockPos pos, BlockState state, RecipeType<ICastingRecipe> castingType, RecipeType<MoldingRecipe> moldingType, TagKey<Item> emptyCastTag) {
        super(beType, pos, state, NAME, 2, 1);
        AbstractCastingBlock casting;
        Block block = state.getBlock();
        this.requireCast = block instanceof AbstractCastingBlock && (casting = (AbstractCastingBlock)block).isRequireCast();
        this.emptyCastTag = emptyCastTag;
        this.itemHandler = new SidedInvWrapper((WorldlyContainer)this, Direction.DOWN);
        this.castingType = castingType;
        this.moldingType = moldingType;
        this.castingInventory = new CastingContainerWrapper(this);
        this.moldingInventory = new MoldingContainerWrapper((IItemHandler)this.itemHandler, 0);
    }

    public void interact(Player player, InteractionHand hand) {
        if (this.level == null || this.level.isClientSide || this.coolingTime >= 0 && this.timer > 0) {
            return;
        }
        ItemStack held = player.getItemInHand(hand);
        if (FluidTransferHelper.interactWithContainer((Level)this.level, (BlockPos)this.worldPosition, (IFluidHandler)this.tank, (Player)player, (InteractionHand)hand).didTransfer() || !this.tank.isEmpty()) {
            return;
        }
        ItemStack input = this.getItem(0);
        ItemStack output = this.getItem(1);
        if (!input.isEmpty() && output.isEmpty()) {
            this.moldingInventory.setPattern(held);
            MoldingRecipe recipe = this.findMoldingRecipe();
            if (recipe != null) {
                ItemStack result = recipe.assemble((RecipeInput)this.moldingInventory, this.level.registryAccess());
                result.onCraftedBy(this.level, player, 1);
                if (held.isEmpty()) {
                    this.setItem(0, ItemStack.EMPTY);
                    player.setItemInHand(hand, result);
                } else {
                    this.setItem(0, result);
                    if (!recipe.isPatternConsumed()) {
                        this.setItem(1, held.copyWithCount(1));
                        this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
                    }
                    held.shrink(1);
                    player.setItemInHand(hand, held.isEmpty() ? ItemStack.EMPTY : held);
                }
                this.moldingInventory.setPattern(ItemStack.EMPTY);
                return;
            }
            if (!held.isEmpty()) {
                this.moldingInventory.setPattern(ItemStack.EMPTY);
                recipe = this.findMoldingRecipe();
                if (recipe != null) {
                    ItemStack result = recipe.assemble((RecipeInput)this.moldingInventory, this.level.registryAccess());
                    this.setItem(0, ItemStack.EMPTY);
                    ItemHandlerHelper.giveItemToPlayer((Player)player, (ItemStack)result, (int)player.getInventory().selected);
                    return;
                }
            }
            this.moldingInventory.setPattern(ItemStack.EMPTY);
        }
        if (input.isEmpty() && output.isEmpty()) {
            if (!held.isEmpty()) {
                ItemStack stack = held.split(this.stackSizeLimit);
                player.setItemInHand(hand, held.isEmpty() ? ItemStack.EMPTY : held);
                this.setItem(0, stack);
            }
        } else {
            int slot = output.isEmpty() ? 0 : 1;
            ItemStack stack = this.removeItemNoUpdate(slot);
            ItemHandlerHelper.giveItemToPlayer((Player)player, (ItemStack)stack, (int)player.getInventory().selected);
            if (slot == 1) {
                this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
            }
        }
    }

    public void setItem(int slot, ItemStack stack) {
        ItemStack original = this.getItem(slot);
        super.setItem(slot, stack);
        if (original.isEmpty() != stack.isEmpty()) {
            this.updateAnalogSignal();
        }
        if (this.level != null && !this.level.isClientSide) {
            boolean hasItem = !this.getItem(0).isEmpty() || !this.getItem(1).isEmpty();
            BlockState state = this.getBlockState();
            if ((Boolean)state.getValue((Property)AbstractCastingBlock.HAS_ITEM) != hasItem) {
                this.level.setBlockAndUpdate(this.worldPosition, (BlockState)state.setValue((Property)AbstractCastingBlock.HAS_ITEM, (Comparable)Boolean.valueOf(hasItem)));
            }
        }
    }

    public void handleRedstone(boolean hasSignal) {
        if (this.lastRedstone != hasSignal) {
            if (hasSignal && this.level != null) {
                this.level.scheduleTick(this.worldPosition, this.getBlockState().getBlock(), 2);
            }
            this.lastRedstone = hasSignal;
        }
    }

    public void swap() {
        if (this.currentRecipe == null) {
            ItemStack output = this.getItem(1);
            this.setItem(1, this.getItem(0));
            this.setItem(0, output);
            if (this.level != null) {
                this.level.playSound(null, this.getBlockPos(), Sounds.CASTING_CLICKS.getSound(), SoundSource.BLOCKS, 1.0f, 1.0f);
            }
        }
    }

    @Nonnull
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1};
    }

    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.tank.isEmpty() && index == 0 && !this.isStackInSlot(1);
    }

    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return this.tank.isEmpty() && index == 1;
    }

    private void serverTick(Level level, BlockPos pos) {
        if (this.currentRecipe == null) {
            return;
        }
        FluidStack currentFluid = this.tank.getFluid();
        if (this.coolingTime >= 0) {
            ++this.timer;
            if (this.timer >= this.coolingTime) {
                if (!this.currentRecipe.matches((RecipeInput)this.castingInventory, level)) {
                    this.currentRecipe = this.findCastingRecipe();
                    this.recipeName = null;
                    if (this.currentRecipe == null || this.currentRecipe.getFluidAmount((ICastingContainer)this.castingInventory) > currentFluid.getAmount()) {
                        this.timer = 0;
                        this.updateAnalogSignal();
                        return;
                    }
                }
                boolean consumed = this.currentRecipe.isConsumed((ICastingContainer)this.castingInventory);
                ItemStack output = this.currentRecipe.assemble((RecipeInput)this.castingInventory, level.registryAccess());
                if (this.currentRecipe.switchSlots() != this.lastRedstone) {
                    if (!consumed) {
                        this.setItem(1, this.getItem(0));
                    }
                    this.setItem(0, output);
                } else {
                    if (consumed) {
                        this.setItem(0, ItemStack.EMPTY);
                    }
                    this.setItem(1, output);
                }
                if (this.lastRedstone) {
                    level.playSound(null, this.getBlockPos(), Sounds.CASTING_CLICKS.getSound(), SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                level.playSound(null, pos, Sounds.CASTING_COOLS.getSound(), SoundSource.BLOCKS, 0.5f, 4.0f);
                this.reset();
            } else {
                this.updateAnalogSignal();
            }
        }
    }

    private void clientTick(Level level, BlockPos pos) {
        if (this.currentRecipe == null) {
            return;
        }
        FluidStack currentFluid = this.tank.getFluid();
        if (currentFluid.getAmount() >= this.tank.getCapacity() && !currentFluid.isEmpty()) {
            ++this.timer;
            if (level.random.nextFloat() > 0.9f) {
                level.addParticle((ParticleOptions)ParticleTypes.SMOKE, (double)pos.getX() + level.random.nextDouble(), (double)pos.getY() + 1.1, (double)pos.getZ() + level.random.nextDouble(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Nullable
    private ICastingRecipe findCastingRecipe() {
        if (this.level == null) {
            return null;
        }
        if (this.lastCastingRecipe != null && this.lastCastingRecipe.matches((RecipeInput)this.castingInventory, this.level)) {
            return this.lastCastingRecipe;
        }
        ICastingRecipe castingRecipe = this.level.getRecipeManager().getRecipeFor(this.castingType, (RecipeInput)this.castingInventory, this.level).map(RecipeHolder::value).orElse(null);
        if (castingRecipe != null) {
            this.lastCastingRecipe = castingRecipe;
        }
        return castingRecipe;
    }

    @Nullable
    private MoldingRecipe findMoldingRecipe() {
        if (this.level == null) {
            return null;
        }
        if (this.lastMoldingRecipe != null && this.lastMoldingRecipe.matches((IMoldingContainer)this.moldingInventory, this.level)) {
            return this.lastMoldingRecipe;
        }
        Optional<MoldingRecipe> newRecipe = this.level.getRecipeManager().getRecipeFor(this.moldingType, (RecipeInput)this.moldingInventory, this.level).map(RecipeHolder::value);
        if (newRecipe.isPresent()) {
            this.lastMoldingRecipe = newRecipe.get();
            return this.lastMoldingRecipe;
        }
        return null;
    }

    public int initNewCasting(FluidStack fluid, IFluidHandler.FluidAction action) {
        boolean hasOutput;
        if (this.currentRecipe != null || this.recipeName != null) {
            return 0;
        }
        boolean hasInput = !this.getItem(0).isEmpty();
        boolean bl = hasOutput = !this.getItem(1).isEmpty();
        if (hasInput && hasOutput) {
            return 0;
        }
        this.castingInventory.setFluid(fluid);
        if (!hasOutput) {
            if (!hasInput && this.requireCast) {
                return 0;
            }
            this.castingInventory.useInput();
            ICastingRecipe castingRecipe = this.findCastingRecipe();
            if (castingRecipe != null) {
                if (action == IFluidHandler.FluidAction.EXECUTE) {
                    this.currentRecipe = castingRecipe;
                    this.recipeName = null;
                    this.lastOutput = null;
                }
                return castingRecipe.getFluidAmount((ICastingContainer)this.castingInventory);
            }
        } else {
            this.castingInventory.useOutput();
            ICastingRecipe castingRecipe = this.findCastingRecipe();
            if (castingRecipe != null) {
                if (action == IFluidHandler.FluidAction.EXECUTE) {
                    this.currentRecipe = castingRecipe;
                    this.recipeName = null;
                    this.lastOutput = null;
                    this.setItem(0, this.getItem(1));
                    this.setItem(1, ItemStack.EMPTY);
                    this.castingInventory.useInput();
                }
                return castingRecipe.getFluidAmount((ICastingContainer)this.castingInventory);
            }
        }
        return 0;
    }

    public void reset() {
        this.timer = 0;
        this.currentRecipe = null;
        this.recipeName = null;
        this.lastOutput = null;
        this.castingInventory.setFluid(FluidStack.EMPTY);
        this.tank.reset();
        this.onContentsChanged();
    }

    public void onContentsChanged() {
        FluidStack fluidStack = this.tank.getFluid();
        if (fluidStack.getAmount() >= this.tank.getCapacity() && this.currentRecipe != null) {
            this.castingInventory.setFluid(fluidStack);
            this.coolingTime = Math.max(0, this.currentRecipe.getCoolingTime((ICastingContainer)this.castingInventory));
        } else {
            this.coolingTime = -1;
        }
        this.setChangedFast();
        this.updateAnalogSignal();
        Level world = this.getLevel();
        if (world != null && !world.isClientSide) {
            BlockPos pos = this.getBlockPos();
            TinkerNetwork.getInstance().sendToClientsAround((Object)new FluidUpdatePacket(pos, fluidStack), (LevelAccessor)world, pos);
        }
    }

    public void updateFluidTo(FluidStack fluid) {
        if (fluid.isEmpty()) {
            this.reset();
        } else {
            int capacity = this.initNewCasting(fluid, IFluidHandler.FluidAction.EXECUTE);
            if (capacity > 0) {
                this.tank.setCapacity(capacity);
            }
        }
        this.tank.setFluid(fluid);
        this.onContentsChanged();
    }

    @Nullable
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return null;
    }

    public ItemStack getRecipeOutput() {
        if (this.lastOutput == null) {
            if (this.currentRecipe == null || this.level == null) {
                return ItemStack.EMPTY;
            }
            this.castingInventory.setFluid(this.tank.getFluid());
            this.lastOutput = this.currentRecipe.assemble((RecipeInput)this.castingInventory, this.level.registryAccess());
        }
        return this.lastOutput;
    }

    private void updateAnalogSignal() {
        int newStrength;
        if (!(this.level != null && this.level.isClientSide || (newStrength = this.getAnalogSignal()) == this.lastAnalogSignal)) {
            this.lastAnalogSignal = newStrength;
            if (this.level != null) {
                this.level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
            }
        }
    }

    public int getAnalogSignal() {
        if (this.isStackInSlot(1)) {
            return 15;
        }
        if (this.coolingTime > 0) {
            return 11 + this.timer * 4 / this.coolingTime;
        }
        int capacity = this.tank.getCapacity();
        if (capacity > 0) {
            return 2 + this.tank.getFluid().getAmount() * 9 / capacity;
        }
        if (this.isStackInSlot(0)) {
            return 1;
        }
        return 0;
    }

    private void loadRecipe(Level level, ResourceLocation name) {
        FluidStack fluid = this.tank.getFluid();
        if (!fluid.isEmpty()) {
            RecipeHelper.getRecipe((RecipeManager)level.getRecipeManager(), (ResourceLocation)name, ICastingRecipe.class).ifPresent(recipe -> {
                this.currentRecipe = recipe;
                this.castingInventory.setFluid(fluid);
                this.tank.setCapacity(recipe.getFluidAmount((ICastingContainer)this.castingInventory));
                if (fluid.getAmount() >= this.tank.getCapacity()) {
                    this.coolingTime = recipe.getCoolingTime((ICastingContainer)this.castingInventory);
                }
            });
        }
    }

    public void setLevel(Level pLevel) {
        super.setLevel(pLevel);
        if (this.recipeName != null) {
            this.loadRecipe(pLevel, this.recipeName);
            this.recipeName = null;
        }
    }

    protected void saveAdditional(CompoundTag tags, HolderLookup.Provider registries) {
        super.saveAdditional(tags, registries);
        tags.putBoolean(TAG_REDSTONE, this.lastRedstone);
    }

    protected void saveSynced(CompoundTag tags, HolderLookup.Provider registries) {
        super.saveSynced(tags, registries);
        tags.put(TAG_TANK, (Tag)this.tank.writeToTag(new CompoundTag(), registries));
        if (this.currentRecipe != null || this.recipeName != null) {
            tags.putInt(TAG_TIMER, this.timer);
        }
        if (this.currentRecipe != null) {
            if (this.level != null) {
                this.level.getRecipeManager().getRecipes().stream().filter(holder -> holder.value() == this.currentRecipe).findFirst().ifPresent(holder -> tags.putString(TAG_RECIPE, holder.id().toString()));
            }
        } else if (this.recipeName != null) {
            tags.putString(TAG_RECIPE, this.recipeName.toString());
        }
    }

    public void load(CompoundTag tags, HolderLookup.Provider registries) {
        super.load(tags, registries);
        this.tank.readFromTag(tags.getCompound(TAG_TANK), registries);
        this.timer = tags.getInt(TAG_TIMER);
        if (tags.contains(TAG_RECIPE, 8)) {
            ResourceLocation name = ResourceLocation.parse((String)tags.getString(TAG_RECIPE));
            if (this.level != null) {
                this.loadRecipe(this.level, name);
            } else {
                this.recipeName = name;
            }
        }
        this.lastRedstone = tags.getBoolean(TAG_REDSTONE);
    }

    @Nullable
    public static <CAST extends CastingBlockEntity, RET extends BlockEntity> BlockEntityTicker<RET> getTicker(Level level, BlockEntityType<RET> check, BlockEntityType<CAST> casting) {
        return BlockEntityHelper.castTicker(check, casting, level.isClientSide ? CLIENT_TICKER : SERVER_TICKER);
    }

    @Generated
    public CastingFluidHandler getTank() {
        return this.tank;
    }

    @Generated
    public int getTimer() {
        return this.timer;
    }

    @Generated
    public int getCoolingTime() {
        return this.coolingTime;
    }

    @Generated
    public TagKey<Item> getEmptyCastTag() {
        return this.emptyCastTag;
    }
}
