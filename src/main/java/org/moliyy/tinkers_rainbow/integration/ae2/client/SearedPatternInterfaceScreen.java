package org.moliyy.tinkers_rainbow.integration.ae2.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import org.moliyy.tinkers_rainbow.integration.ae2.SearedPatternInterfaceMenu;
import org.moliyy.tinkers_rainbow.smeltery.client.AbstractSearedOutputScreen;

/**
 * GUI of the seared pattern interface.
 *
 * <p>Shows the output-mode row (fluid / basin / table), the transfer-mode row (auto / block / manual), the
 * numeric limit input with the MAX and apply buttons and the manual return button. Reuses the shared
 * {@link AbstractSearedOutputScreen} logic, including the live dark/light theme toggle.</p>
 */
public class SearedPatternInterfaceScreen extends AbstractSearedOutputScreen<SearedPatternInterfaceMenu> {

    public SearedPatternInterfaceScreen(SearedPatternInterfaceMenu menu, Inventory playerInventory,
                                        Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 248;
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelY = 1000;
    }

    @Override
    protected String getGuiTextureName() {
        return "seared_pattern_interface";
    }

    @Override
    protected void init() {
        super.init();
        // offsets are relative to the GUI origin; the base screen re-anchors them every frame
        addOutputModeButtons(0, 18);
        addTransferModeButtons(0, 40);
        addLimitBox(0, 114);
        addManualButton(0, 134, 120);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFFFFF, false);
        drawStatus(guiGraphics, 8, 62);

        guiGraphics.drawString(this.font,
                Component.translatable("gui.tinkers_rainbow.seared_output.limit.hint"),
                8, 105, 0x888888, false);
        guiGraphics.drawString(this.font,
                Component.translatable("gui.tinkers_rainbow.seared_output.slot.cast"),
                126, 66, 0xAAAAAA, false);
    }
}
