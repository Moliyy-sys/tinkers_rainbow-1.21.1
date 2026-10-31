package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.UsingToolModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.Random;

public class OnsenModifier extends Modifier implements InventoryTickModifierHook {

    private static final int INTERVAL_TICKS = 40;
    private static final Random RANDOM = new Random();

    private static final String KEY_SERVE = "message.tinkers_rainbow.onsen.serve";
    private static final String KEY_MANTOU = "message.tinkers_rainbow.onsen.food.mantou";
    private static final String KEY_ROD = "message.tinkers_rainbow.onsen.food.rod";
    private static final String KEY_MILK = "message.tinkers_rainbow.onsen.food.milk";

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
    }

    private static void heal(Player player, float ratio, String translationKey) {
        float amount = player.getMaxHealth() * ratio;
        player.heal(amount);
        player.displayClientMessage(Component.translatable(translationKey), true);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier,
                                Level world,
                                LivingEntity holder, int itemSlot,
                                boolean isSelected,boolean isCorrectSlot, @Nullable ItemStack stack) {
        if (world.isClientSide() ||!(holder instanceof Player player)) {
            return;
        }

        if (stack == null || (stack != player.getMainHandItem() && stack != player.getOffhandItem())) {
            return;
        }

        if (player.tickCount % INTERVAL_TICKS != 0) {
            return;
        }
        if (player.getHealth() >= player.getMaxHealth()) {
            return;
        }

        player.displayClientMessage(Component.translatable(KEY_SERVE), true);

        int roll = RANDOM.nextInt(100);
        if (roll < 60) {
            heal(player, 0.2F, KEY_MANTOU);
        } else if (roll < 90) {
            heal(player, 0.5F, KEY_ROD);
        } else {
            heal(player, 0.8F, KEY_MILK);
        }

    }
}