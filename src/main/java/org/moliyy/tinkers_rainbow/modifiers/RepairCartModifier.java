package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.nbt.LongTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.moliyy.tinkers_rainbow.Config;
import org.moliyy.tinkers_rainbow.utils.ICooldownModifier;
import org.moliyy.tinkers_rainbow.utils.ModifierInteractionHelper;
import org.moliyy.tinkers_rainbow.utils.RepairMinecart;
import org.moliyy.tinkers_rainbow.utils.SyncUtils;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RepairCartModifier extends Modifier implements
        GeneralInteractionModifierHook, ICooldownModifier {
    private static final Map<UUID, RepairMinecart> LAST_CART = new HashMap<>();

    private static final ResourceLocation COOLDOWN_KEY  =
            ResourceLocation.fromNamespaceAndPath("tinkers_rainbow", "repair_cart_cooldown");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.GENERAL_INTERACT);
    }

    @Override
    public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player,
                                       InteractionHand hand, InteractionSource source) {
        if (player.level().isClientSide || tool.isBroken()) return InteractionResult.PASS;

        if (!ModifierInteractionHelper.isSelected(tool, modifier, player)) {
            return InteractionResult.PASS;
        }

        long currentTime = player.level().getGameTime();
        long lastUse = tool.getPersistentData().get(COOLDOWN_KEY, (tag, key) -> tag.getLong(key));

        if (lastUse > 0 && currentTime - lastUse < Config.COMMON.repairCartCooldown.get()) {
            return InteractionResult.FAIL;
        }

        RepairMinecart old = LAST_CART.remove(player.getUUID());

        if (old != null && old.isAlive()) {
            old.discard();
        }

        Vec3 look = player.getLookAngle();
        Vec3 horizontalLook = new Vec3(look.x, 0, look.z);
        if (horizontalLook.lengthSqr() < 1.0E-4) {
            horizontalLook = Vec3.directionFromRotation(0, player.getYRot());
        }
        horizontalLook = horizontalLook.normalize();

        RepairMinecart minecart = new RepairMinecart(player.level(), player.getX(), player.getY(), player.getZ());
        player.level().addFreshEntity(minecart);

        player.startRiding(minecart, true);
        player.hurtMarked = true;

        minecart.setChargeVector(horizontalLook.scale(6.0), player);
        minecart.hurtMarked = true;

        LAST_CART.put(player.getUUID(), minecart);

        tool.getPersistentData().put(COOLDOWN_KEY, LongTag.valueOf(currentTime));

        SyncUtils.syncHand(player, hand);

        return InteractionResult.SUCCESS;
    }

    @Override
    public ResourceLocation getCooldownKey() {
        return COOLDOWN_KEY;
    }

    @Override
    public int getCooldownTicks(IToolStackView tool, ModifierEntry modifier) {
        return Config.COMMON.repairCartCooldown.get();
    }
}