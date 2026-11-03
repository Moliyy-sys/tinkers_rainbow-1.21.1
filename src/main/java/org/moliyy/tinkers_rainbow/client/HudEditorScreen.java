package org.moliyy.tinkers_rainbow.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.moliyy.tinkers_rainbow.Config;

public class HudEditorScreen extends Screen {
    private boolean dragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public HudEditorScreen() {
        super(Component.translatable("gui.tinkers_rainbow.hud_editor.title"));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        guiGraphics.fill(0, 0, this.width, this.height, 0x55000000);

        guiGraphics.drawCenteredString(this.font, Component.translatable("gui.tinkers_rainbow.hud_editor.instructions"), this.width / 2, 20, 0xFFFFFF);

        int x = getBarX();
        int y = getBarY();
        CooldownHudRenderer.render(guiGraphics, this.font, x, y, 1.0f, 0, true);

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int x = getBarX();
            int y = getBarY();
            if (mouseX >= x && mouseX <= x + CooldownHudRenderer.RENDER_WIDTH &&
                    mouseY >= y && mouseY <= y + CooldownHudRenderer.RENDER_HEIGHT) {
                this.dragging = true;
                this.dragOffsetX = (int) (mouseX - x);
                this.dragOffsetY = (int) (mouseY - y);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.dragging) {
            int newX = (int) (mouseX - dragOffsetX);
            int newY = (int) (mouseY - dragOffsetY);

            newX = Math.max(0, Math.min(newX, this.width - CooldownHudRenderer.RENDER_WIDTH));
            newY = Math.max(0, Math.min(newY, this.height - CooldownHudRenderer.RENDER_HEIGHT));

            Config.COMMON.cooldownBarX.set(newX);
            Config.COMMON.cooldownBarY.set(newY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.dragging = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void onClose() {
        Config.SPEC.save();
        super.onClose();
    }

    private int getBarX() {
        int x = Config.COMMON.cooldownBarX.get();
        return x == -1 ? (this.width / 2 - (CooldownHudRenderer.RENDER_WIDTH / 2)) : x;
    }

    private int getBarY() {
        int y = Config.COMMON.cooldownBarY.get();
        return y == -1 ? (this.height - 80) : y;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}