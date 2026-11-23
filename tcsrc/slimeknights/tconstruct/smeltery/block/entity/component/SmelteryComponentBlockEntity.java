/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  slimeknights.tconstruct.common.multiblock.ServantTileEntity
 */
package slimeknights.tconstruct.smeltery.block.entity.component;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.common.multiblock.IMasterLogic;
import slimeknights.tconstruct.common.multiblock.ServantTileEntity;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

public class SmelteryComponentBlockEntity
extends ServantTileEntity {
    public SmelteryComponentBlockEntity(BlockPos pos, BlockState state) {
        this((BlockEntityType)TinkerSmeltery.smelteryComponent.get(), pos, state);
    }

    protected SmelteryComponentBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void updateNeighbors(Level world, BlockPos pos, BlockState state) {
        for (Direction direction : Direction.values()) {
            SmelteryComponentBlockEntity component;
            BlockEntity tileEntity = world.getBlockEntity(pos.relative(direction));
            if (tileEntity instanceof IMasterLogic) {
                IMasterLogic master = (IMasterLogic)tileEntity;
                master.notifyChange(pos, state);
                break;
            }
            if (!(tileEntity instanceof SmelteryComponentBlockEntity) || !(component = (SmelteryComponentBlockEntity)tileEntity).hasMaster()) continue;
            component.notifyMasterOfChange(pos, state);
            break;
        }
    }
}
