package org.moliyy.tinkers_rainbow.utils;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SyncUtils {
    public static void syncHand(Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer sp) {
            ItemStack stack = player.getItemInHand(hand);
            int slot = hand == InteractionHand.MAIN_HAND ? 36 + sp.getInventory().selected : 45;
            sp.connection.send(new ClientboundContainerSetSlotPacket(0, 0, slot, stack));
        }
    }

    public static void syncArmor(Player player, EquipmentSlot slot) {
        if (player instanceof ServerPlayer sp) {
            ItemStack stack = player.getItemBySlot(slot);

            int slotIndex = switch (slot) {
                case HEAD -> 5;
                case CHEST -> 6;
                case LEGS -> 7;
                case FEET -> 8;
                default -> -1;
            };
            if (slotIndex != -1) {
                sp.connection.send(new ClientboundContainerSetSlotPacket(0, 0, slotIndex, stack));
            }
        }
    }
}