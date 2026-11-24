package org.moliyy.tinkers_rainbow.cooler;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import slimeknights.tconstruct.smeltery.block.component.SearedBlock;

/**
 * High speed cooler: a plain (non AE) block that instantly pulls casting output from a smeltery into its
 * own output slots. It is a seared wall block so it can be part of the smeltery multiblock, exactly like
 * the seared pattern interface.
 */
public class HighSpeedCoolerBlock extends SearedBlock {

    public HighSpeedCoolerBlock(Properties properties) {
        super(properties, true);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HighSpeedCoolerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return null;
        }
        return (tickLevel, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof HighSpeedCoolerBlockEntity cooler) {
                cooler.serverTick();
            }
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof HighSpeedCoolerBlockEntity cooler
                && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(cooler, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
                            ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && !state.is(newState.getBlock())
                && level.getBlockEntity(pos) instanceof HighSpeedCoolerBlockEntity cooler) {
            cooler.dropContents();
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
