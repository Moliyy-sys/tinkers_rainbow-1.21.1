/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.world.inventory.ContainerData
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.fluids.FluidStack
 *  net.neoforged.neoforge.fluids.capability.IFluidHandler
 *  net.neoforged.neoforge.fluids.capability.IFluidHandler$FluidAction
 *  net.neoforged.neoforge.items.IItemHandlerModifiable
 *  slimeknights.mantle.block.entity.MantleBlockEntity
 *  slimeknights.tconstruct.library.recipe.melting.IMeltingContainer
 *  slimeknights.tconstruct.library.recipe.melting.IMeltingContainer$IOreRate
 *  slimeknights.tconstruct.library.recipe.melting.IMeltingRecipe
 *  slimeknights.tconstruct.smeltery.block.entity.module.MeltingModule
 */
package slimeknights.tconstruct.smeltery.block.entity.module;

import java.util.Arrays;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import slimeknights.mantle.block.entity.MantleBlockEntity;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;
import slimeknights.tconstruct.library.recipe.melting.IMeltingRecipe;
import slimeknights.tconstruct.smeltery.block.entity.module.MeltingModule;

public class MeltingModuleInventory
implements IItemHandlerModifiable {
    private static final int MAX_SIZE = 10920;
    private static final String TAG_SLOT = "slot";
    private static final String TAG_ITEMS = "items";
    private static final String TAG_SIZE = "size";
    private final MantleBlockEntity parent;
    protected final IFluidHandler fluidHandler;
    private MeltingModule[] modules;
    private final boolean strictSize;
    private final IMeltingContainer.IOreRate oreRate;

    public MeltingModuleInventory(MantleBlockEntity parent, IFluidHandler fluidHandler, IMeltingContainer.IOreRate oreRate, int size) {
        this.parent = parent;
        this.fluidHandler = fluidHandler;
        this.modules = new MeltingModule[size];
        this.oreRate = oreRate;
        this.strictSize = size != 0;
    }

    public MeltingModuleInventory(MantleBlockEntity parent, IFluidHandler fluidHandler, IMeltingContainer.IOreRate oreRate) {
        this(parent, fluidHandler, oreRate, 0);
    }

    public int getSlots() {
        return this.modules.length;
    }

    public boolean validSlot(int slot) {
        return slot >= 0 && slot < this.getSlots();
    }

    public int getSlotLimit(int slot) {
        return 1;
    }

    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }

    private boolean hasModule(int slot) {
        return this.validSlot(slot) && this.modules[slot] != null;
    }

    public int getCurrentTime(int slot) {
        return this.hasModule(slot) ? this.modules[slot].getCurrentTime() : 0;
    }

    public int getRequiredTime(int slot) {
        return this.hasModule(slot) ? this.modules[slot].getRequiredTime() : 0;
    }

    public int getRequiredTemp(int slot) {
        return this.hasModule(slot) ? this.modules[slot].getRequiredTemp() : 0;
    }

    public MeltingModule getModule(int slot) {
        if (!this.validSlot(slot)) {
            throw new IndexOutOfBoundsException();
        }
        if (this.modules[slot] == null) {
            this.modules[slot] = new MeltingModule(this.parent, recipe -> this.tryFillTank(slot, (IMeltingRecipe)recipe), this.oreRate, slot);
        }
        return this.modules[slot];
    }

    public void resize(int newSize, Consumer<ItemStack> stackConsumer) {
        if (this.strictSize) {
            throw new IllegalStateException("Cannot resize this melting module inventory");
        }
        if (newSize > 10920) {
            newSize = 10920;
        }
        if (newSize == this.modules.length) {
            return;
        }
        if (newSize < this.modules.length) {
            for (int i = newSize; i < this.modules.length; ++i) {
                if (this.modules[i] == null || this.modules[i].getStack().isEmpty()) continue;
                stackConsumer.accept(this.modules[i].getStack());
            }
        }
        this.modules = Arrays.copyOf(this.modules, newSize);
        this.parent.setChangedFast();
    }

    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        if (this.validSlot(slot) && this.modules[slot] != null) {
            return this.modules[slot].getStack();
        }
        return ItemStack.EMPTY;
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        if (this.validSlot(slot)) {
            if (stack.isEmpty()) {
                if (this.modules[slot] != null) {
                    this.modules[slot].setStack(ItemStack.EMPTY);
                }
            } else {
                if (stack.getCount() > 1) {
                    stack.setCount(1);
                }
                this.getModule(slot).setStack(stack);
            }
        }
    }

    @Nonnull
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (slot < 0 || slot >= this.getSlots()) {
            return stack;
        }
        MeltingModule module = this.getModule(slot);
        boolean canInsert = module.getStack().isEmpty();
        if (!simulate && canInsert) {
            this.setStackInSlot(slot, stack.copyWithCount(1));
        }
        return canInsert ? stack.copyWithCount(stack.getCount() - 1) : stack;
    }

    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        }
        if (!this.validSlot(slot)) {
            return ItemStack.EMPTY;
        }
        ItemStack existing = this.getStackInSlot(slot);
        if (existing.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (simulate) {
            return existing.copy();
        }
        this.setStackInSlot(slot, ItemStack.EMPTY);
        return existing;
    }

    public boolean canHeat(int temperature) {
        for (MeltingModule module : this.modules) {
            if (module == null || !module.canHeatItem(temperature)) continue;
            return true;
        }
        return false;
    }

    protected boolean tryFillTank(int index, IMeltingRecipe recipe) {
        FluidStack fluid = recipe.getOutput((IMeltingContainer)this.getModule(index));
        if (this.fluidHandler.fill(fluid.copy(), IFluidHandler.FluidAction.SIMULATE) == fluid.getAmount()) {
            this.fluidHandler.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
            return true;
        }
        return false;
    }

    public void heatItems(int temperature, int rate) {
        for (MeltingModule module : this.modules) {
            if (module == null) continue;
            module.heatItem(temperature, rate);
        }
    }

    public void coolItems() {
        for (MeltingModule module : this.modules) {
            if (module == null) continue;
            module.coolItem();
        }
    }

    public CompoundTag writeToTag(HolderLookup.Provider registries) {
        CompoundTag nbt = new CompoundTag();
        ListTag list = new ListTag();
        for (int i = 0; i < this.modules.length; ++i) {
            if (this.modules[i] == null || this.modules[i].getStack().isEmpty()) continue;
            CompoundTag moduleTag = this.modules[i].writeToTag(registries);
            moduleTag.putByte(TAG_SLOT, (byte)i);
            list.add((Object)moduleTag);
        }
        if (!list.isEmpty()) {
            nbt.put(TAG_ITEMS, (Tag)list);
        }
        nbt.putByte(TAG_SIZE, (byte)this.modules.length);
        return nbt;
    }

    public void readFromTag(CompoundTag nbt, HolderLookup.Provider registries) {
        int newSize;
        if (!this.strictSize && (newSize = nbt.getByte(TAG_SIZE) & 0xFF) != this.modules.length) {
            this.modules = Arrays.copyOf(this.modules, newSize);
        }
        for (MeltingModule module : this.modules) {
            if (module == null) continue;
            module.setStack(ItemStack.EMPTY);
        }
        ListTag list = nbt.getList(TAG_ITEMS, 10);
        for (int i = 0; i < list.size(); ++i) {
            int slot;
            CompoundTag item = list.getCompound(i);
            if (!item.contains(TAG_SLOT, 1) || !this.validSlot(slot = item.getByte(TAG_SLOT) & 0xFF)) continue;
            this.getModule(slot).readFromTag(item, registries);
        }
    }

    public void trackInts(Consumer<ContainerData> consumer) {
        for (int i = 0; i < this.getSlots(); ++i) {
            consumer.accept((ContainerData)this.getModule(i));
        }
    }
}
