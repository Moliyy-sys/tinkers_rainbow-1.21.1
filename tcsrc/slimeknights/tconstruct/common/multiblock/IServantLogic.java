/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.block.state.BlockState
 */
package slimeknights.tconstruct.common.multiblock;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.common.multiblock.IMasterLogic;

public interface IServantLogic {
    @Nullable
    public BlockPos getMasterPos();

    public void notifyMasterOfChange(BlockPos var1, BlockState var2);

    public boolean isValidMaster(IMasterLogic var1);

    public void setPotentialMaster(IMasterLogic var1);

    default public void onMasterLoad(IMasterLogic master) {
    }

    public void removeMaster(IMasterLogic var1);
}
