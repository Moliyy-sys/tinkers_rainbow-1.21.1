package org.moliyy.tinkers_rainbow.integration.ae2;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Menu of the seared pattern interface. The menu exposes three machine slots (pattern, cast, sample),
 * plus a handful of server synchronized values used for the status display and several buttons used to
 * change the mode, toggle blocking mode and write or clear the pattern.
 */
public class SearedPatternInterfaceMenu extends AbstractContainerMenu {

    public static final int BUTTON_MODE_NONE = 0;
    public static final int BUTTON_MODE_BASIN = 1;
    public static final int BUTTON_MODE_TABLE = 2;
    public static final int BUTTON_TOGGLE_BLOCKING = 3;
    public static final int BUTTON_ENCODE_PATTERN = 4;
    public static final int BUTTON_CLEAR_PATTERN = 5;

    public static final int DATA_MODE = 0;
    public static final int DATA_BLOCKING = 1;
    public static final int DATA_ONLINE = 2;
    public static final int DATA_FORMED = 3;
    public static final int DATA_FEED_STATE = 4;
    public static final int DATA_COUNT = 5;

    private final SearedPatternInterfaceBlockEntity blockEntity;
    private final int[] clientData = new int[DATA_COUNT];
    private final ContainerData data;

    public SearedPatternInterfaceMenu(int containerId, Inventory playerInventory,
                                      SearedPatternInterfaceBlockEntity blockEntity) {
        super(Ae2Integration.SEARED_PATTERN_INTERFACE_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                if (isServerSide()) {
                    return readServerValue(index);
                }
                return index >= 0 && index < clientData.length ? clientData[index] : 0;
            }

            @Override
            public void set(int index, int value) {
                if (index >= 0 && index < clientData.length) {
                    clientData[index] = value;
                }
            }

            @Override
            public int getCount() {
                return DATA_COUNT;
            }
        };
        this.addDataSlots(this.data);

        this.addSlot(new Slot(blockEntity, SearedPatternInterfaceBlockEntity.SLOT_PATTERN, 8, 60) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return SearedPatternItem.isPattern(stack);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        this.addSlot(new Slot(blockEntity, SearedPatternInterfaceBlockEntity.SLOT_CAST, 32, 60) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isTableMode();
            }
        });
        this.addSlot(new Slot(blockEntity, SearedPatternInterfaceBlockEntity.SLOT_SAMPLE, 56, 60));

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 116 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(playerInventory, column, 8 + column * 18, 174));
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

    public SearedPatternInterfaceBlockEntity getBlockEntity() {
        return blockEntity;
    }

    private boolean isServerSide() {
        return blockEntity.getLevel() != null && !blockEntity.getLevel().isClientSide;
    }

    private int readServerValue(int index) {
        return switch (index) {
            case DATA_MODE -> blockEntity.getMode().ordinal();
            case DATA_BLOCKING -> blockEntity.isBlocking() ? 1 : 0;
            case DATA_ONLINE -> blockEntity.isOnline() ? 1 : 0;
            case DATA_FORMED -> blockEntity.isFormed() ? 1 : 0;
            case DATA_FEED_STATE -> blockEntity.getFeedState().ordinal();
            default -> 0;
        };
    }

    // ------------------------------------------------------------------
    // Screen accessors
    // ------------------------------------------------------------------

    public int getModeId() {
        return this.data.get(DATA_MODE);
    }

    public boolean isTableMode() {
        return getModeId() == SearedPatternInterfaceBlockEntity.Mode.TABLE.ordinal();
    }

    public boolean isBlocking() {
        return this.data.get(DATA_BLOCKING) != 0;
    }

    public boolean isOnline() {
        return this.data.get(DATA_ONLINE) != 0;
    }

    public boolean isFormed() {
        return this.data.get(DATA_FORMED) != 0;
    }

    public int getFeedStateId() {
        return this.data.get(DATA_FEED_STATE);
    }

    // ------------------------------------------------------------------
    // Button handling
    // ------------------------------------------------------------------

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (!isServerSide()) {
            return false;
        }
        return switch (id) {
            case BUTTON_MODE_NONE -> {
                blockEntity.setMode(SearedPatternInterfaceBlockEntity.Mode.NONE);
                yield true;
            }
            case BUTTON_MODE_BASIN -> {
                blockEntity.setMode(SearedPatternInterfaceBlockEntity.Mode.BASIN);
                yield true;
            }
            case BUTTON_MODE_TABLE -> {
                blockEntity.setMode(SearedPatternInterfaceBlockEntity.Mode.TABLE);
                yield true;
            }
            case BUTTON_TOGGLE_BLOCKING -> {
                blockEntity.toggleBlocking();
                yield true;
            }
            case BUTTON_ENCODE_PATTERN -> {
                if (blockEntity.encodePatternFromSample()) {
                    player.displayClientMessage(Component.translatable(
                            "gui.tinkers_rainbow.seared_pattern_interface.encoded"), true);
                } else {
                    player.displayClientMessage(Component.translatable(
                            "gui.tinkers_rainbow.seared_pattern_interface.encode_failed"), true);
                }
                yield true;
            }
            case BUTTON_CLEAR_PATTERN -> {
                if (blockEntity.clearPattern()) {
                    player.displayClientMessage(Component.translatable(
                            "gui.tinkers_rainbow.seared_pattern_interface.cleared"), true);
                }
                yield true;
            }
            default -> false;
        };
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
        } else if (SearedPatternItem.isPattern(stack)) {
            if (!this.moveItemStackTo(stack, SearedPatternInterfaceBlockEntity.SLOT_PATTERN,
                    SearedPatternInterfaceBlockEntity.SLOT_PATTERN + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(stack, SearedPatternInterfaceBlockEntity.SLOT_CAST,
                SearedPatternInterfaceBlockEntity.SLOT_COUNT, false)) {
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
