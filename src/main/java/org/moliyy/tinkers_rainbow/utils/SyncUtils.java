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
            int slot = hand == InteractionHand.MAIN_HAND ? sp.getInventory().selected : 40;
            sp.connection.send(new ClientboundContainerSetSlotPacket(-2, 0, slot, stack));
        }
    }

    public static void syncArmor(Player player, EquipmentSlot slot) {
        if (player instanceof ServerPlayer sp) {
            ItemStack stack = player.getItemBySlot(slot);
            int slotIndex = 36 + (3 - slot.getIndex());
            sp.connection.send(new ClientboundContainerSetSlotPacket(-2, 0, slotIndex, stack));
        }
    }
}