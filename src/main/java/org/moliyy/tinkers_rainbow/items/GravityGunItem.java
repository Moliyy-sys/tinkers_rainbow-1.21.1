package org.moliyy.tinkers_rainbow.items;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import org.jetbrains.annotations.Nullable;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import org.moliyy.tinkers_rainbow.utils.GravityGunHandler;

/**
 * The gravity gun. Left as a normal modifiable item so it can be assembled at the Tinker's Anvil; the
 * special behaviour lives in {@link GravityGunHandler}.
 */
public class GravityGunItem extends ModifiableItem {

    public GravityGunItem(Item.Properties properties, ToolDefinition definition) {
        super(properties, definition);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ToolStack tool = ToolStack.from(stack);
        if (tool.isBroken()) {
            return InteractionResultHolder.pass(stack);
        }
        // sneaking + right click switches between attract and mining modes
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                GravityGunHandler.toggleMode(stack, player);
            }
            return InteractionResultHolder.success(stack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int remainingUseDuration) {
        if (living instanceof Player player && level instanceof ServerLevel serverLevel) {
            GravityGunHandler.onUseTick(serverLevel, player, stack);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int timeLeft) {
        if (!level.isClientSide && living instanceof Player player) {
            GravityGunHandler.stopUsing(player);
        }
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity living, int count) {
        if (!living.level().isClientSide && living instanceof Player player) {
            GravityGunHandler.stopUsing(player);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slotId,
                              boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!level.isClientSide && entity instanceof Player player) {
            GravityGunHandler.inventoryTick(player, stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        boolean mining = GravityGunHandler.isMiningMode(stack);
        tooltip.add(Component.translatable(mining
                        ? "tooltip.tinkers_rainbow.gravity_gun.mode.mining"
                        : "tooltip.tinkers_rainbow.gravity_gun.mode.pull")
                .withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.translatable("tooltip.tinkers_rainbow.gravity_gun.controls")
                .withStyle(ChatFormatting.GRAY));
    }
}
