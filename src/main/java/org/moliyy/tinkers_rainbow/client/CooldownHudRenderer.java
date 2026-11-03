package org.moliyy.tinkers_rainbow.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.moliyy.tinkers_rainbow.TinkersRainbow;

public class CooldownHudRenderer {
    private static final ResourceLocation EMPTY =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "textures/gui/cooldown_empty.png");
    private static final ResourceLocation FILL =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "textures/gui/cooldown_fill.png");
    private static final ResourceLocation READY =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "textures/gui/cooldown_ready.png");

    public static final int RENDER_WIDTH = 23;
    public static final int RENDER_HEIGHT = 13;

    public static void render(GuiGraphics guiGraphics, Font font, int x, int y, float progress, int remainingSeconds, boolean isReady) {

        float scale = 0.25f;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, 1.0f);

        if (isReady) {
            guiGraphics.blit(READY, 0, 0, 0, 0, 90, 50, 90, 50);
        } else {
            guiGraphics.blit(EMPTY, 0, 0, 0, 0, 90, 50, 90, 50);

            int fillWidth = (int) (90 * progress);
            if (fillWidth > 0) {
                guiGraphics.blit(FILL, 0, 0, 0, 0, fillWidth, 50, 90, 50);
            }

            String timeText = remainingSeconds + "s";
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(45, 20, 0);
            guiGraphics.pose().scale(2.0f, 2.0f, 1.0f);
            guiGraphics.drawString(font, timeText, -font.width(timeText) / 2, 0, 0xFFFFFF, true);
            guiGraphics.pose().popPose();
        }

        guiGraphics.pose().popPose();
    }
}