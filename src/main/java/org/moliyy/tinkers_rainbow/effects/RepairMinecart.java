package org.moliyy.tinkers_rainbow.effects;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;

public class RepairMinecart extends Minecart {
    public RepairMinecart(EntityType<?> type, Level level) { super(type, level); }
    public RepairMinecart(Level level, double x, double y, double z) { super(level, x, y, z); }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.getDeltaMovement().lengthSqr() > 0.1) {

            for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.0), e -> e != this.getFirstPassenger())) {
                entity.hurt(this.damageSources().generic(), 4.0F);

                entity.setDeltaMovement(entity.getDeltaMovement().add(0, 0.5, 0));
            }
        }
    }
}
