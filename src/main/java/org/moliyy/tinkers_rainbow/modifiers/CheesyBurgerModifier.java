package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.LongTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import org.moliyy.tinkers_rainbow.Config;
import org.moliyy.tinkers_rainbow.utils.ICooldownModifier;
import org.moliyy.tinkers_rainbow.utils.ModifierInteractionHelper;
import org.moliyy.tinkers_rainbow.utils.SyncUtils;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;

public class CheesyBurgerModifier extends Modifier implements
        GeneralInteractionModifierHook, InventoryTickModifierHook, ICooldownModifier {

    private static final ResourceLocation COOLDOWN_KEY  =
            ResourceLocation.fromNamespaceAndPath("tinkers_rainbow", "cheesy_burger_cooldown");

    private static final ResourceLocation EAT_COOLDOWN_KEY =
            ResourceLocation.fromNamespaceAndPath("tinkers_rainbow", "cheesy_burger_eat_cooldown");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {

        hookBuilder.addHook(this, ModifierHooks.GENERAL_INTERACT, ModifierHooks.INVENTORY_TICK);
    }


    @Override
    public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player,
                                       InteractionHand hand, InteractionSource source) {
        if (player.level().isClientSide || tool.isBroken()) return InteractionResult.PASS;

        if (!ModifierInteractionHelper.isSelected(tool, modifier, player)) {
            return InteractionResult.PASS;
        }

        long currentTime = player.level().getGameTime();
        int cooldown = Config.COMMON.cheesyBurgerGenCooldown.get();

        long lastUse = tool.getPersistentData().get(COOLDOWN_KEY, (tag, key) -> tag.getLong(key));
        if (lastUse > 0 && currentTime - lastUse < Config.COMMON.cheesyBurgerGenCooldown.get()) {
            return InteractionResult.FAIL;
        }
        List<Item> foodItems = new ArrayList<>();
        for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(Tags.Items.FOODS)) {
            foodItems.add(holder.value());
        }
        if (!foodItems.isEmpty()) {
            ItemStack food = new ItemStack(foodItems.get(player.getRandom().nextInt(foodItems.size())));
            if (!player.getInventory().add(food)) player.drop(food, false);
        }

        tool.getPersistentData().put(COOLDOWN_KEY, LongTag.valueOf(currentTime));

        SyncUtils.syncHand(player, hand);

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder,
                                int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (world.isClientSide || !(holder instanceof Player player)) return;
        if (tool.isBroken()) return;

        if (player.getHealth() >= player.getMaxHealth()) return;

        long currentTime = world.getGameTime();
        int cooldown = Config.COMMON.cheesyBurgerEatCooldown.get();
        long lastUse = tool.getPersistentData().get(EAT_COOLDOWN_KEY, (tag, key) -> tag.getLong(key));

        if (currentTime - lastUse < cooldown) return;

        ItemStack foodToEat = ItemStack.EMPTY;

        for (ItemStack invStack : player.getInventory().items) {
            if (!invStack.isEmpty() && invStack.is(Tags.Items.FOODS)) {
                foodToEat = invStack;
                break;
            }
        }

        if (foodToEat.isEmpty() && player.getOffhandItem().is(Tags.Items.FOODS)) {
            foodToEat = player.getOffhandItem();
        }

        if (foodToEat.isEmpty()) return;

        foodToEat.shrink(1);

        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0F, 1.0F);

        if (world instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.ITEM_SLIME,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    5, 0.2, 0.2, 0.2, 0.05);
        }

        float healPercentage = Config.COMMON.cheesyBurgerHealPercentage.get() / 100.0F;
        float healAmount = player.getMaxHealth() * healPercentage;
        player.heal(healAmount);

        tool.getPersistentData().put(EAT_COOLDOWN_KEY, LongTag.valueOf(currentTime));
    }

    @Override
    public ResourceLocation getCooldownKey() {
        return COOLDOWN_KEY;
    }

    @Override
    public int getCooldownTicks(IToolStackView tool, ModifierEntry modifier) {
        return Config.COMMON.cheesyBurgerGenCooldown.get();
    }
}