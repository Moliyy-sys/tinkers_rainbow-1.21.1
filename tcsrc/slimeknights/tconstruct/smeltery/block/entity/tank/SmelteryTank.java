/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  javax.annotation.Nonnull
 *  lombok.Generated
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.neoforged.neoforge.fluids.FluidStack
 *  net.neoforged.neoforge.fluids.capability.IFluidHandler
 *  net.neoforged.neoforge.fluids.capability.IFluidHandler$FluidAction
 *  slimeknights.mantle.block.entity.MantleBlockEntity
 *  slimeknights.tconstruct.common.network.TinkerNetwork
 *  slimeknights.tconstruct.library.fluid.IMultitankListChange
 *  slimeknights.tconstruct.library.utils.WeakListenerList
 *  slimeknights.tconstruct.smeltery.block.entity.tank.ISmelteryTankHandler
 *  slimeknights.tconstruct.smeltery.block.entity.tank.ISmelteryTankHandler$FluidChange
 *  slimeknights.tconstruct.smeltery.network.SmelteryTankUpdatePacket
 */
package slimeknights.tconstruct.smeltery.block.entity.tank;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import slimeknights.mantle.block.entity.MantleBlockEntity;
import slimeknights.tconstruct.common.network.TinkerNetwork;
import slimeknights.tconstruct.library.fluid.IMultitankListChange;
import slimeknights.tconstruct.library.utils.WeakListenerList;
import slimeknights.tconstruct.smeltery.block.entity.tank.ISmelteryTankHandler;
import slimeknights.tconstruct.smeltery.network.SmelteryTankUpdatePacket;

public class SmelteryTank<T extends MantleBlockEntity>
implements IFluidHandler,
IMultitankListChange {
    private final T parent;
    private final List<FluidStack> fluids;
    private int capacity = 0;
    private int contained = 0;
    private final WeakListenerList tankListChange = new WeakListenerList();
    private static final String TAG_FLUIDS = "fluids";
    private static final String TAG_CAPACITY = "capacity";

    public SmelteryTank(T parent) {
        this.fluids = Lists.newArrayList();
        this.parent = parent;
    }

    public void syncFluids() {
        Level world = this.parent.getLevel();
        if (world != null && !world.isClientSide) {
            BlockPos pos = this.parent.getBlockPos();
            TinkerNetwork.getInstance().sendToClientsAround((Object)new SmelteryTankUpdatePacket(pos, this.fluids), (LevelAccessor)world, pos);
        }
    }

    public void setCapacity(int maxCapacity) {
        this.capacity = maxCapacity;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getRemainingSpace() {
        if (this.contained >= this.capacity) {
            return 0;
        }
        return this.capacity - this.contained;
    }

    public boolean isFluidValid(int tank, FluidStack stack) {
        return true;
    }

    public int getTanks() {
        if (this.contained < this.capacity) {
            return this.fluids.size() + 1;
        }
        return this.fluids.size();
    }

    @Nonnull
    public FluidStack getFluidInTank(int tank) {
        if (tank < 0 || tank >= this.fluids.size()) {
            return FluidStack.EMPTY;
        }
        return this.fluids.get(tank);
    }

    public int getTankCapacity(int tank) {
        if (tank < 0) {
            return 0;
        }
        int remaining = this.capacity - this.contained;
        if (tank == this.fluids.size()) {
            return remaining;
        }
        return this.fluids.get(tank).getAmount() + remaining;
    }

    public void moveFluidToBottom(int index) {
        if (index < this.fluids.size()) {
            FluidStack fluid = this.fluids.get(index);
            this.fluids.remove(index);
            this.fluids.add(0, fluid);
            ((ISmelteryTankHandler)this.parent).notifyFluidsChanged(ISmelteryTankHandler.FluidChange.CHANGED, FluidStack.EMPTY);
            this.tankListChange.run();
        }
    }

    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        if (this.contained >= this.capacity || resource.isEmpty()) {
            return 0;
        }
        int usable = Math.min(this.capacity - this.contained, resource.getAmount());
        if (usable <= 0) {
            return 0;
        }
        if (action.simulate()) {
            return usable;
        }
        this.contained += usable;
        for (FluidStack fluid : this.fluids) {
            if (!fluid.isFluidEqual(resource)) continue;
            fluid.grow(usable);
            ((ISmelteryTankHandler)this.parent).notifyFluidsChanged(ISmelteryTankHandler.FluidChange.CHANGED, fluid);
            if (this.contained >= this.capacity) {
                this.tankListChange.run();
            }
            return usable;
        }
        resource = resource.copy();
        resource.setAmount(usable);
        this.fluids.add(resource);
        ((ISmelteryTankHandler)this.parent).notifyFluidsChanged(ISmelteryTankHandler.FluidChange.ADDED, resource);
        this.tankListChange.run();
        return usable;
    }

    @Nonnull
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        if (this.fluids.isEmpty()) {
            return FluidStack.EMPTY;
        }
        boolean wasFull = this.contained >= this.capacity;
        FluidStack fluid = this.fluids.get(0);
        int drainable = Math.min(maxDrain, fluid.getAmount());
        FluidStack ret = fluid.copy();
        ret.setAmount(drainable);
        if (action.execute()) {
            fluid.shrink(drainable);
            this.contained -= drainable;
            if (fluid.getAmount() <= 0) {
                this.fluids.remove(fluid);
                ((ISmelteryTankHandler)this.parent).notifyFluidsChanged(ISmelteryTankHandler.FluidChange.REMOVED, fluid);
                this.tankListChange.run();
            } else {
                ((ISmelteryTankHandler)this.parent).notifyFluidsChanged(ISmelteryTankHandler.FluidChange.CHANGED, fluid);
                if (wasFull && this.contained < this.capacity) {
                    this.tankListChange.run();
                }
            }
        }
        return ret;
    }

    @Nonnull
    public FluidStack drain(FluidStack toDrain, IFluidHandler.FluidAction action) {
        boolean wasFull = this.contained >= this.capacity;
        ListIterator<FluidStack> iter = this.fluids.listIterator();
        while (iter.hasNext()) {
            FluidStack fluid = iter.next();
            if (!fluid.isFluidEqual(toDrain)) continue;
            int drainable = Math.min(toDrain.getAmount(), fluid.getAmount());
            FluidStack ret = fluid.copy();
            ret.setAmount(drainable);
            if (action.execute()) {
                fluid.shrink(drainable);
                this.contained -= drainable;
                if (fluid.getAmount() <= 0) {
                    iter.remove();
                    ((ISmelteryTankHandler)this.parent).notifyFluidsChanged(ISmelteryTankHandler.FluidChange.REMOVED, fluid);
                    this.tankListChange.run();
                } else {
                    ((ISmelteryTankHandler)this.parent).notifyFluidsChanged(ISmelteryTankHandler.FluidChange.CHANGED, fluid);
                    if (wasFull && this.contained < this.capacity) {
                        this.tankListChange.run();
                    }
                }
            }
            return ret;
        }
        return FluidStack.EMPTY;
    }

    public void setFluids(List<FluidStack> fluids) {
        FluidStack oldFirst = this.getFluidInTank(0);
        this.fluids.clear();
        this.fluids.addAll(fluids);
        this.contained = fluids.stream().mapToInt(FluidStack::getAmount).reduce(0, Integer::sum);
        FluidStack newFirst = this.getFluidInTank(0);
        if (!oldFirst.isFluidEqual(newFirst)) {
            ((ISmelteryTankHandler)this.parent).notifyFluidsChanged(ISmelteryTankHandler.FluidChange.ORDER_CHANGED, newFirst);
            this.tankListChange.run();
        }
    }

    public CompoundTag write(CompoundTag nbt, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (FluidStack liquid : this.fluids) {
            list.add((Object)liquid.save(registries));
        }
        nbt.put(TAG_FLUIDS, (Tag)list);
        nbt.putInt(TAG_CAPACITY, this.capacity);
        return nbt;
    }

    public void read(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = tag.getList(TAG_FLUIDS, 10);
        this.fluids.clear();
        this.contained = 0;
        for (int i = 0; i < list.size(); ++i) {
            CompoundTag fluidTag = list.getCompound(i);
            FluidStack fluid = FluidStack.parseOptional((HolderLookup.Provider)registries, (CompoundTag)fluidTag);
            if (fluid.isEmpty()) continue;
            this.fluids.add(fluid);
            this.contained += fluid.getAmount();
        }
        this.capacity = tag.getInt(TAG_CAPACITY);
    }

    public <TE> void addTankListListener(TE parent, Consumer<TE> listener) {
        this.tankListChange.addListener(parent, listener);
    }

    public void removeTankListListeners(Object parent) {
        this.tankListChange.removeListeners(parent);
    }

    @Generated
    public List<FluidStack> getFluids() {
        return this.fluids;
    }

    @Generated
    public int getContained() {
        return this.contained;
    }
}
