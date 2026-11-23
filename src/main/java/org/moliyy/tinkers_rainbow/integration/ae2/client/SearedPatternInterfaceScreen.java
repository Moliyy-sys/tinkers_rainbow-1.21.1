package org.moliyy.tinkers_rainbow.integration.ae2.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import org.moliyy.tinkers_rainbow.TinkersRainbow;
import org.moliyy.tinkers_rainbow.integration.ae2.SearedPatternInterfaceBlockEntity;
import org.moliyy.tinkers_rainbow.integration.ae2.SearedPatternInterfaceMenu;

/**
 * GUI of the seared pattern interface.
 */
public class SearedPatternInterfaceScreen extends AbstractContainerScreen<SearedPatternInterfaceMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            TinkersRainbow.MODID, "textures/gui/seared_pattern_interface.png");

    private static final int MODE_BUTTON_WIDTH = 52;
    private static final int MODE_BUTTON_Y = 18;
    private static final int STATUS_X = 80;
    private static final int STATUS_Y = 38;
    private static final int STATUS_LINE_HEIGHT = 11;

    private Button modeNoneButton;
    private Button modeBasinButton;
    private Button modeTableButton;
    private Button blockingButton;

    public SearedPatternInterfaceScreen(SearedPatternInterfaceMenu menu, Inventory playerInventory,
                                        Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 198;
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelY = 1000;
    }

    @Override
    protected void init() {
        super.init();
        int x = this.leftPos;
        int y = this.topPos;

        this.modeNoneButton = this.addRenderableWidget(Button
                .builder(modeLabel(SearedPatternInterfaceBlockEntity.Mode.NONE),
                        button -> sendButton(SearedPatternInterfaceMenu.BUTTON_MODE_NONE))
                .bounds(x + 8, y + MODE_BUTTON_Y, MODE_BUTTON_WIDTH, 18).build());
        this.modeBasinButton = this.addRenderableWidget(Button
                .builder(modeLabel(SearedPatternInterfaceBlockEntity.Mode.BASIN),
                        button -> sendButton(SearedPatternInterfaceMenu.BUTTON_MODE_BASIN))
                .bounds(x + 62, y + MODE_BUTTON_Y, MODE_BUTTON_WIDTH, 18).build());
        this.modeTableButton = this.addRenderableWidget(Button
                .builder(modeLabel(SearedPatternInterfaceBlockEntity.Mode.TABLE),
                        button -> sendButton(SearedPatternInterfaceMenu.BUTTON_MODE_TABLE))
                .bounds(x + 116, y + MODE_BUTTON_Y, MODE_BUTTON_WIDTH, 18).build());
        this.blockingButton = this.addRenderableWidget(Button
                .builder(Component.empty(), button -> sendButton(SearedPatternInterfaceMenu.BUTTON_TOGGLE_BLOCKING))
                .bounds(x + 8, y + 38, 64, 18).build());

        this.addRenderableWidget(Button
                .builder(Component.translatable("gui.tinkers_rainbow.seared_pattern_interface.encode"),
                        button -> sendButton(SearedPatternInterfaceMenu.BUTTON_ENCODE_PATTERN))
                .bounds(x + 8, y + 92, 76, 18).build());
        this.addRenderableWidget(Button
                .builder(Component.translatable("gui.tinkers_rainbow.seared_pattern_interface.clear"),
                        button -> sendButton(SearedPatternInterfaceMenu.BUTTON_CLEAR_PATTERN))
                .bounds(x + 88, y + 92, 80, 18).build());

        updateButtonLabels();
    }

    private void sendButton(int id) {
        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.getMenu().containerId, id);
        }
    }

    private static Component modeLabel(SearedPatternInterfaceBlockEntity.Mode mode) {
        String key = switch (mode) {
            case NONE -> "gui.tinkers_rainbow.seared_pattern_interface.mode.none";
            case BASIN -> "gui.tinkers_rainbow.seared_pattern_interface.mode.basin";
            case TABLE -> "gui.tinkers_rainbow.seared_pattern_interface.mode.table";
        };
        return Component.translatable(key);
    }

    private void updateButtonLabels() {
        if (this.modeNoneButton == null) {
            return;
        }
        int mode = this.getMenu().getModeId();
        this.modeNoneButton.setMessage(modeLabel(SearedPatternInterfaceBlockEntity.Mode.NONE));
        this.modeBasinButton.setMessage(modeLabel(SearedPatternInterfaceBlockEntity.Mode.BASIN));
        this.modeTableButton.setMessage(modeLabel(SearedPatternInterfaceBlockEntity.Mode.TABLE));
        this.modeNoneButton.active = mode != SearedPatternInterfaceBlockEntity.Mode.NONE.ordinal();
        this.modeBasinButton.active = mode != SearedPatternInterfaceBlockEntity.Mode.BASIN.ordinal();
        this.modeTableButton.active = mode != SearedPatternInterfaceBlockEntity.Mode.TABLE.ordinal();

        boolean blocking = this.getMenu().isBlocking();
        this.blockingButton.setMessage(Component.translatable(
                blocking
                        ? "gui.tinkers_rainbow.seared_pattern_interface.blocking.on"
                        : "gui.tinkers_rainbow.seared_pattern_interface.blocking.off"));
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        updateButtonLabels();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFFFFF, false);

        drawCenteredLabel(guiGraphics, "gui.tinkers_rainbow.seared_pattern_interface.slot.pattern", 8);
        drawCenteredLabel(guiGraphics, "gui.tinkers_rainbow.seared_pattern_interface.slot.cast", 32);
        drawCenteredLabel(guiGraphics, "gui.tinkers_rainbow.seared_pattern_interface.slot.sample", 56);

        SearedPatternInterfaceMenu menu = this.getMenu();
        int y = STATUS_Y;
        Component networkLine = Component.translatable(
                menu.isOnline()
                        ? "gui.tinkers_rainbow.seared_pattern_interface.status.online"
                        : "gui.tinkers_rainbow.seared_pattern_interface.status.offline");
        guiGraphics.drawString(this.font, networkLine, STATUS_X, y, menu.isOnline() ? 0x55FF55 : 0xFF5555, false);
        y += STATUS_LINE_HEIGHT;
        Component structureLine = Component.translatable(
                menu.isFormed()
                        ? "gui.tinkers_rainbow.seared_pattern_interface.status.formed"
                        : "gui.tinkers_rainbow.seared_pattern_interface.status.unformed");
        guiGraphics.drawString(this.font, structureLine, STATUS_X, y, menu.isFormed() ? 0x55FF55 : 0xAAAAAA, false);
        y += STATUS_LINE_HEIGHT;
        Component feedLine = switch (menu.getFeedStateId()) {
            case 1 -> Component.translatable("gui.tinkers_rainbow.seared_pattern_interface.status.feeding");
            case 2 -> Component.translatable("gui.tinkers_rainbow.seared_pattern_interface.status.waiting");
            default -> Component.translatable("gui.tinkers_rainbow.seared_pattern_interface.status.idle");
        };
        guiGraphics.drawString(this.font, feedLine, STATUS_X, y, 0xAAAAAA, false);
    }

    private void drawCenteredLabel(GuiGraphics guiGraphics, String key, int slotX) {
        Component label = Component.translatable(key);
        int width = this.font.width(label);
        guiGraphics.drawString(this.font, label, slotX + 9 - width / 2, 80, 0xAAAAAA, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
