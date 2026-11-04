package org.moliyy.tinkers_rainbow.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.moliyy.tinkers_rainbow.Config;

public class HudEditorScreen extends Screen {

    private int draggingMode = 0;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public HudEditorScreen() {
        super(Component.translatable("gui.tinkers_rainbow.hud_editor.title"));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, this.width, this.height, 0x55000000);
        guiGraphics.drawCenteredString(this.font, Component.translatable("gui.tinkers_rainbow.hud_editor.instructions"), this.width / 2, 20, 0xFFFFFF);


        int cdX = getBarX(Config.COMMON.cooldownBarX.get(), CooldownHudRenderer.RENDER_WIDTH);
        int cdY = getBarY(Config.COMMON.cooldownBarY.get(), CooldownHudRenderer.RENDER_HEIGHT, -80);
        CooldownHudRenderer.render(guiGraphics, this.font, cdX, cdY, 1.0f, 0, true);


        int shX = getBarX(Config.COMMON.shieldBarX.get(), ShieldHudRenderer.RENDER_WIDTH);
        int shY = getBarY(Config.COMMON.shieldBarY.get(), ShieldHudRenderer.RENDER_HEIGHT, -100);
        ShieldHudRenderer.render(guiGraphics, this.font, shX, shY, 100f, 100f, false, 1.0f, 0);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {

            int cdX = getBarX(Config.COMMON.cooldownBarX.get(), CooldownHudRenderer.RENDER_WIDTH);
            int cdY = getBarY(Config.COMMON.cooldownBarY.get(), CooldownHudRenderer.RENDER_HEIGHT, -80);
            if (mouseX >= cdX && mouseX <= cdX + CooldownHudRenderer.RENDER_WIDTH && mouseY >= cdY && mouseY <= cdY + CooldownHudRenderer.RENDER_HEIGHT) {
                draggingMode = 1;
                dragOffsetX = (int) (mouseX - cdX);
                dragOffsetY = (int) (mouseY - cdY);
                return true;
            }


            int shX = getBarX(Config.COMMON.shieldBarX.get(), ShieldHudRenderer.RENDER_WIDTH);
            int shY = getBarY(Config.COMMON.shieldBarY.get(), ShieldHudRenderer.RENDER_HEIGHT, -100);
            if (mouseX >= shX && mouseX <= shX + ShieldHudRenderer.RENDER_WIDTH && mouseY >= shY && mouseY <= shY + ShieldHudRenderer.RENDER_HEIGHT) {
                draggingMode = 2;
                dragOffsetX = (int) (mouseX - shX);
                dragOffsetY = (int) (mouseY - shY);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggingMode == 1) {
            int newX = Math.max(0, Math.min((int) (mouseX - dragOffsetX), this.width - CooldownHudRenderer.RENDER_WIDTH));
            int newY = Math.max(0, Math.min((int) (mouseY - dragOffsetY), this.height - CooldownHudRenderer.RENDER_HEIGHT));
            Config.COMMON.cooldownBarX.set(newX);
            Config.COMMON.cooldownBarY.set(newY);
            return true;
        } else if (draggingMode == 2) {
            int newX = Math.max(0, Math.min((int) (mouseX - dragOffsetX), this.width - ShieldHudRenderer.RENDER_WIDTH));
            int newY = Math.max(0, Math.min((int) (mouseY - dragOffsetY), this.height - ShieldHudRenderer.RENDER_HEIGHT));
            Config.COMMON.shieldBarX.set(newX);
            Config.COMMON.shieldBarY.set(newY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.draggingMode = 0;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void onClose() {
        Config.SPEC.save();
        super.onClose();
    }


    private int getBarX(int configX, int width) {
        return configX == -1 ? (this.width / 2 - (width / 2)) : configX;
    }

    private int getBarY(int configY, int height, int defaultOffset) {
        return configY == -1 ? (this.height + defaultOffset) : configY;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}