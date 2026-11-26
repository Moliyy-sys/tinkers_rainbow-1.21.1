package org.moliyy.tinkers_rainbow.utils;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import org.moliyy.tinkers_rainbow.modifiers.OrangeGemModifier;
import org.moliyy.tinkers_rainbow.modifiers.RainbowModifiers;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = TinkersRainbow.MODID)
public class OrangeGemEventHandler {

    // 记录玩家上次触发受击获得橘子的时间，防止高频伤害（如岩浆）导致刷物品卡顿
    private static final Map<UUID, Long> COOLDOWN_MAP = new HashMap<>();
    private static final int COOLDOWN_TICKS = 20; // 1秒冷却

    @SubscribeEvent
    public static void onPlayerHurt(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) {
            return;
        }

        // 冷却判断
        long currentTime = player.level().getGameTime();
        long lastTime = COOLDOWN_MAP.getOrDefault(player.getUUID(), (long) -COOLDOWN_TICKS);
        if (currentTime - lastTime < COOLDOWN_TICKS) {
            return;
        }

        // 检查玩家所有的装备槽位（主手、副手、四个防具槽）
        boolean hasModifier = false;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = player.getItemBySlot(slot);
            if (!stack.isEmpty() && stack.getItem() instanceof IModifiable) {
                ToolStack tool = ToolStack.from(stack);
                if (tool.getModifierLevel(RainbowModifiers.orangeGem.getId()) > 0) {
                    hasModifier = true;
                    break;
                }
            }
        }

        // 如果有该词条，发放橘子并记录冷却时间
        if (hasModifier) {
            OrangeGemModifier.giveOrange(player);
            COOLDOWN_MAP.put(player.getUUID(), currentTime);
        }
    }
}
