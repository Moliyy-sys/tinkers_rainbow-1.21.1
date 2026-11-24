package org.moliyy.tinkers_rainbow.cooler;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import org.moliyy.tinkers_rainbow.smeltery.AbstractSearedOutputBlockEntity;
import org.moliyy.tinkers_rainbow.smeltery.ISearedControlMenu;

/**
 * Menu of the high speed cooler: one cast input slot, 27 output slots, the shared control data and the
 * player inventory.
 */
public class HighSpeedCoolerMenu extends AbstractContainerMenu implements ISearedControlMenu {

    public static final int OUTPUT_COLUMNS = 9;
    public static final int OUTPUT_ROWS = 3;

    private final HighSpeedCoolerBlockEntity blockEntity;
    private final ContainerData data;

    public HighSpeedCoolerMenu(int containerId, Inventory playerInventory,
                               HighSpeedCoolerBlockEntity blockEntity) {
        super(CoolerRegistry.HIGH_SPEED_COOLER_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.data = blockEntity.createContainerData();
        this.addDataSlots(this.data);

        // cast input slot
        this.addSlot(new Slot(blockEntity, HighSpeedCoolerBlockEntity.CAST_SLOT, 8, 78));

        // 27 output slots (players may take items out)
        for (int row = 0; row < OUTPUT_ROWS; row++) {
            for (int column = 0; column < OUTPUT_COLUMNS; column++) {
                int index = 1 + column + row * OUTPUT_COLUMNS;
                this.addSlot(new Slot(blockEntity, index, 8 + column * 18, 18 + row * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }
                });
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 176 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(playerInventory, column, 8 + column * 18, 234));
        }
    }

    /** Client side constructor. */
    public HighSpeedCoolerMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buffer) {
        this(containerId, playerInventory, resolveBlockEntity(playerInventory, buffer));
    }

    private static HighSpeedCoolerBlockEntity resolveBlockEntity(Inventory playerInventory,
                                                                 RegistryFriendlyByteBuf buffer) {
        var pos = buffer.readBlockPos();
        var blockEntity = playerInventory.player.level().getBlockEntity(pos);
        if (blockEntity instanceof HighSpeedCoolerBlockEntity cooler) {
            return cooler;
        }
        throw new IllegalStateException("High speed cooler missing at " + pos);
    }

    @Override
    public AbstractSearedOutputBlockEntity getSearedOutputEntity() {
        return blockEntity;
    }

    @Override
    public int getMenuData(int index) {
        return this.data.get(index);
    }

    public HighSpeedCoolerBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack result = stack.copy();
        int machineSlots = HighSpeedCoolerBlockEntity.SLOT_COUNT;
        if (index < machineSlots) {
            if (!this.moveItemStackTo(stack, machineSlots, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(stack, HighSpeedCoolerBlockEntity.CAST_SLOT,
                HighSpeedCoolerBlockEntity.CAST_SLOT + 1, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == result.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stack);
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.canInteractWithBlock(blockEntity.getBlockPos(), 4.0D);
    }
}
