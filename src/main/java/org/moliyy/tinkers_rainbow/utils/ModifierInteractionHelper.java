package org.moliyy.tinkers_rainbow.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModifierInteractionHelper {
    private static final Random RANDOM = new Random();
    private static final String LAST_TICK_KEY = "tinkers_rainbow.last_interact_tick";
    private static final String SELECTED_MOD_KEY = "tinkers_rainbow.selected_modifier";


    public static boolean isSelected(IToolStackView tool, ModifierEntry currentModifier, Player player) {
        CompoundTag data = tool.getPersistentData().getCopy();
        long currentTime = player.level().getGameTime();
        long lastTick = data.getLong(LAST_TICK_KEY);

        if (currentTime != lastTick) {
            List<ResourceLocation> pool = new ArrayList<>();

            for (ModifierEntry entry : tool.getModifierList()) {
                if (entry.getModifier().getHooks().getOrNull(ModifierHooks.GENERAL_INTERACT) != null) {
                    pool.add(entry.getModifier().getId());
                }
            }

            if (pool.isEmpty()) return false;

            ResourceLocation selected = pool.get(RANDOM.nextInt(pool.size()));

            data.putLong(LAST_TICK_KEY, currentTime);
            data.putString(SELECTED_MOD_KEY, selected.toString());
            tool.getPersistentData().copyFrom(data);

            return currentModifier.getModifier().getId().equals(selected);
        }

        else {
            String selectedStr = data.getString(SELECTED_MOD_KEY);
            if (selectedStr.isEmpty()) return false;

            ResourceLocation selected = ResourceLocation.parse(selectedStr);
            return currentModifier.getModifier().getId().equals(selected);
        }
    }
}
