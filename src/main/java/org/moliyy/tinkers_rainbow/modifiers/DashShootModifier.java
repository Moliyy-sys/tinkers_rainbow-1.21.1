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
import org.moliyy.tinkers_rainbow.Config;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
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
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class DashShootModifier extends Modifier implements
        GeneralInteractionModifierHook, InventoryTickModifierHook, ICooldownModifier {

    private static final ResourceLocation KEY_DASH_TICK =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "dash_tick");
    private static final ResourceLocation KEY_DASH_COOLDOWN =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "dash_shoot_cooldown");
    private static final ResourceLocation KEY_DASH_STAGE =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "dash_stage");

    private static final int TICKS_PER_STAGE = 4;
    private static final int TOTAL_STAGES = 3;
    private static final double DASH_DISTANCE_MULTIPLIER = 3.0;
    private static final double WALL_CHECK_OFFSET = 0.1;

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.GENERAL_INTERACT, ModifierHooks.INVENTORY_TICK);
    }

    @Override
    public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier,
                                       Player player, InteractionHand hand, InteractionSource source) {
        if (player.level().isClientSide() || tool.isBroken()) {
            return InteractionResult.PASS;
        }

        if (!ModifierInteractionHelper.isSelected(tool, modifier, player)) {
            return InteractionResult.PASS;
        }

        var data = tool.getPersistentData();

        long now = player.level().getGameTime();

        long lastUse = data.get(KEY_DASH_COOLDOWN, (tag, key) -> tag.getLong(key));
        int cooldownTicks = Config.COMMON.dashShootCooldown.get();

        if (lastUse > 0 && now - lastUse < cooldownTicks) {
            return InteractionResult.FAIL;
        }

        data.put(KEY_DASH_COOLDOWN, LongTag.valueOf(now));

        SyncUtils.syncHand(player, hand);

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

        int stage = data.getInt(KEY_DASH_STAGE);
        int tick = data.getInt(KEY_DASH_TICK);

        if (stage >= TOTAL_STAGES) {
            return;
        }

        if (!player.isAlive() || player.isSpectator()) {
            stopDash(tool, player);
            return;
        }

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

        Vec3 flatStep = new Vec3(step.x, 0, step.z);
        if (flatStep.lengthSqr() > 1.0E-6) {
            Vec3 offset = flatStep.normalize().scale(WALL_CHECK_OFFSET);
            AABB checkBox = player.getBoundingBox().move(offset);
            if (!player.level().noCollision(player, checkBox)) {
                stopDash(tool, player);
                return;
            }
        }

        AABB sweptBox = player.getBoundingBox()
                .minmax(player.getBoundingBox().move(step))
                .inflate(1.5);


        List<LivingEntity> targets = player.level().getEntitiesOfClass(
                LivingEntity.class, sweptBox,
                e -> e != player
                        && e.isAlive()
                        && !(e instanceof Player)
                        && !(e instanceof net.minecraft.world.entity.TamableAnimal tame && tame.isTame() && tame.getOwner() == player)
                        && !e.isInvulnerable()
        );

        player.setDeltaMovement(step);
        player.hurtMarked = true;
        player.fallDistance = 0;

        if (!targets.isEmpty()) {
            DamageSource source = player.damageSources().playerAttack(player);

            float base = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            if (base <= 0.0F) base = 1.0F;
            float damage = base * Config.COMMON.dashShootDamageMultiplier.get().floatValue();;

            for (LivingEntity target : targets) {

                target.hurt(source, damage);
            }
        }

        tick++;
        if (tick >= TICKS_PER_STAGE) {
            stage++;
            tick = 0;
        }

        if (stage >= TOTAL_STAGES) {
            stopDash(tool, player);
            return;
        }

        data.putInt(KEY_DASH_STAGE, stage);
        data.putInt(KEY_DASH_TICK, tick);
    }

    private static void stopDash(IToolStackView tool, Player player) {
        var data = tool.getPersistentData();
        data.putInt(KEY_DASH_STAGE, TOTAL_STAGES);
        data.putInt(KEY_DASH_TICK, 0);
        player.setDeltaMovement(Vec3.ZERO);
        player.hurtMarked = true;
    }

    @Override
    public ResourceLocation getCooldownKey() {
        return KEY_DASH_COOLDOWN;
    }

    @Override
    public int getCooldownTicks(IToolStackView tool, ModifierEntry modifier) {
        return Config.COMMON.dashShootCooldown.get();
    }
}