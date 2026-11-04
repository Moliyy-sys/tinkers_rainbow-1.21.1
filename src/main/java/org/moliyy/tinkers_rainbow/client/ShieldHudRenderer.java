package org.moliyy.tinkers_rainbow.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.moliyy.tinkers_rainbow.TinkersRainbow;

public class ShieldHudRenderer {
    private static final ResourceLocation EMPTY =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "textures/gui/shield_empty.png");
    private static final ResourceLocation FILL =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "textures/gui/shield_fill.png");

    public static final int TEXTURE_WIDTH = 86;
    public static final int TEXTURE_HEIGHT = 30;

    private static final int BAR_X = 13;
    private static final int BAR_Y = 8;
    private static final int BAR_WIDTH = 52;
    private static final int BAR_HEIGHT = 14;

    private static final float SCALE = 0.25f;

    public static final int RENDER_WIDTH = (int) Math.ceil(TEXTURE_WIDTH * SCALE);
    public static final int RENDER_HEIGHT = (int) Math.ceil(TEXTURE_HEIGHT * SCALE);

    public static void render(GuiGraphics guiGraphics, Font font, int x, int y, float shield, float maxShield, boolean isCooldown, float cooldownProgress, int cooldownSeconds) {

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(SCALE, SCALE, 1.0f);


        guiGraphics.blit(EMPTY, 0, 0, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);


        float percent = 0.0f;
        if (!isCooldown && shield > 0 && maxShield > 0) {
            percent = Math.min(1.0f, shield / maxShield);
        } else if (isCooldown) {
            percent = cooldownProgress;
        }


        if (percent > 0) {
            int fillWidth = (int) Math.ceil(BAR_WIDTH * percent);

            int scissorLeft = (int) (x + BAR_X * SCALE);
            int scissorTop = (int) (y + BAR_Y * SCALE);
            int scissorRight = (int) (x + (BAR_X + fillWidth) * SCALE);
            int scissorBottom = (int) (y + (BAR_Y + BAR_HEIGHT) * SCALE);

            if (scissorRight <= scissorLeft) {
                scissorRight = scissorLeft + 1;
            }

            guiGraphics.enableScissor(scissorLeft, scissorTop, scissorRight, scissorBottom);

            guiGraphics.blit(FILL, BAR_X, BAR_Y, 0, 0, BAR_WIDTH, BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);

            guiGraphics.disableScissor();
        }

        if (isCooldown && cooldownSeconds > 0) {
            String text = cooldownSeconds + "s";
            float textCenterX = BAR_X + BAR_WIDTH / 2.0f;
            float textCenterY = BAR_Y + (BAR_HEIGHT - font.lineHeight) / 2.0f;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(textCenterX, textCenterY, 0);
            guiGraphics.pose().scale(2.0f, 2.0f, 1.0f);
            guiGraphics.drawString(font, text, -font.width(text) / 2, 0, 0xFFFFFF, true);
            guiGraphics.pose().popPose();
        } else if (!isCooldown && shield > 0) {

            String text = (int)Math.ceil(shield) + " / " + (int)maxShield;
            float textCenterX = BAR_X + BAR_WIDTH / 2.0f;
            float textCenterY = BAR_Y + (BAR_HEIGHT - font.lineHeight) / 2.0f;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(textCenterX, textCenterY, 0);
            guiGraphics.pose().scale(2.0f, 2.0f, 1.0f);
            guiGraphics.drawString(font, text, -font.width(text) / 2, 0, 0x00FFFF, true);
            guiGraphics.pose().popPose();
        }

        guiGraphics.pose().popPose();
    }
}