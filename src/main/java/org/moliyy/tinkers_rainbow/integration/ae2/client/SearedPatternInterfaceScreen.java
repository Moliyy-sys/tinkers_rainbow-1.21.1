package org.moliyy.tinkers_rainbow.integration.ae2.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import org.moliyy.tinkers_rainbow.TinkersRainbow;
import org.moliyy.tinkers_rainbow.integration.ae2.SearedPatternInterfaceMenu;
import org.moliyy.tinkers_rainbow.smeltery.client.AbstractSearedOutputScreen;

/**
 * GUI of the seared pattern interface.
 *
 * <p>Shows the output-mode row (fluid / basin / table), the transfer-mode row (auto / block / manual), the
 * numeric limit input with the MAX and apply buttons and the manual return button. Reuses the shared
 * {@link AbstractSearedOutputScreen} logic.</p>
 */
public class SearedPatternInterfaceScreen extends AbstractSearedOutputScreen<SearedPatternInterfaceMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            TinkersRainbow.MODID, "textures/gui/seared_pattern_interface.png");

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
    protected void init() {
        super.init();
        int x = this.leftPos;
        addOutputModeButtons(x, 18);
        addTransferModeButtons(x, 40);
        addLimitBox(x, 114);
        addManualButton(x, 134, 120);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
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

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
