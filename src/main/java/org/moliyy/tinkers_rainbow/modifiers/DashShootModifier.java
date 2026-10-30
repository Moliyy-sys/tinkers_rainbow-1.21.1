package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.nbt.LongTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class DashShootModifier extends Modifier implements GeneralInteractionModifierHook, InventoryTickModifierHook {

    private static final ResourceLocation KEY_DASH_TICK =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "dash_tick");
    private static final ResourceLocation KEY_DASH_UNTIL =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "dash_cooldown_until");
    private static final ResourceLocation KEY_DASH_STAGE =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "dash_stage");

    private static final int COOLDOWN_TICKS = 200;
    private static final int TICKS_PER_STAGE = 4;
    private static final int TOTAL_STAGES = 3;
    private static final double DASH_DISTANCE_MULTIPLIER = 3.0;

    private static final double WALL_CHECK_OFFSET = 0.1;

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        // 分两次注册，避免可变参数签名歧义
        hookBuilder.addHook(this, ModifierHooks.GENERAL_INTERACT);
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
    }

    @Override
    public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier,
                                       Player player, InteractionHand hand, InteractionSource source) {
        if (player.level().isClientSide() || tool.isBroken()) {
            return InteractionResult.PASS;
        }

        var data = tool.getPersistentData();
        long now = player.level().getGameTime();
        Long untilBoxed = data.get(KEY_DASH_UNTIL, (tag, key) -> tag.getLong(key));
        long until = untilBoxed == null ? 0L : untilBoxed;
        if (now < until) {
            return InteractionResult.FAIL;
        }

        data.put(KEY_DASH_UNTIL, LongTag.valueOf(now + COOLDOWN_TICKS));
        data.putInt(KEY_DASH_STAGE, 0);
        data.putInt(KEY_DASH_TICK, 0);

        player.setDeltaMovement(Vec3.ZERO);
        player.hurtMarked = true;

        ToolDamageUtil.damage(tool, 10, player, null);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier,
                                Level level, LivingEntity holder, int itemSlot,
                                boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (level.isClientSide || !(holder instanceof Player player) || tool.isBroken()) {
            return;
        }
        tickDash(player, tool, modifier.getLevel());
    }

    public static void tickDash(Player player, IToolStackView tool, int level) {
        var data = tool.getPersistentData();

        Long cooldownBoxed = data.get(KEY_DASH_UNTIL, (tag, key) -> tag.getLong(key));
        long cooldownUntil = cooldownBoxed == null ? 0L : cooldownBoxed;
        if (cooldownUntil == 0L) return;

        long now = player.level().getGameTime();
        int stage = data.getInt(KEY_DASH_STAGE);
        int tick = data.getInt(KEY_DASH_TICK);

        if (stage >= TOTAL_STAGES) {
            if (now >= cooldownUntil) {
                data.put(KEY_DASH_UNTIL, LongTag.valueOf(0L));
            }
            return;
        }

        if (!player.isAlive() || player.isSpectator()) {
            stopDash(tool, player, now, cooldownUntil);
            return;
        }

        // ---- 强制水平方向 ----
        Vec3 look = player.getLookAngle();
        Vec3 dir = new Vec3(look.x, 0, look.z);
        if (dir.lengthSqr() > 1.0E-6) {
            dir = dir.normalize();
        } else {
            float yaw = player.getYRot() * (float) (Math.PI / 180.0);
            dir = new Vec3(-Math.sin(yaw), 0, Math.cos(yaw)).normalize();
        }
        Vec3 side = new Vec3(-dir.z, 0, dir.x).normalize();

        Vec3 stageMotion = switch (stage) {
            case 0 -> dir.scale(3.0).add(side.scale(3.0));
            case 1 -> side.scale(-3.0 * Math.sqrt(2.0));
            case 2 -> dir.scale(-3.0).add(side.scale(3.0));
            default -> Vec3.ZERO;
        };
        Vec3 step = stageMotion.scale(DASH_DISTANCE_MULTIPLIER / TICKS_PER_STAGE);

        // ---- 撞墙保护：只检测水平方向 0.1 格偏移 ----
        Vec3 flatStep = new Vec3(step.x, 0, step.z);
        if (flatStep.lengthSqr() > 1.0E-6) {
            Vec3 offset = flatStep.normalize().scale(WALL_CHECK_OFFSET);
            AABB checkBox = player.getBoundingBox().move(offset);
            if (!player.level().noCollision(player, checkBox)) {
                stopDash(tool, player, now, cooldownUntil);
                return;
            }
        }

        // ---- 关键修复 1：用"扫掠 AABB"覆盖整个 tick 位移 ----
        // 记录本 tick 位移前的碰撞箱，与位移后的碰撞箱合并，再膨胀 1.5 格
        AABB sweptBox = player.getBoundingBox()
                .minmax(player.getBoundingBox().move(step))
                .inflate(1.5);

        // ---- 关键修复 2：移除 Enemy 过滤，攻击所有非玩家、非友方、非创造模式实体 ----
        List<LivingEntity> targets = player.level().getEntitiesOfClass(
                LivingEntity.class, sweptBox,
                e -> e != player
                        && e.isAlive()
                        && !(e instanceof Player)                       // 不打其他玩家
                        && !(e instanceof net.minecraft.world.entity.TamableAnimal tame && tame.isTame() && tame.getOwner() == player) // 不打自己的宠物
                        && !e.isInvulnerable()                          // 不打无敌实体
        );

        // 每 tick 施加速度
        player.setDeltaMovement(step);
        player.hurtMarked = true;
        player.fallDistance = 0;

        // ---- 伤害判定（仅服务端） ----
        if (!targets.isEmpty()) {
            DamageSource source = player.damageSources().playerAttack(player);

            // ---- 关键修复 3：攻击力保底 1.0，避免空手/副手时伤害为 0 ----
            float base = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            if (base <= 0.0F) base = 1.0F;
            float damage = base * 3.0F;

            for (LivingEntity target : targets) {
                // hurt 返回 false 表示被无敌帧/免疫拦截，无需处理，继续尝试下一个
                target.hurt(source, damage);
            }
        }

        tick++;
        if (tick >= TICKS_PER_STAGE) {
            stage++;
            tick = 0;
        }

        if (stage >= TOTAL_STAGES) {
            stopDash(tool, player, now, cooldownUntil);
            return;
        }

        data.putInt(KEY_DASH_STAGE, stage);
        data.putInt(KEY_DASH_TICK, tick);
    }

    private static void stopDash(IToolStackView tool, Player player, long now, long cooldownUntil) {
        var data = tool.getPersistentData();
        data.putInt(KEY_DASH_STAGE, TOTAL_STAGES);
        data.putInt(KEY_DASH_TICK, 0);
        player.setDeltaMovement(Vec3.ZERO);
        player.hurtMarked = true;

        if (now >= cooldownUntil) {
            data.put(KEY_DASH_UNTIL, LongTag.valueOf(0L));
        }
    }
}