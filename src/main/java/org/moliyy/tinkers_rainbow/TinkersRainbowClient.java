package org.moliyy.tinkers_rainbow;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;
import org.moliyy.tinkers_rainbow.client.CooldownHudRenderer;
import org.moliyy.tinkers_rainbow.client.HudEditorScreen;
import org.moliyy.tinkers_rainbow.utils.ICooldownModifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.ArrayList;
import java.util.List;

@Mod(value = TinkersRainbow.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = TinkersRainbow.MODID, value = Dist.CLIENT)
public class TinkersRainbowClient {

    public static final KeyMapping EDIT_HUD_KEY = new KeyMapping(
            "key.tinkers_rainbow.edit_hud",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "key.categories.tinkers_rainbow"
    );

    private static boolean hasCooldownTool = false;
    private static float cachedProgress = 1.0f;
    private static int cachedRemainingSeconds = 0;
    private static boolean cachedReady = true;

    public TinkersRainbowClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        NeoForge.EVENT_BUS.addListener(TinkersRainbowClient::onClientTick);
        NeoForge.EVENT_BUS.addListener(TinkersRainbowClient::onRenderGui);
        NeoForge.EVENT_BUS.addListener(TinkersRainbowClient::onKeyInput);
    }

    @SubscribeEvent
    static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(EDIT_HUD_KEY);
    }

    public static void onKeyInput(InputEvent.Key event) {
        if (EDIT_HUD_KEY.consumeClick()) {
            Minecraft.getInstance().setScreen(new HudEditorScreen());
        }
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) {
            hasCooldownTool = false;
            return;
        }

        hasCooldownTool = false;
        ToolStack targetTool = null;
        ModifierEntry targetEntry = null;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isEmpty()) continue;

            ToolStack tool = ToolStack.from(stack);
            if (tool == null || tool.isBroken()) continue;

            List<ModifierEntry> currentMods = new ArrayList<>();
            for (ModifierEntry entry : tool.getModifierList()) {
                if (entry.getModifier() instanceof ICooldownModifier) {
                    currentMods.add(entry);
                }
            }

            if (currentMods.size() == 1) {
                targetTool = tool;
                targetEntry = currentMods.get(0);
                break;
            }
        }

        if (targetTool == null || targetEntry == null) return;

        ICooldownModifier coolMod = (ICooldownModifier) targetEntry.getModifier();
        long currentTime = mc.level.getGameTime();
        int totalTicks = coolMod.getCooldownTicks(targetTool, targetEntry);
        long lastUse = targetTool.getPersistentData().get(coolMod.getCooldownKey(), (tag, key) -> tag.getLong(key));
        int elapsed = (int) (currentTime - lastUse);

        hasCooldownTool = true;
        if (lastUse == 0 || elapsed >= totalTicks) {
            cachedReady = true;
            cachedProgress = 1.0f;
            cachedRemainingSeconds = 0;
        } else {
            cachedReady = false;
            cachedProgress = (float) elapsed / totalTicks;
            cachedRemainingSeconds = (int) Math.ceil((totalTicks - elapsed) / 20.0);
        }
    }

    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.screen instanceof HudEditorScreen || !hasCooldownTool) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        int configX = Config.COMMON.cooldownBarX.get();
        int configY = Config.COMMON.cooldownBarY.get();

        int x = configX == -1 ? (screenWidth / 2 - (CooldownHudRenderer.RENDER_WIDTH / 2)) : configX;
        int y = configY == -1 ? (screenHeight - 80) : configY;

        CooldownHudRenderer.render(guiGraphics, mc.font, x, y, cachedProgress, cachedRemainingSeconds, cachedReady);
    }
}