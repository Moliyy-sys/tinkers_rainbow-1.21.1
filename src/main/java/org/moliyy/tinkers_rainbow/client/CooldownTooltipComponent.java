package org.moliyy.tinkers_rainbow.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import org.moliyy.tinkers_rainbow.TinkersRainbow;

public class CooldownTooltipComponent implements ClientTooltipComponent {
    private static final ResourceLocation EMPTY =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "textures/gui/cooldown_empty.png");
    private static final ResourceLocation FILL =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "textures/gui/cooldown_fill.png");
    private static final ResourceLocation READY =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "textures/gui/cooldown_ready.png");

    private final float progress;
    private final int remainingSeconds;
    private final boolean isReady;

    public CooldownTooltipComponent(CooldownTooltip tooltip) {
        this.progress = tooltip.progress();
        this.remainingSeconds = tooltip.remainingSeconds();
        this.isReady = tooltip.isReady();
    }

    @Override
    public int getHeight() {
        return 50;
    }

    @Override
    public int getWidth(Font font) {
        return 90;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        if (isReady) {
            guiGraphics.blit(READY, x, y, 0, 0, 90, 50, 90, 50);
        } else {
            guiGraphics.blit(EMPTY, x, y, 0, 0, 90, 50, 90, 50);

            int fillWidth = (int) (90 * progress);
            if (fillWidth > 0) {
                guiGraphics.blit(FILL, x, y, 0, 0, fillWidth, 50, 90, 50);
            }

            String timeText = remainingSeconds + "s";
            guiGraphics.drawString(font, timeText, x + 45 - font.width(timeText) / 2, y + 20, 0xFFFFFF, true);
        }
    }
}