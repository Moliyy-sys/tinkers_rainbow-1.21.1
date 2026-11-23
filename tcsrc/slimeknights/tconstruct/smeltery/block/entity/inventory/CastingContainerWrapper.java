/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.material.Fluid
 *  net.neoforged.neoforge.fluids.FluidStack
 */
package slimeknights.tconstruct.smeltery.block.entity.inventory;

import lombok.Generated;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import slimeknights.tconstruct.library.recipe.casting.ICastingContainer;
import slimeknights.tconstruct.smeltery.block.entity.CastingBlockEntity;

public class CastingContainerWrapper
implements ICastingContainer {
    private final CastingBlockEntity tile;
    private FluidStack fluid;
    private boolean switchSlots = false;

    public ItemStack getStack() {
        ItemStack stack = this.tile.getItem(this.switchSlots ? 1 : 0);
        if (stack.is(this.tile.getEmptyCastTag())) {
            return ItemStack.EMPTY;
        }
        return stack;
    }

    @Override
    public Fluid getFluid() {
        return this.fluid.getFluid();
    }

    @Override
    public FluidStack getFluidStack() {
        return this.fluid;
    }

    @Override
    public boolean isEmpty() {
        return this.getStack().isEmpty() && this.fluid.isEmpty();
    }

    public void useInput() {
        this.switchSlots = false;
    }

    public void useOutput() {
        this.switchSlots = true;
    }

    @Generated
    public CastingContainerWrapper(CastingBlockEntity tile) {
        this.tile = tile;
    }

    @Generated
    public void setFluid(FluidStack fluid) {
        this.fluid = fluid;
    }
}
