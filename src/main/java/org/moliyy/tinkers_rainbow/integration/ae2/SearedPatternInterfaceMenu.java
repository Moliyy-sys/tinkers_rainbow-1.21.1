package org.moliyy.tinkers_rainbow.integration.ae2;

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
 * Menu of the seared pattern interface.
 *
 * <p>The interface is a pure I/O bridge: one cast slot for the casting-table mode and the shared output
 * controls (output mode, transfer mode and numeric limit). All control data is synchronised through
 * {@link ContainerData} using the shared {@link org.moliyy.tinkers_rainbow.smeltery.SearedMenuData} indices.</p>
 */
public class SearedPatternInterfaceMenu extends AbstractContainerMenu implements ISearedControlMenu {

    private final SearedPatternInterfaceBlockEntity blockEntity;
    private final ContainerData data;

    public SearedPatternInterfaceMenu(int containerId, Inventory playerInventory,
                                      SearedPatternInterfaceBlockEntity blockEntity) {
        super(Ae2Integration.SEARED_PATTERN_INTERFACE_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.data = blockEntity.createContainerData();
        this.addDataSlots(this.data);

        this.addSlot(new Slot(blockEntity, SearedPatternInterfaceBlockEntity.SLOT_CAST, 150, 62));

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 166 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(playerInventory, column, 8 + column * 18, 224));
        }
    }

    /** Client side constructor, the block position is sent as extra data when the menu is opened. */
    public SearedPatternInterfaceMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buffer) {
        this(containerId, playerInventory, resolveBlockEntity(playerInventory, buffer));
    }

    private static SearedPatternInterfaceBlockEntity resolveBlockEntity(Inventory playerInventory,
                                                                        RegistryFriendlyByteBuf buffer) {
        var pos = buffer.readBlockPos();
        var blockEntity = playerInventory.player.level().getBlockEntity(pos);
        if (blockEntity instanceof SearedPatternInterfaceBlockEntity interfaceEntity) {
            return interfaceEntity;
        }
        throw new IllegalStateException("Seared pattern interface missing at " + pos);
    }

    @Override
    public AbstractSearedOutputBlockEntity getSearedOutputEntity() {
        return blockEntity;
    }

    @Override
    public int getMenuData(int index) {
        return this.data.get(index);
    }

    public SearedPatternInterfaceBlockEntity getBlockEntity() {
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
        int machineSlots = SearedPatternInterfaceBlockEntity.SLOT_COUNT;
        if (index < machineSlots) {
            if (!this.moveItemStackTo(stack, machineSlots, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(stack, 0, machineSlots, false)) {
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
