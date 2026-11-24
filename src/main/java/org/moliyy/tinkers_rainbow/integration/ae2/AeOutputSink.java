package org.moliyy.tinkers_rainbow.integration.ae2;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.MEStorage;

import org.moliyy.tinkers_rainbow.smeltery.OutputSink;

/**
 * {@link OutputSink} that writes instant smeltery output directly into the ME network.
 */
public final class AeOutputSink implements OutputSink {

    private final MEStorage storage;
    private final IActionSource source;

    public AeOutputSink(MEStorage storage, IActionSource source) {
        this.storage = storage;
        this.source = source;
    }

    @Override
    public long insertFluid(FluidStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        AEFluidKey key = AEFluidKey.of(stack);
        if (key == null) {
            return 0;
        }
        long accepted = this.storage.insert(key, stack.getAmount(), Actionable.SIMULATE, this.source);
        if (accepted <= 0) {
            return 0;
        }
        return this.storage.insert(key, accepted, Actionable.MODULATE, this.source);
    }

    @Override
    public int insertItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        AEItemKey key = AEItemKey.of(stack);
        if (key == null) {
            return 0;
        }
        long accepted = this.storage.insert(key, stack.getCount(), Actionable.SIMULATE, this.source);
        if (accepted <= 0) {
            return 0;
        }
        return (int) Math.min(this.storage.insert(key, accepted, Actionable.MODULATE, this.source),
                Integer.MAX_VALUE);
    }
}
