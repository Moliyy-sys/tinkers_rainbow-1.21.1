package org.moliyy.tinkers_rainbow.integration.ae2;

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
 * Seared pattern interface block. It behaves like a regular seared component (it can be used inside the
 * smeltery wall) but additionally hosts an AE2 grid node and opens its own configuration GUI.
 */
public class SearedPatternInterfaceBlock extends SearedBlock {

    public SearedPatternInterfaceBlock(Properties properties) {
        super(properties, true);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SearedPatternInterfaceBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return null;
        }
        return (tickLevel, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof SearedPatternInterfaceBlockEntity interfaceEntity) {
                interfaceEntity.serverTick();
            }
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof SearedPatternInterfaceBlockEntity interfaceEntity
                && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(interfaceEntity, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
                            ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && placer instanceof Player player
                && level.getBlockEntity(pos) instanceof SearedPatternInterfaceBlockEntity interfaceEntity) {
            interfaceEntity.setOwner(player);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && !state.is(newState.getBlock())
                && level.getBlockEntity(pos) instanceof SearedPatternInterfaceBlockEntity interfaceEntity) {
            interfaceEntity.dropContents();
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
