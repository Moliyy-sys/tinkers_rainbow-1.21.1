package org.moliyy.tinkers_rainbow.model;

import net.minecraft.nbt.LongTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.moliyy.tinkers_rainbow.modifiers.JulieCookingModifier;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class MuffinEntity extends TamableAnimal {

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState chargeAnimationState = new AnimationState();

    private static final EntityDataAccessor<Integer> DATA_IDLE_START_TICK =
            SynchedEntityData.defineId(MuffinEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_CHARGE_START_TICK =
            SynchedEntityData.defineId(MuffinEntity.class, EntityDataSerializers.INT);

    public MuffinEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean success = super.doHurtTarget(entity);
        if (success && entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 2), this);
        }
        return success;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IDLE_START_TICK, -1);
        builder.define(DATA_CHARGE_START_TICK, -1);
    }

    public void triggerChargeAnimation() {
        if (!this.level().isClientSide()) {
            this.entityData.set(DATA_CHARGE_START_TICK, this.tickCount);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            int idleTick = this.entityData.get(DATA_IDLE_START_TICK);
            if (idleTick >= 0 && !this.idleAnimationState.isStarted()) {
                this.idleAnimationState.start(idleTick);
            }
            int chargeTick = this.entityData.get(DATA_CHARGE_START_TICK);
            if (chargeTick >= 0) {
                if (!this.chargeAnimationState.isStarted()) {
                    this.chargeAnimationState.start(chargeTick);
                }
                if (this.tickCount - chargeTick > 10) {
                    this.chargeAnimationState.stop();
                }
            }
        } else {
            if (this.entityData.get(DATA_IDLE_START_TICK) < 0) {
                this.entityData.set(DATA_IDLE_START_TICK, this.tickCount);
            }
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (this.getOwner() instanceof Player player && !player.level().isClientSide) {

            for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
                ItemStack stack = player.getItemBySlot(slot);
                if (!stack.isEmpty() && stack.getItem() instanceof IModifiable) {
                    ToolStack tool = ToolStack.from(stack);
                    if (tool.getModifierLevel(JulieCookingModifier.MODIFIER_ID) > 0) {
                        tool.getPersistentData().put(JulieCookingModifier.MUFFIN_RESPAWN_KEY, LongTag.valueOf(player.level().getGameTime() + 2400));
                    }
                }
            }
        }
    }
}