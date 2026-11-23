package org.moliyy.tinkers_rainbow.integration.ae2.client;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import org.moliyy.tinkers_rainbow.TinkersRainbow;
import org.moliyy.tinkers_rainbow.integration.ae2.Ae2Integration;

/**
 * Client side registration of the AE2 integration. Called from {@code TinkersRainbowClient} only after the
 * runtime AE2 check succeeded, so this class never loads when AE2 is missing.
 */
public final class Ae2ClientIntegration {

    private Ae2ClientIntegration() {
    }

    public static void registerScreens(RegisterMenuScreensEvent event) {
        if (!TinkersRainbow.isAe2Loaded()) {
            return;
        }
        event.register(Ae2Integration.SEARED_PATTERN_INTERFACE_MENU.get(),
                SearedPatternInterfaceScreen::new);
    }
}
