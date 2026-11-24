package org.moliyy.tinkers_rainbow.smeltery.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import org.moliyy.tinkers_rainbow.smeltery.AbstractSearedOutputBlockEntity;
import org.moliyy.tinkers_rainbow.smeltery.ISearedControlMenu;
import org.moliyy.tinkers_rainbow.smeltery.OutputMode;
import org.moliyy.tinkers_rainbow.smeltery.SearedControlPayload;
import org.moliyy.tinkers_rainbow.smeltery.SearedMenuData;
import org.moliyy.tinkers_rainbow.smeltery.TransferMode;

/**
 * Shared screen logic for the blocks with seared output controls.
 *
 * <p>It provides the numeric limit input box (with the MAX / apply buttons and the click-and-type fix),
 * the output-mode and transfer-mode buttons, the manual return button and the shared status lines. All
 * values are read from the menu's {@link net.minecraft.world.inventory.ContainerData} so the client always
 * sees the server state.</p>
 */
public abstract class AbstractSearedOutputScreen<M extends AbstractContainerMenu & ISearedControlMenu>
        extends AbstractContainerScreen<M> {

    private static final int BUTTON_WIDTH = 52;
    private static final int BUTTON_HEIGHT = 18;

    protected EditBox limitBox;
    private int displayedLimit = Integer.MIN_VALUE;
    private boolean limitFocusedLast = false;
    private Button outputCycleButton;
    private Button transferCycleButton;
    private OutputMode[] outputCycleModes = OutputMode.values();

    protected AbstractSearedOutputScreen(M menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    // ------------------------------------------------------------------
    // control widgets
    // ------------------------------------------------------------------

    /** Adds the output-mode row (fluid / basin / table) at the given position. */
    protected void addOutputModeButtons(int x, int y) {
        this.addRenderableWidget(Button.builder(modeLabel(OutputMode.FLUID),
                        button -> sendControl(AbstractSearedOutputBlockEntity.ACTION_SET_OUTPUT_MODE,
                                OutputMode.FLUID.ordinal()))
                .bounds(x + 8, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());
        this.addRenderableWidget(Button.builder(modeLabel(OutputMode.BASIN),
                        button -> sendControl(AbstractSearedOutputBlockEntity.ACTION_SET_OUTPUT_MODE,
                                OutputMode.BASIN.ordinal()))
                .bounds(x + 62, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());
        this.addRenderableWidget(Button.builder(modeLabel(OutputMode.TABLE),
                        button -> sendControl(AbstractSearedOutputBlockEntity.ACTION_SET_OUTPUT_MODE,
                                OutputMode.TABLE.ordinal()))
                .bounds(x + 116, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());
    }

    /** Adds the transfer-mode row (auto / block / manual) at the given position. */
    protected void addTransferModeButtons(int x, int y) {
        this.addRenderableWidget(Button.builder(transferLabel(TransferMode.AUTO),
                        button -> sendControl(AbstractSearedOutputBlockEntity.ACTION_SET_TRANSFER_MODE,
                                TransferMode.AUTO.ordinal()))
                .bounds(x + 8, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());
        this.addRenderableWidget(Button.builder(transferLabel(TransferMode.BLOCK),
                        button -> sendControl(AbstractSearedOutputBlockEntity.ACTION_SET_TRANSFER_MODE,
                                TransferMode.BLOCK.ordinal()))
                .bounds(x + 62, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());
        this.addRenderableWidget(Button.builder(transferLabel(TransferMode.MANUAL),
                        button -> sendControl(AbstractSearedOutputBlockEntity.ACTION_SET_TRANSFER_MODE,
                                TransferMode.MANUAL.ordinal()))
                .bounds(x + 116, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());
    }

    /** Adds a single button that cycles through the given output kinds. */
    protected void addOutputModeCycleButton(int x, int y, int width, OutputMode... modes) {
        this.outputCycleModes = modes.length == 0 ? OutputMode.values() : modes;
        this.outputCycleButton = this.addRenderableWidget(Button.builder(Component.empty(), button -> {
            OutputMode current = OutputMode.byId(getMenu().getMenuData(SearedMenuData.OUTPUT_MODE));
            int index = 0;
            for (int i = 0; i < outputCycleModes.length; i++) {
                if (outputCycleModes[i] == current) {
                    index = i;
                    break;
                }
            }
            OutputMode next = outputCycleModes[(index + 1) % outputCycleModes.length];
            sendControl(AbstractSearedOutputBlockEntity.ACTION_SET_OUTPUT_MODE, next.ordinal());
        }).bounds(x, y, width, BUTTON_HEIGHT).build());
    }

    /** Adds a single button that cycles auto -> block -> manual. */
    protected void addTransferModeCycleButton(int x, int y, int width) {
        this.transferCycleButton = this.addRenderableWidget(Button.builder(Component.empty(), button -> {
            TransferMode current = TransferMode.byId(getMenu().getMenuData(SearedMenuData.TRANSFER_MODE));
            TransferMode next = TransferMode.values()[(current.ordinal() + 1) % TransferMode.values().length];
            sendControl(AbstractSearedOutputBlockEntity.ACTION_SET_TRANSFER_MODE, next.ordinal());
        }).bounds(x, y, width, BUTTON_HEIGHT).build());
    }

    /** Adds the manual "return once" button. */
    protected void addManualButton(int x, int y, int width) {
        addManualButton(x, y, width, "gui.tinkers_rainbow.seared_output.manual");
    }

    /** Adds the manual "return once" button with a custom translation key. */
    protected void addManualButton(int x, int y, int width, String translationKey) {
        this.addRenderableWidget(Button.builder(
                        Component.translatable(translationKey),
                        button -> sendControl(AbstractSearedOutputBlockEntity.ACTION_MANUAL_TRANSFER, 0))
                .bounds(x, y, width, BUTTON_HEIGHT).build());
    }

    /** Adds the numeric input box plus MAX and apply buttons. */
    protected void addLimitBox(int x, int y) {
        this.limitBox = new EditBox(this.font, x + 8, y, 60, 16,
                Component.translatable("gui.tinkers_rainbow.seared_output.limit"));
        this.limitBox.setMaxLength(10);
        // tolerant filter: digits and MAX are accepted, transient states while editing do not get eaten
        this.limitBox.setFilter(text -> text.length() <= 10 && text.matches("(?i)[0-9max]*"));
        this.limitBox.setValue("0");
        this.addRenderableWidget(this.limitBox);

        this.addRenderableWidget(Button.builder(
                        Component.translatable("gui.tinkers_rainbow.seared_output.limit.max"),
                        button -> applyLimit(AbstractSearedOutputBlockEntity.LIMIT_UNLIMITED))
                .bounds(x + 72, y - 1, 34, BUTTON_HEIGHT).build());
        this.addRenderableWidget(Button.builder(
                        Component.translatable("gui.tinkers_rainbow.seared_output.limit.apply"),
                        button -> applyLimit(parseLimit()))
                .bounds(x + 110, y - 1, 58, BUTTON_HEIGHT).build());
    }

    protected void sendControl(int action, int value) {
        PacketDistributor.sendToServer(new SearedControlPayload(action, value));
    }

    // ------------------------------------------------------------------
    // limit box logic (includes the MAX fix)
    // ------------------------------------------------------------------

    /** Reads the box defensively: anything that is not a non-negative integer becomes 0. */
    protected int parseLimit() {
        if (this.limitBox == null) {
            return AbstractSearedOutputBlockEntity.LIMIT_PAUSED;
        }
        String text = this.limitBox.getValue().trim();
        if (text.isEmpty()) {
            return AbstractSearedOutputBlockEntity.LIMIT_PAUSED;
        }
        if (text.equalsIgnoreCase("max")) {
            return AbstractSearedOutputBlockEntity.LIMIT_UNLIMITED;
        }
        try {
            long value = Long.parseLong(text);
            if (value <= 0L) {
                return AbstractSearedOutputBlockEntity.LIMIT_PAUSED;
            }
            if (value >= AbstractSearedOutputBlockEntity.MAX_ENTERABLE_LIMIT) {
                return AbstractSearedOutputBlockEntity.MAX_ENTERABLE_LIMIT;
            }
            return (int) value;
        } catch (NumberFormatException ignored) {
            return AbstractSearedOutputBlockEntity.LIMIT_PAUSED;
        }
    }

    private void applyLimit(int limit) {
        int safe = Math.max(limit, 0);
        this.displayedLimit = safe;
        if (this.limitBox != null) {
            if (safe == AbstractSearedOutputBlockEntity.LIMIT_UNLIMITED) {
                // show MAX and select the whole text so typing a number replaces it immediately
                this.limitBox.setValue("MAX");
                this.limitBox.setFocused(true);
                this.limitBox.moveCursorToEnd(false);
                this.limitBox.setHighlightPos(0);
            } else {
                this.limitBox.setValue(Integer.toString(safe));
            }
        }
        sendControl(AbstractSearedOutputBlockEntity.ACTION_SET_LIMIT, safe);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.limitBox != null && this.limitBox.isFocused()
                && (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER)) {
            applyLimit(parseLimit());
            this.limitBox.setFocused(false);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (this.limitBox == null) {
            return;
        }
        boolean focused = this.limitBox.isFocused();
        // when the box gains focus while showing the MAX placeholder, select everything so typing replaces it
        if (focused && !this.limitFocusedLast && this.limitBox.getValue().equalsIgnoreCase("max")) {
            this.limitBox.moveCursorToEnd(false);
            this.limitBox.setHighlightPos(0);
        }
        this.limitFocusedLast = focused;

        if (this.outputCycleButton != null) {
            OutputMode mode = OutputMode.byId(getMenu().getMenuData(SearedMenuData.OUTPUT_MODE));
            this.outputCycleButton.setMessage(Component.translatable(
                    "gui.tinkers_rainbow.seared_output.output_mode", modeLabel(mode)));
        }
        if (this.transferCycleButton != null) {
            TransferMode mode = TransferMode.byId(getMenu().getMenuData(SearedMenuData.TRANSFER_MODE));
            this.transferCycleButton.setMessage(Component.translatable(
                    "gui.tinkers_rainbow.seared_output.transfer_mode", transferLabel(mode)));
        }

        int limit = getMenu().getMenuData(SearedMenuData.OUTPUT_LIMIT);
        if (!focused && limit != this.displayedLimit) {
            this.displayedLimit = limit;
            this.limitBox.setValue(limit == AbstractSearedOutputBlockEntity.LIMIT_UNLIMITED
                    ? "MAX"
                    : Integer.toString(Math.max(limit, 0)));
        }
    }

    // ------------------------------------------------------------------
    // labels / status (shared)
    // ------------------------------------------------------------------

    public static Component modeLabel(OutputMode mode) {
        String key = switch (mode) {
            case FLUID -> "gui.tinkers_rainbow.seared_output.mode.fluid";
            case BASIN -> "gui.tinkers_rainbow.seared_output.mode.basin";
            case TABLE -> "gui.tinkers_rainbow.seared_output.mode.table";
        };
        return Component.translatable(key);
    }

    public static Component transferLabel(TransferMode mode) {
        String key = switch (mode) {
            case AUTO -> "gui.tinkers_rainbow.seared_output.transfer.auto";
            case BLOCK -> "gui.tinkers_rainbow.seared_output.transfer.block";
            case MANUAL -> "gui.tinkers_rainbow.seared_output.transfer.manual";
        };
        return Component.translatable(key);
    }

    /** Draws only the limit + transfer status lines (used by the compact cooler GUI). */
    protected void drawLimitStatus(GuiGraphics guiGraphics, int x, int y) {
        int limit = getMenu().getMenuData(SearedMenuData.OUTPUT_LIMIT);
        int transfer = getMenu().getMenuData(SearedMenuData.TRANSFER_MODE);
        Component limitLine;
        int color;
        if (limit == AbstractSearedOutputBlockEntity.LIMIT_PAUSED) {
            limitLine = Component.translatable("gui.tinkers_rainbow.seared_output.status.paused");
            color = 0xFFAA00;
        } else if (limit == AbstractSearedOutputBlockEntity.LIMIT_UNLIMITED) {
            limitLine = Component.translatable("gui.tinkers_rainbow.seared_output.status.unlimited");
            color = 0x55FF55;
        } else {
            limitLine = Component.translatable("gui.tinkers_rainbow.seared_output.status.limited", limit);
            color = 0x55FFFF;
        }
        guiGraphics.drawString(this.font, limitLine, x, y, color, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.tinkers_rainbow.seared_output.status.transfer",
                transferLabel(TransferMode.byId(transfer))), x, y + 10, 0xAAAAAA, false);
    }

    /** Draws the network/formed/limit/transfer status block. */
    protected void drawStatus(GuiGraphics guiGraphics, int x, int y) {
        int online = getMenu().getMenuData(SearedMenuData.ONLINE);
        int formed = getMenu().getMenuData(SearedMenuData.FORMED);
        int limit = getMenu().getMenuData(SearedMenuData.OUTPUT_LIMIT);
        int transfer = getMenu().getMenuData(SearedMenuData.TRANSFER_MODE);

        guiGraphics.drawString(this.font, Component.translatable(online != 0
                        ? "gui.tinkers_rainbow.seared_output.status.online"
                        : "gui.tinkers_rainbow.seared_output.status.offline"),
                x, y, online != 0 ? 0x55FF55 : 0xFF5555, false);
        guiGraphics.drawString(this.font, Component.translatable(formed != 0
                        ? "gui.tinkers_rainbow.seared_output.status.formed"
                        : "gui.tinkers_rainbow.seared_output.status.unformed"),
                x, y + 10, formed != 0 ? 0x55FF55 : 0xAAAAAA, false);

        Component limitLine;
        int color;
        if (limit == AbstractSearedOutputBlockEntity.LIMIT_PAUSED) {
            limitLine = Component.translatable("gui.tinkers_rainbow.seared_output.status.paused");
            color = 0xFFAA00;
        } else if (limit == AbstractSearedOutputBlockEntity.LIMIT_UNLIMITED) {
            limitLine = Component.translatable("gui.tinkers_rainbow.seared_output.status.unlimited");
            color = 0x55FF55;
        } else {
            limitLine = Component.translatable("gui.tinkers_rainbow.seared_output.status.limited", limit);
            color = 0x55FFFF;
        }
        guiGraphics.drawString(this.font, limitLine, x, y + 20, color, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.tinkers_rainbow.seared_output.status.transfer",
                transferLabel(TransferMode.byId(transfer))), x, y + 30, 0xAAAAAA, false);
    }
}
