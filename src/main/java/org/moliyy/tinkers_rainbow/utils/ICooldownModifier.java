package org.moliyy.tinkers_rainbow.utils;

import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface ICooldownModifier {
    ResourceLocation getCooldownKey();

    int getCooldownTicks(IToolStackView tool, ModifierEntry modifier);
}