package org.moliyy.tinkers_rainbow.integration.ae2;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import org.moliyy.tinkers_rainbow.TinkersRainbow;

import appeng.api.AECapabilities;

/**
 * Everything that references Applied Energistics 2 lives in this class (and its package). The class is only
 * touched from {@link TinkersRainbow} when the AE2 mod is actually present, so this mod still loads without
 * AE2 installed.
 */
public final class Ae2Integration {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TinkersRainbow.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TinkersRainbow.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TinkersRainbow.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, TinkersRainbow.MODID);
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, TinkersRainbow.MODID);

    public static final DeferredBlock<SearedPatternInterfaceBlock> SEARED_PATTERN_INTERFACE = BLOCKS.register(
            "seared_pattern_interface", () -> new SearedPatternInterfaceBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(3.0F, 9.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    public static final DeferredItem<BlockItem> SEARED_PATTERN_INTERFACE_ITEM =
            ITEMS.registerSimpleBlockItem(SEARED_PATTERN_INTERFACE);

    public static final DeferredItem<SearedPatternItem> SEARED_PATTERN =
            ITEMS.register("seared_pattern", SearedPatternItem::new);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SearedPatternInterfaceBlockEntity>>
            SEARED_PATTERN_INTERFACE_BLOCK_ENTITY = BLOCK_ENTITIES.register("seared_pattern_interface",
            () -> BlockEntityType.Builder.of(SearedPatternInterfaceBlockEntity::new,
                    SEARED_PATTERN_INTERFACE.get()).build(null));

    public static final DeferredHolder<MenuType<?>, MenuType<SearedPatternInterfaceMenu>>
            SEARED_PATTERN_INTERFACE_MENU = MENUS.register("seared_pattern_interface",
            () -> IMenuTypeExtension.create(SearedPatternInterfaceMenu::new));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SearedPatternData>>
            SEARED_PATTERN_DATA = DATA_COMPONENTS.register("seared_pattern", () -> DataComponentType
            .<SearedPatternData>builder()
            .persistent(SearedPatternData.CODEC)
            .networkSynchronized(SearedPatternData.STREAM_CODEC)
            .cacheEncoding()
            .build());

    private Ae2Integration() {
    }

    /** Called from the mod constructor, only when AE2 is loaded. */
    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        MENUS.register(modEventBus);
        DATA_COMPONENTS.register(modEventBus);
        modEventBus.addListener(Ae2Integration::addCreativeTabItems);
        modEventBus.addListener(Ae2Integration::registerCapabilities);
    }

    private static void addCreativeTabItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(TinkersRainbow.RAINBOW_TAB.getKey())) {
            event.accept(SEARED_PATTERN.get());
            event.accept(SEARED_PATTERN_INTERFACE_ITEM.get());
        }
    }

    /**
     * AE2 discovers connectable block entities through the {@code ae2:in_world_grid_node_host} block
     * capability, not through a plain instanceof check. Without this registration ME cables (and every other
     * AE2 device) cannot see the interface and it never joins an AE network.
     */
    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST,
                SEARED_PATTERN_INTERFACE_BLOCK_ENTITY.get(),
                (blockEntity, context) -> blockEntity);
    }
}
