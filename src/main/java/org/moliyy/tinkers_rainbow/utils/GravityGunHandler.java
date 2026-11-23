package org.moliyy.tinkers_rainbow.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import org.moliyy.tinkers_rainbow.TinkersRainbow;
import org.moliyy.tinkers_rainbow.items.GravityGunItem;

/**
 * Server side behaviour of the gravity gun.
 *
 * <ul>
 *     <li>Attract mode: holding right click pulls living entities in front of the player to their hand
 *     position, locking them there (no movement, no attacks). Left clicking a pulled entity launches it
 *     away with fall-damage based on travel distance.</li>
 *     <li>Mining mode: holding right click breaks stone blocks in a 5x5x5 area, sending the drops directly
 *     into the player inventory with block particles drifting back towards the player.</li>
 * </ul>
 */
public final class GravityGunHandler {

    public static final ResourceLocation MODE_KEY =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "gravity_gun_mode");

    public static final int MODE_ATTRACT = 0;
    public static final int MODE_MINING = 1;

    private static final int MINING_AREA = 2;
    private static final int MAX_BLOCKS_PER_TICK = 8;
    private static final double PULL_RANGE = 5.5D;
    private static final double HAND_HEIGHT = 0.0D;

    private static final Map<UUID, Set<UUID>> PULLED = new HashMap<>();
    private static final Map<UUID, LaunchData> LAUNCHED = new HashMap<>();

    private static final class LaunchData {
        final ServerLevel level;
        Vec3 last;
        double distance;
        int ticks;

        LaunchData(ServerLevel level, Vec3 start) {
            this.level = level;
            this.last = start;
        }
    }

    private GravityGunHandler() {
    }

    public static void init() {
        NeoForge.EVENT_BUS.addListener(GravityGunHandler::onServerTick);
        NeoForge.EVENT_BUS.addListener(GravityGunHandler::onAttackEntity);
        NeoForge.EVENT_BUS.addListener(GravityGunHandler::onPlayerLoggedOut);
    }

    // ------------------------------------------------------------------
    // mode handling
    // ------------------------------------------------------------------

    public static boolean isMiningMode(ItemStack stack) {
        if (!(stack.getItem() instanceof GravityGunItem)) {
            return false;
        }
        try {
            return ToolStack.from(stack).getPersistentData().getInt(MODE_KEY) == MODE_MINING;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static void toggleMode(ItemStack stack, Player player) {
        try {
            ToolStack tool = ToolStack.from(stack);
            int next = tool.getPersistentData().getInt(MODE_KEY) == MODE_MINING ? MODE_ATTRACT : MODE_MINING;
            tool.getPersistentData().putInt(MODE_KEY, next);
            stopUsing(player);
            player.displayClientMessage(Component.translatable(next == MODE_MINING
                    ? "message.tinkers_rainbow.gravity_gun.mode.mining"
                    : "message.tinkers_rainbow.gravity_gun.mode.pull"), true);
            player.playSound(SoundEvents.ENDERMAN_TELEPORT, 0.6F, 1.4F);
        } catch (Throwable ignored) {
            // not a valid tool stack, ignore
        }
    }

    // ------------------------------------------------------------------
    // continuous use
    // ------------------------------------------------------------------

    public static void onUseTick(ServerLevel level, Player player, ItemStack stack) {
        ToolStack tool;
        try {
            tool = ToolStack.from(stack);
        } catch (Throwable ignored) {
            return;
        }
        if (tool.isBroken()) {
            stopUsing(player);
            return;
        }
        if (isMiningMode(stack)) {
            miningTick(level, player, stack);
        } else {
            attractTick(level, player);
        }
    }

    private static void attractTick(ServerLevel level, Player player) {
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        // target position in front of the player, at eye level
        Vec3 handTarget = eye.add(look.scale(2.5D));
        Set<UUID> pulled = PULLED.computeIfAbsent(player.getUUID(), id -> new HashSet<>());
        Set<UUID> seen = new HashSet<>();

        List<LivingEntity> candidates = level.getEntitiesOfClass(LivingEntity.class,
                player.getBoundingBox().inflate(PULL_RANGE + 2.0D),
                entity -> entity != player && entity.isAlive() && !entity.isSpectator()
                        && !(entity instanceof Player));
        for (LivingEntity entity : candidates) {
            Vec3 center = entity.getBoundingBox().getCenter();
            Vec3 offset = center.subtract(eye);
            double distance = offset.length();
            if (distance > PULL_RANGE || distance < 0.3D) {
                continue;
            }
            // must be roughly in front of the player
            if (offset.normalize().dot(look) < 0.25D) {
                continue;
            }
            seen.add(entity.getUUID());
            pulled.add(entity.getUUID());

            // lock the entity completely
            if (entity instanceof Mob mob) {
                mob.setNoAi(true);
                mob.setTarget(null);
                mob.getNavigation().stop();
            }
            entity.setDeltaMovement(Vec3.ZERO);
            entity.fallDistance = 0.0F;
            entity.setYRot(entity.getYRot() + 15.0F);
            entity.setYHeadRot(entity.getYRot());
            entity.setYBodyRot(entity.getYRot());

            // snap entity to hand position every tick
            Vec3 current = entity.position();
            Vec3 target = handTarget;
            Vec3 smooth = current.lerp(target, 0.45D);
            entity.moveTo(smooth.x, smooth.y + HAND_HEIGHT, smooth.z);
        }

        // release entities that left the pull area
        Iterator<UUID> iterator = pulled.iterator();
        while (iterator.hasNext()) {
            UUID id = iterator.next();
            if (!seen.contains(id)) {
                releaseEntity(level, id);
                iterator.remove();
            }
        }
    }

    private static void miningTick(ServerLevel level, Player player, ItemStack stack) {
        if (player.tickCount % 3 != 0) {
            return;
        }
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        BlockPos center = BlockPos.containing(eye.add(look.scale(3.0D)));
        int broken = 0;
        outer:
        for (int dx = -MINING_AREA; dx <= MINING_AREA; dx++) {
            for (int dy = -MINING_AREA; dy <= MINING_AREA; dy++) {
                for (int dz = -MINING_AREA; dz <= MINING_AREA; dz++) {
                    BlockPos pos = center.offset(dx, dy, dz);
                    BlockState state = level.getBlockState(pos);
                    if (!isStone(state) || state.getDestroySpeed(level, pos) < 0.0F) {
                        continue;
                    }
                    List<ItemStack> drops = Block.getDrops(state, level, pos, level.getBlockEntity(pos), player, stack);
                    if (!level.destroyBlock(pos, false)) {
                        continue;
                    }
                    for (ItemStack drop : drops) {
                        if (drop.isEmpty()) {
                            continue;
                        }
                        if (!player.getInventory().add(drop)) {
                            player.drop(drop, false);
                        }
                    }
                    spawnBlockPullParticles(level, state, pos, eye);
                    broken++;
                    if (broken >= MAX_BLOCKS_PER_TICK) {
                        break outer;
                    }
                }
            }
        }
        if (broken > 0) {
            if (!player.isCreative()) {
                stack.hurtAndBreak(broken, player, EquipmentSlot.MAINHAND);
            }
            level.playSound(null, center, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.15F, 1.6F);
        }
    }

    private static boolean isStone(BlockState state) {
        return state.is(BlockTags.BASE_STONE_OVERWORLD)
                || state.is(BlockTags.STONE_BRICKS)
                || state.is(Tags.Blocks.STONES)
                || state.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
    }

    private static void spawnBlockPullParticles(ServerLevel level, BlockState state, BlockPos pos, Vec3 target) {
        BlockParticleOption option = new BlockParticleOption(ParticleTypes.BLOCK, state);
        Vec3 from = Vec3.atCenterOf(pos);
        for (int step = 1; step <= 3; step++) {
            Vec3 point = from.lerp(target, step / 4.0D);
            level.sendParticles(option, point.x, point.y, point.z, 1, 0.02D, 0.02D, 0.02D, 0.0D);
        }
    }

    // ------------------------------------------------------------------
    // attack / launch on left click
    // ------------------------------------------------------------------

    private static void onAttackEntity(AttackEntityEvent event) {
        Entity attacker = event.getEntity();
        LivingEntity target = event.getTarget();
        if (target == null || target == attacker) {
            return;
        }

        // Prevent any pulled entity from attacking
        for (Set<UUID> pulled : PULLED.values()) {
            if (pulled.contains(attacker.getUUID())) {
                event.setCanceled(true);
                return;
            }
        }

        // Left click with gravity gun launches a pulled entity away
        if (attacker instanceof Player player && target instanceof LivingEntity
                && player.getMainHandItem().getItem() instanceof GravityGunItem) {
            Set<UUID> pulled = PULLED.get(player.getUUID());
            if (pulled != null && pulled.remove(target.getUUID())) {
                event.setCanceled(true);
                launch(player, target);
            }
        }
    }

    private static void launch(Player player, LivingEntity target) {
        if (target instanceof Mob mob) {
            mob.setNoAi(false);
        }
        Vec3 look = player.getLookAngle();
        // strong launch forward and slightly up
        target.setDeltaMovement(look.x * 3.0D, 0.5D, look.z * 3.0D);
        target.hurtMarked = true;
        target.fallDistance = 0.0F;
        target.setYRot(player.getYRot());
        target.setYHeadRot(player.getYRot());
        target.setYBodyRot(player.getYRot());
        if (player.level() instanceof ServerLevel level) {
            LAUNCHED.put(target.getUUID(), new LaunchData(level, target.position()));
        }
        player.playSound(SoundEvents.PLAYER_ATTACK_KNOCKBACK, 1.0F, 0.8F);
    }

    // ------------------------------------------------------------------
    // release / cleanup
    // ------------------------------------------------------------------

    public static void stopUsing(Player player) {
        Set<UUID> pulled = PULLED.remove(player.getUUID());
        if (pulled == null || pulled.isEmpty()) {
            return;
        }
        if (player.level() instanceof ServerLevel level) {
            for (UUID id : pulled) {
                releaseEntity(level, id);
            }
        }
    }

    private static void releaseEntity(ServerLevel level, UUID id) {
        Entity entity = level.getEntity(id);
        if (entity instanceof LivingEntity living) {
            if (living instanceof Mob mob) {
                mob.setNoAi(false);
            }
            living.setDeltaMovement(Vec3.ZERO);
            living.hurtMarked = true;
            living.fallDistance = 0.0F;
        }
    }

    public static void inventoryTick(Player player, ItemStack stack) {
        Set<UUID> pulled = PULLED.get(player.getUUID());
        if (pulled != null && !player.isUsingItem()) {
            stopUsing(player);
        }
    }

    private static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        stopUsing(event.getEntity());
    }

    // ------------------------------------------------------------------
    // launched entity fall damage
    // ------------------------------------------------------------------

    private static void onServerTick(ServerTickEvent.Post event) {
        ServerLevel level = event.getLevel();
        if (LAUNCHED.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<UUID, LaunchData>> iterator = LAUNCHED.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, LaunchData> entry = iterator.next();
            LaunchData data = entry.getValue();
            Entity entity = data.level.getEntity(entry.getKey());
            if (!(entity instanceof LivingEntity living) || !living.isAlive()) {
                iterator.remove();
                continue;
            }
            data.ticks++;
            Vec3 pos = living.position();
            data.distance += Math.hypot(pos.x - data.last.x, pos.z - data.last.z);
            data.last = pos;
            living.fallDistance = 0.0F;
            if (living.onGround() && data.ticks > 3) {
                double damage = Math.min(24.0D, data.distance * 1.5D);
                if (damage > 1.0D) {
                    living.hurt(data.level.damageSources().fall(), (float) damage);
                }
                iterator.remove();
            }
        }
    }
}
