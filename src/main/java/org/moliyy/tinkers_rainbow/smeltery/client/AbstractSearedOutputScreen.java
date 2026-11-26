package org.moliyy.tinkers_rainbow.smeltery.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import org.moliyy.tinkers_rainbow.Config;
import org.moliyy.tinkers_rainbow.client.GuiTextures;
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

    /** Size of the theme toggle icon in the top right corner of the GUI. */
    protected static final int THEME_BUTTON_SIZE = 16;
    /** Distance of the theme toggle from the right edge of the GUI. */
    protected static final int THEME_BUTTON_RIGHT_OFFSET = 20;

    protected EditBox limitBox;
    private int displayedLimit = Integer.MIN_VALUE;
    private boolean limitFocusedLast = false;
    private Button outputCycleButton;
    private Button transferCycleButton;
    private OutputMode[] outputCycleModes = OutputMode.values();

    /** Widgets added by this screen together with their offset from the GUI origin. */
    private final List<WidgetPlacement> placements = new ArrayList<>();

    private record WidgetPlacement(AbstractWidget widget, int dx, int dy) {
    }

    protected AbstractSearedOutputScreen(M menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    /** File name (without extension) of this screen's background inside {@code textures/gui/<theme>/}. */
    protected abstract String getGuiTextureName();

    /** Vertical offset of the theme toggle; overridable for compact layouts. */
    protected int getThemeButtonTopOffset() {
        return 4;
    }

    /**
     * Keeps the GUI centred and every widget aligned when the window size or the GUI scale changes.
     *
     * <p>Vanilla only re-centres {@code leftPos}/{@code topPos} in {@link #init()}; widgets that were added
     * with absolute coordinates would keep their old position and end up offset from (and overlapping) the
     * background. Recomputing the origin and rebuilding the widgets here makes the whole layout follow the
     * new scale, and also refreshes the edit box text from the synchronised menu data.</p>
     */
    @Override
    protected void repositionElements() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        applyPlacements();
    }

    /** Clears the recorded widget anchors together with the widgets themselves. */
    @Override
    protected void clearWidgets() {
        super.clearWidgets();
        this.placements.clear();
    }

    /** Re-anchors every widget at its offset from the current GUI origin. */
    private void applyPlacements() {
        for (WidgetPlacement placement : this.placements) {
            placement.widget().setX(this.leftPos + placement.dx());
            placement.widget().setY(this.topPos + placement.dy());
        }
    }

    /** Registers a widget with its offset from the GUI origin (instead of an absolute position). */
    private <T extends AbstractWidget> T place(T widget, int dx, int dy) {
        this.placements.add(new WidgetPlacement(widget, dx, dy));
        this.addRenderableWidget(widget);
        return widget;
    }

    // ------------------------------------------------------------------
    // background + theme toggle
    // ------------------------------------------------------------------

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        // resolved on every frame so switching the theme is visible immediately
        guiGraphics.blit(GuiTextures.getGuiTexture(getGuiTextureName()),
                this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // keep the origin in sync with the live screen size and re-anchor all widgets: after a GUI scale
        // or window size change the buttons must scale and move together with the background, whether or
        // not the screen was re-initialised
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        applyPlacements();
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderThemeButton(guiGraphics, mouseX, mouseY);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    protected int themeButtonX() {
        return this.leftPos + this.imageWidth - THEME_BUTTON_RIGHT_OFFSET;
    }

    protected int themeButtonY() {
        return this.topPos + getThemeButtonTopOffset();
    }

    /** Draws the sun (light theme) / moon (dark theme) toggle in the top right corner. */
    private void renderThemeButton(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = themeButtonX();
        int y = themeButtonY();
        boolean dark = GuiTextures.isDarkTheme();

        // small recessed button background so the icon stays readable on both themes
        guiGraphics.fill(x, y, x + THEME_BUTTON_SIZE, y + THEME_BUTTON_SIZE, 0xFF000000);
        guiGraphics.fill(x + 1, y + 1, x + THEME_BUTTON_SIZE - 1, y + THEME_BUTTON_SIZE - 1,
                dark ? 0xFF23232E : 0xFFE2E2EA);

        if (dark) {
            // crescent moon
            guiGraphics.fill(x + 5, y + 3, x + 12, y + 13, 0xFFEDEDFF);
            guiGraphics.fill(x + 3, y + 5, x + 12, y + 11, 0xFFEDEDFF);
            guiGraphics.fill(x + 8, y + 2, x + 14, y + 9, 0xFF23232E);
        } else {
            // sun with rays
            guiGraphics.fill(x + 6, y + 6, x + 10, y + 10, 0xFFFFC93A);
            guiGraphics.fill(x + 7, y + 3, x + 9, y + 5, 0xFFFFC93A);
            guiGraphics.fill(x + 7, y + 11, x + 9, y + 13, 0xFFFFC93A);
            guiGraphics.fill(x + 3, y + 7, x + 5, y + 9, 0xFFFFC93A);
            guiGraphics.fill(x + 11, y + 7, x + 13, y + 9, 0xFFFFC93A);
        }

        if (isOverThemeButton(mouseX, mouseY)) {
            guiGraphics.renderTooltip(this.font,
                    Component.translatable("gui.tinkers_rainbow.theme.toggle"), mouseX, mouseY);
        }
    }

    private boolean isOverThemeButton(double mouseX, double mouseY) {
        int x = themeButtonX();
        int y = themeButtonY();
        return mouseX >= x && mouseX < x + THEME_BUTTON_SIZE
                && mouseY >= y && mouseY < y + THEME_BUTTON_SIZE;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isOverThemeButton(mouseX, mouseY)) {
            toggleTheme();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    /** Flips the theme config and saves it; the next frame already renders the other texture set. */
    protected void toggleTheme() {
        boolean dark = !GuiTextures.isDarkTheme();
        try {
            Config.COMMON.useDarkGui.set(dark);
            Config.SPEC.save();
        } catch (Throwable ignored) {
            // config not loaded (should not happen for an open screen), keep rendering with the old value
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            minecraft.player.displayClientMessage(Component.translatable(
                    "gui.tinkers_rainbow.theme.changed",
                    Component.translatable(dark
                            ? "gui.tinkers_rainbow.theme.dark"
                            : "gui.tinkers_rainbow.theme.light")), true);
        }
    }

    // ------------------------------------------------------------------
    // control widgets
    // ------------------------------------------------------------------

    /** Adds the output-mode row (fluid / basin / table) at the given offset from the GUI origin. */
    protected void addOutputModeButtons(int rx, int ry) {
        place(Button.builder(modeLabel(OutputMode.FLUID),
                        button -> sendControl(AbstractSearedOutputBlockEntity.ACTION_SET_OUTPUT_MODE,
                                OutputMode.FLUID.ordinal()))
                .bounds(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT).build(), rx + 8, ry);
        place(Button.builder(modeLabel(OutputMode.BASIN),
                        button -> sendControl(AbstractSearedOutputBlockEntity.ACTION_SET_OUTPUT_MODE,
                                OutputMode.BASIN.ordinal()))
                .bounds(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT).build(), rx + 62, ry);
        place(Button.builder(modeLabel(OutputMode.TABLE),
                        button -> sendControl(AbstractSearedOutputBlockEntity.ACTION_SET_OUTPUT_MODE,
                                OutputMode.TABLE.ordinal()))
                .bounds(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT).build(), rx + 116, ry);
    }

    /** Adds the transfer-mode row (auto / block / manual) at the given offset from the GUI origin. */
    protected void addTransferModeButtons(int rx, int ry) {
        place(Button.builder(transferLabel(TransferMode.AUTO),
                        button -> sendControl(AbstractSearedOutputBlockEntity.ACTION_SET_TRANSFER_MODE,
                                TransferMode.AUTO.ordinal()))
                .bounds(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT).build(), rx + 8, ry);
        place(Button.builder(transferLabel(TransferMode.BLOCK),
                        button -> sendControl(AbstractSearedOutputBlockEntity.ACTION_SET_TRANSFER_MODE,
                                TransferMode.BLOCK.ordinal()))
                .bounds(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT).build(), rx + 62, ry);
        place(Button.builder(transferLabel(TransferMode.MANUAL),
                        button -> sendControl(AbstractSearedOutputBlockEntity.ACTION_SET_TRANSFER_MODE,
                                TransferMode.MANUAL.ordinal()))
                .bounds(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT).build(), rx + 116, ry);
    }

    /** Adds a single button that cycles through the given output kinds. */
    protected void addOutputModeCycleButton(int rx, int ry, int width, OutputMode... modes) {
        this.outputCycleModes = modes.length == 0 ? OutputMode.values() : modes;
        this.outputCycleButton = place(Button.builder(Component.empty(), button -> {
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
        }).bounds(0, 0, width, BUTTON_HEIGHT).build(), rx, ry);
    }

    /** Adds a single button that cycles auto -> block -> manual. */
    protected void addTransferModeCycleButton(int rx, int ry, int width) {
        this.transferCycleButton = place(Button.builder(Component.empty(), button -> {
            TransferMode current = TransferMode.byId(getMenu().getMenuData(SearedMenuData.TRANSFER_MODE));
            TransferMode next = TransferMode.values()[(current.ordinal() + 1) % TransferMode.values().length];
            sendControl(AbstractSearedOutputBlockEntity.ACTION_SET_TRANSFER_MODE, next.ordinal());
        }).bounds(0, 0, width, BUTTON_HEIGHT).build(), rx, ry);
    }

    /** Adds the manual "return once" button. */
    protected void addManualButton(int rx, int ry, int width) {
        addManualButton(rx, ry, width, "gui.tinkers_rainbow.seared_output.manual");
    }

    /** Adds the manual "return once" button with a custom translation key. */
    protected void addManualButton(int rx, int ry, int width, String translationKey) {
        place(Button.builder(Component.translatable(translationKey),
                        button -> sendControl(AbstractSearedOutputBlockEntity.ACTION_MANUAL_TRANSFER, 0))
                .bounds(0, 0, width, BUTTON_HEIGHT).build(), rx, ry);
    }

    /** Adds the numeric input box plus MAX and apply buttons at the given offset from the GUI origin. */
    protected void addLimitBox(int rx, int ry) {
        this.limitBox = new EditBox(this.font, 0, 0, 60, 16,
                Component.translatable("gui.tinkers_rainbow.seared_output.limit"));
        this.limitBox.setMaxLength(10);
        // tolerant filter: digits and MAX are accepted, transient states while editing do not get eaten
        this.limitBox.setFilter(text -> text.length() <= 10 && text.matches("(?i)[0-9max]*"));
        // seed the box from the synchronised value so a rebuild (resize / GUI scale change) keeps the text
        int current = getMenu().getMenuData(SearedMenuData.OUTPUT_LIMIT);
        this.displayedLimit = current;
        this.limitBox.setValue(current == AbstractSearedOutputBlockEntity.LIMIT_UNLIMITED
                ? "MAX"
                : Integer.toString(Math.max(current, 0)));
        place(this.limitBox, rx + 8, ry);

        place(Button.builder(Component.translatable("gui.tinkers_rainbow.seared_output.limit.max"),
                        button -> applyLimit(AbstractSearedOutputBlockEntity.LIMIT_UNLIMITED))
                .bounds(0, 0, 34, BUTTON_HEIGHT).build(), rx + 72, ry - 1);
        place(Button.builder(Component.translatable("gui.tinkers_rainbow.seared_output.limit.apply"),
                        button -> applyLimit(parseLimit()))
                .bounds(0, 0, 58, BUTTON_HEIGHT).build(), rx + 110, ry - 1);
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
