package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HonorAndFaithModifier extends Modifier implements GeneralInteractionModifierHook, InventoryTickModifierHook {
    private static final Map<UUID, Integer> JUMPING_PLAYERS = new HashMap<>();
    private static final int COOLDOWN = 160; // 8秒

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.GENERAL_INTERACT, ModifierHooks.INVENTORY_TICK);
    }

    @Override
    public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player, InteractionHand hand, InteractionSource source) {
        if (player.level().isClientSide || tool.isBroken() || player.getCooldowns().isOnCooldown(tool.getItem())) return InteractionResult.PASS;

        player.setDeltaMovement(player.getDeltaMovement().x, 1.2, player.getDeltaMovement().z);
        player.hurtMarked = true;
        JUMPING_PLAYERS.put(player.getUUID(), modifier.getLevel());
        player.getCooldowns().addCooldown(tool.getItem(), COOLDOWN);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (world.isClientSide || !(holder instanceof Player player)) return;

        Integer level = JUMPING_PLAYERS.get(player.getUUID());
        // 如果玩家在空中且正在下落，且上一tick不在地面上
        if (level != null && !player.onGround() && player.getDeltaMovement().y < 0) {
            // 等待落地
        } else if (level != null && player.onGround()) {
            // 触发落地伤害
            float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * 4.0F;
            AABB area = player.getBoundingBox().inflate(4.0);
            for (LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class, area, e -> e != player && e.isAlive())) {
                entity.hurt(player.damageSources().playerAttack(player), damage);
            }
            JUMPING_PLAYERS.remove(player.getUUID());
        }
    }
}