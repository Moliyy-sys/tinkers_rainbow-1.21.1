package org.moliyy.tinkers_rainbow.smeltery;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Destination of the instant smeltery output. Implemented by the AE bridge (writes into the ME network)
 * and by the high speed cooler (writes into internal item/fluid slots).
 *
 * <p>Both methods must only report what was really accepted; returning a smaller value than offered is
 * how the transfer engine detects a full target and stops producing (lazy output).</p>
 */
public interface OutputSink {

    /** Inserts up to {@code stack.getAmount()} mB and returns the amount that was actually accepted. */
    long insertFluid(FluidStack stack);

    /** Inserts the item stack and returns the number of items that were actually accepted. */
    int insertItem(ItemStack stack);
}
