package org.moliyy.tinkers_rainbow.client;

import net.minecraft.resources.ResourceLocation;

import org.moliyy.tinkers_rainbow.Config;
import org.moliyy.tinkers_rainbow.TinkersRainbow;

/**
 * Resolves GUI background textures from the currently selected theme.
 *
 * <p>The theme is stored in the client config ({@code useDarkGui}); the path is resolved on every call so a
 * theme change is visible on the very next frame without reopening the screen.</p>
 */
public final class GuiTextures {

    public static final String DARK = "dark";
    public static final String LIGHT = "light";

    private GuiTextures() {
    }

    /** Whether the dark texture set is currently selected. Safe to call before the config is loaded. */
    public static boolean isDarkTheme() {
        try {
            return Config.COMMON.useDarkGui.get();
        } catch (Throwable ignored) {
            return true;
        }
    }

    /** Builds {@code textures/gui/<dark|light>/<name>.png} from the current theme. */
    public static ResourceLocation getGuiTexture(String name) {
        String theme = isDarkTheme() ? DARK : LIGHT;
        return ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID,
                "textures/gui/" + theme + "/" + name + ".png");
    }
}
