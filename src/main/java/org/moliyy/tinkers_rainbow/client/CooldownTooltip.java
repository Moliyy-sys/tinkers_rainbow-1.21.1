package org.moliyy.tinkers_rainbow.client;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record CooldownTooltip(float progress, int remainingSeconds, boolean isReady) implements TooltipComponent {
}