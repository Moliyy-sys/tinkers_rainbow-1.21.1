/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.level.material.Fluid
 *  net.neoforged.neoforge.fluids.FluidStack
 *  slimeknights.mantle.recipe.container.ISingleStackContainer
 *  slimeknights.tconstruct.library.utils.FluidStackUtil
 */
package slimeknights.tconstruct.library.recipe.casting;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import slimeknights.mantle.recipe.container.ISingleStackContainer;
import slimeknights.tconstruct.library.utils.FluidStackUtil;

public interface ICastingContainer
extends ISingleStackContainer {
    default public boolean isEmpty() {
        return this.getStack().isEmpty() && this.getFluidStack().isEmpty();
    }

    public Fluid getFluid();

    public FluidStack getFluidStack();

    @Nullable
    default public CompoundTag getFluidTag() {
        return FluidStackUtil.getTag((FluidStack)this.getFluidStack());
    }
}
