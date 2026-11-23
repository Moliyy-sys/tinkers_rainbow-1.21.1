/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.neoforge.fluids.FluidStack
 *  net.neoforged.neoforge.fluids.capability.IFluidHandler
 *  slimeknights.tconstruct.smeltery.block.entity.tank.ISmelteryTankHandler$FluidChange
 */
package slimeknights.tconstruct.smeltery.block.entity.tank;

import java.util.List;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import slimeknights.tconstruct.smeltery.block.entity.tank.ISmelteryTankHandler;
import slimeknights.tconstruct.smeltery.block.entity.tank.SmelteryTank;

public interface ISmelteryTankHandler {
    public void updateFluidsFromPacket(List<FluidStack> var1);

    public SmelteryTank<?> getTank();

    public IFluidHandler getFluidHandler();

    default public void notifyFluidsChanged(FluidChange type, FluidStack fluid) {
    }
}
