package org.moliyy.tinkers_rainbow.cooler;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import org.jetbrains.annotations.Nullable;

import org.moliyy.tinkers_rainbow.TinkersRainbow;

/**
 * Registration of the high speed cooler. This block works without AE2, so it is registered from the main
 * mod constructor and not from the AE2 integration.
 */
public final class CoolerRegistry {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TinkersRainbow.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TinkersRainbow.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TinkersRainbow.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, TinkersRainbow.MODID);

    public static final DeferredBlock<HighSpeedCoolerBlock> HIGH_SPEED_COOLER = BLOCKS.register(
            "high_speed_cooler", () -> new HighSpeedCoolerBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(3.0F, 9.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    public static final DeferredItem<BlockItem> HIGH_SPEED_COOLER_ITEM =
            ITEMS.registerSimpleBlockItem(HIGH_SPEED_COOLER);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HighSpeedCoolerBlockEntity>>
            HIGH_SPEED_COOLER_BLOCK_ENTITY = BLOCK_ENTITIES.register("high_speed_cooler",
            () -> BlockEntityType.Builder.of(HighSpeedCoolerBlockEntity::new,
                    HIGH_SPEED_COOLER.get()).build(null));

    public static final DeferredHolder<MenuType<?>, MenuType<HighSpeedCoolerMenu>>
            HIGH_SPEED_COOLER_MENU = MENUS.register("high_speed_cooler",
            () -> IMenuTypeExtension.create(HighSpeedCoolerMenu::new));

    private CoolerRegistry() {
    }

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        MENUS.register(modEventBus);
        modEventBus.addListener(CoolerRegistry::registerCapabilities);
    }

    /** Exposes the output slots for extraction and the fluid tanks for draining by pipes. */
    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.BLOCK,
                HIGH_SPEED_COOLER_BLOCK_ENTITY.get(),
                (blockEntity, context) -> new IItemHandler() {
                    @Override
                    public int getSlots() {
                        return HighSpeedCoolerBlockEntity.OUTPUT_SLOTS;
                    }

                    @Override
                    public net.minecraft.world.item.ItemStack getStackInSlot(int slot) {
                        return blockEntity.getOutputInventory().getStackInSlot(slot);
                    }

                    @Override
                    public net.minecraft.world.item.ItemStack insertItem(int slot, net.minecraft.world.item.ItemStack stack,
                                                                         boolean simulate) {
                        return stack; // output only
                    }

                    @Override
                    public net.minecraft.world.item.ItemStack extractItem(int slot, int amount, boolean simulate) {
                        return blockEntity.getOutputInventory().extractItem(slot, amount, simulate);
                    }

                    @Override
                    public int getSlotLimit(int slot) {
                        return blockEntity.getOutputInventory().getSlotLimit(slot);
                    }

                    @Override
                    public boolean isItemValid(int slot, net.minecraft.world.item.ItemStack stack) {
                        return false;
                    }
                });
    }
}
