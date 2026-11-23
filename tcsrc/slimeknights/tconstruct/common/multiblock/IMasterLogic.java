/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 */
package slimeknights.tconstruct.common.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface IMasterLogic {
    private BlockEntity self() {
        return (BlockEntity)this;
    }

    default public BlockState getMasterBlock() {
        return this.self().getBlockState();
    }

    default public BlockPos getMasterPos() {
        return this.self().getBlockPos();
    }

    public void notifyChange(BlockPos var1, BlockState var2);

    default public <T extends BlockEntity> void onServantLoad(T servant) {
    }
}
