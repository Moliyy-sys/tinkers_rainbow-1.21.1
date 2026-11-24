package org.moliyy.tinkers_rainbow.cooler.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import org.moliyy.tinkers_rainbow.TinkersRainbow;
import org.moliyy.tinkers_rainbow.cooler.HighSpeedCoolerMenu;
import org.moliyy.tinkers_rainbow.smeltery.OutputMode;
import org.moliyy.tinkers_rainbow.smeltery.client.AbstractSearedOutputScreen;

/**
 * GUI of the high speed cooler.
 *
 * <p>A chest-like layout: 27 item output slots in a 9x3 grid, the cast slot, the shared limit / transfer /
 * output controls and the player inventory. The cooler has no fluid tanks any more.</p>
 */
public class HighSpeedCoolerScreen extends AbstractSearedOutputScreen<HighSpeedCoolerMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            TinkersRainbow.MODID, "textures/gui/high_speed_cooler.png");

    public HighSpeedCoolerScreen(HighSpeedCoolerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 256;
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelY = 1000;
    }

    @Override
    protected void init() {
        super.init();
        int x = this.leftPos;
        int y = this.topPos;
        addLimitBox(x, 100);
        addOutputModeCycleButton(x + 8, y + 122, 76, OutputMode.BASIN, OutputMode.TABLE);
        addTransferModeCycleButton(x + 88, y + 122, 80);
        addManualButton(x + 8, y + 146, 120);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFFFFF, false);
        drawLimitStatus(guiGraphics, 52, 78);
        guiGraphics.drawString(this.font,
                Component.translatable("gui.tinkers_rainbow.seared_output.slot.cast"),
                30, 82, 0xAAAAAA, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
