package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.LongTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CheesyBurgerModifier extends Modifier implements GeneralInteractionModifierHook {
    private static final Random RANDOM = new Random();
    private static final int COOLDOWN_TICKS = 100; // 5秒

    private static final ResourceLocation COOLDOWN_KEY = ResourceLocation.fromNamespaceAndPath("tinkers_rainbow", "cheesy_burger_cooldown");

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

        if (currentTime - lastUse < COOLDOWN_TICKS) {
            return InteractionResult.PASS;
        }

        List<Item> foodItems = new ArrayList<>();
        for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(Tags.Items.FOODS)) {
            foodItems.add(holder.value());
        }
        if (!foodItems.isEmpty()) {
            ItemStack food = new ItemStack(foodItems.get(RANDOM.nextInt(foodItems.size())));
            if (!player.getInventory().add(food)) player.drop(food, false);
        }


        tool.getPersistentData().put(COOLDOWN_KEY, LongTag.valueOf(currentTime));

        return InteractionResult.SUCCESS;
    }
}