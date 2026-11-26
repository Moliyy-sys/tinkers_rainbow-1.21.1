package org.moliyy.tinkers_rainbow.cooler.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import org.moliyy.tinkers_rainbow.cooler.HighSpeedCoolerMenu;
import org.moliyy.tinkers_rainbow.smeltery.OutputMode;
import org.moliyy.tinkers_rainbow.smeltery.client.AbstractSearedOutputScreen;

/**
 * GUI of the high speed cooler.
 *
 * <p>A chest-like layout: 27 item output slots in a 9x3 grid, the cast slot, the shared limit / transfer /
 * output controls and the player inventory. Reuses the shared {@link AbstractSearedOutputScreen} logic,
 * including the live dark/light theme toggle.</p>
 */
public class HighSpeedCoolerScreen extends AbstractSearedOutputScreen<HighSpeedCoolerMenu> {

    public HighSpeedCoolerScreen(HighSpeedCoolerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 256;
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelY = 1000;
    }

    @Override
    protected String getGuiTextureName() {
        return "high_speed_cooler";
    }

    @Override
    protected int getThemeButtonTopOffset() {
        // keep the icon inside the title bar, above the first row of output slots
        return 1;
    }

    @Override
    protected void init() {
        super.init();
        // offsets are relative to the GUI origin; the base screen re-anchors them every frame
        addLimitBox(0, 100);
        addOutputModeCycleButton(8, 122, 76, OutputMode.BASIN, OutputMode.TABLE);
        addTransferModeCycleButton(88, 122, 80);
        addManualButton(8, 146, 120);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFFFFF, false);
        drawLimitStatus(guiGraphics, 52, 78);
        guiGraphics.drawString(this.font,
                Component.translatable("gui.tinkers_rainbow.seared_output.slot.cast"),
                30, 82, 0xAAAAAA, false);
    }
}
