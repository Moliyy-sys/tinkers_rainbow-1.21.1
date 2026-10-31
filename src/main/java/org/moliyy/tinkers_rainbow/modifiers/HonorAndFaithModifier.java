package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.nbt.LongTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HonorAndFaithModifier extends Modifier implements GeneralInteractionModifierHook {

    private static final Map<UUID, Integer> JUMPING_PLAYERS = new HashMap<>();
    private static final int COOLDOWN = 160;
    private static final ResourceLocation COOLDOWN_KEY = ResourceLocation.fromNamespaceAndPath("tinkers_rainbow", "honor_faith_cooldown");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.GENERAL_INTERACT);
    }

    @Override
    public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player,
                                       InteractionHand hand, InteractionSource source) {
        if (player.level().isClientSide || tool.isBroken()) return InteractionResult.PASS;

        long currentTime = player.level().getGameTime();
        long lastUse = tool.getPersistentData().get(COOLDOWN_KEY, (tag, key) -> tag.getLong(key));
        if (currentTime - lastUse < COOLDOWN) return InteractionResult.PASS;

        player.setDeltaMovement(player.getDeltaMovement().x, 1.2, player.getDeltaMovement().z);
        player.hurtMarked = true;
        JUMPING_PLAYERS.put(player.getUUID(), modifier.getLevel());

        tool.getPersistentData().put(COOLDOWN_KEY, LongTag.valueOf(currentTime));
        return InteractionResult.SUCCESS;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        Integer level = JUMPING_PLAYERS.remove(player.getUUID());
        if (level == null) return;

        float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * 4.0F;
        AABB area = player.getBoundingBox().inflate(4.0);
        for (LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class, area,
                e -> e != player && e.isAlive())) {
            entity.hurt(player.damageSources().playerAttack(player), damage);
        }

        if (hasHonorAndFaith(player)) {
            event.setCanceled(true);
        }
    }

    private static boolean hasHonorAndFaith(Player player) {

        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (isToolWithModifier(stack)) return true;
        }

        for (EquipmentSlot slot : new EquipmentSlot[]{
                EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack stack = player.getItemBySlot(slot);
            if (isToolWithModifier(stack)) return true;
        }
        return false;
    }

    private static boolean isToolWithModifier(ItemStack stack) {
        if (stack.isEmpty()) return false;
        try {
            ToolStack tool = ToolStack.from(stack);

            return tool.getModifierLevel(new HonorAndFaithModifier()) > 0;
        } catch (Exception e) {

            return false;
        }
    }
}