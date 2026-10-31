package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.moliyy.tinkers_rainbow.effects.RepairMinecart;
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

public class RepairCartModifier extends Modifier implements GeneralInteractionModifierHook {
    private static final Map<UUID, RepairMinecart> LAST_CART = new HashMap<>();

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.GENERAL_INTERACT);
    }

    @Override
    public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player,
                                       InteractionHand hand, InteractionSource source) {
        if (player.level().isClientSide || tool.isBroken()) return InteractionResult.PASS;

        // 清除上一次召唤的矿车
        RepairMinecart old = LAST_CART.remove(player.getUUID());
        if (old != null && old.isAlive()) {
            old.discard();
        }

        // 获取水平冲刺方向
        Vec3 look = player.getLookAngle();
        Vec3 horizontalLook = new Vec3(look.x, 0, look.z);
        if (horizontalLook.lengthSqr() < 1.0E-4) {
            horizontalLook = Vec3.directionFromRotation(0, player.getYRot());
        }
        horizontalLook = horizontalLook.normalize();

        // 直接生成在玩家当前位置
        RepairMinecart minecart = new RepairMinecart(player.level(), player.getX(), player.getY(), player.getZ());
        player.level().addFreshEntity(minecart);

        // 玩家乘坐
        player.startRiding(minecart, true);
        player.hurtMarked = true; // 强制同步玩家位置到客户端

        // 传入速度，原速度 1.5 提高 4 倍即为 6.0
        minecart.setChargeVector(horizontalLook.scale(6.0), player);
        minecart.hurtMarked = true;

        LAST_CART.put(player.getUUID(), minecart);
        return InteractionResult.SUCCESS;
    }
}