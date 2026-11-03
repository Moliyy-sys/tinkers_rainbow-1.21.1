package org.moliyy.tinkers_rainbow.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashSet;
import java.util.Set;

public class RepairMinecart extends Minecart {
    private Vec3 chargeVector = null;
    private int chargeTicks = 0;
    private final Set<LivingEntity> hitEntities = new HashSet<>();
    private Player attacker = null;
    private boolean hasExploded = false;

    public RepairMinecart(EntityType<?> type, Level level) { super(type, level); }
    public RepairMinecart(Level level, double x, double y, double z) { super(level, x, y, z); }

    public void setChargeVector(Vec3 vector, Player attacker) {
        this.chargeVector = vector;
        this.attacker = attacker;
        this.chargeTicks = 40;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void destroy(DamageSource source) {
        this.discard();
    }

    @Override
    public void tick() {
        if (this.level().isClientSide) {
            super.tick();
            return;
        }

        if (this.getFirstPassenger() == null && this.tickCount > 5) {
            this.explodeAndSelfDestruct();
            return;
        }

        if (this.chargeVector != null && this.chargeTicks > 0) {
            AABB nextBox = this.getBoundingBox().move(this.getDeltaMovement());
            boolean hitWall = false;

            for (BlockPos pos : BlockPos.betweenClosed(
                    Mth.floor(nextBox.minX), Mth.floor(nextBox.minY), Mth.floor(nextBox.minZ),
                    Mth.floor(nextBox.maxX), Mth.floor(nextBox.maxY), Mth.floor(nextBox.maxZ))) {

                BlockState state = this.level().getBlockState(pos);
                if (state.isAir() || state.liquid() || state.is(BlockTags.FLOWERS) ||
                        state.is(BlockTags.WOOL_CARPETS) || state.is(BlockTags.REPLACEABLE_BY_TREES) || state.is(Blocks.SNOW)) {
                    continue;
                }

                VoxelShape shape = state.getCollisionShape(this.level(), pos);
                if (!shape.isEmpty() && (shape.max(Direction.Axis.Y) - shape.min(Direction.Axis.Y) > 0.3)) {
                    hitWall = true;
                    break;
                }
            }

            if (hitWall) {
                this.explodeAndSelfDestruct();
                return;
            }
        }

        if (this.chargeVector != null && this.chargeTicks > 0) {
            this.setDeltaMovement(this.chargeVector);

            if (this.getFirstPassenger() != null) {
                this.getFirstPassenger().hurtMarked = true;
            }
        }

        Vec3 prevPos = this.position();
        super.tick();

        if (this.chargeVector != null && this.chargeTicks > 0) {
            this.setDeltaMovement(this.chargeVector);
            this.chargeTicks--;

            if (this.chargeTicks <= 0) {
                this.explodeAndSelfDestruct();
                return;
            }
        }

        if (this.tickCount > 5 && this.chargeTicks > 0) {
            if (this.attacker != null && this.attacker.isAlive()) {
                float damage = (float) this.attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * 3.0F;

                AABB sweepBox = this.getBoundingBox()
                        .minmax(new AABB(prevPos, this.position()))
                        .inflate(1.5);

                for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, sweepBox,
                        e -> e != this.attacker && e.isAlive() && !(e instanceof Player))) {

                    if (this.hitEntities.add(target)) {
                        target.invulnerableTime = 0;
                        target.hurt(this.attacker.damageSources().playerAttack(this.attacker), damage);
                        target.setDeltaMovement(target.getDeltaMovement().add(0, 0.5, 0));
                    }
                }
            }
        }
    }

    private void explodeAndSelfDestruct() {
        if (this.hasExploded) return;
        this.hasExploded = true;

        if (this.getFirstPassenger() instanceof LivingEntity passenger) {
            passenger.stopRiding();
            passenger.setDeltaMovement(Vec3.ZERO);
            passenger.hurtMarked = true;
        }

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 1.0F, 1.0F);

        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        }

        if (this.attacker != null && this.attacker.isAlive()) {
            float damage = (float) this.attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * 3.0F;
            AABB blastArea = this.getBoundingBox().inflate(4.0);

            for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, blastArea,
                    e -> e != this.attacker && e.isAlive() && !(e instanceof Player))) {
                target.invulnerableTime = 0;
                target.hurt(this.attacker.damageSources().playerAttack(this.attacker), damage);
                target.setDeltaMovement(target.getDeltaMovement().add(0, 0.5, 0));
            }
        }

        this.discard();
    }
}