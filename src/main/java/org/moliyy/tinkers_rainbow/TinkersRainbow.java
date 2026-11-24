package org.moliyy.tinkers_rainbow;

import net.minecraft.world.item.Items;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import org.moliyy.tinkers_rainbow.blocks.RainbowBlocks;
import org.moliyy.tinkers_rainbow.effects.RainbowEffects;
import org.moliyy.tinkers_rainbow.entity.RainbowEntityTypes;
import org.moliyy.tinkers_rainbow.fluids.RainbowFluids;
import org.moliyy.tinkers_rainbow.items.RainbowItems;
import org.moliyy.tinkers_rainbow.modifiers.HonorAndFaithModifier;
import org.moliyy.tinkers_rainbow.modifiers.RainbowModifiers;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(TinkersRainbow.MODID)
public class TinkersRainbow {
    public static final String MODID = "tinkers_rainbow";
    public static final Logger LOGGER = LogUtils.getLogger();

    /** True when Applied Energistics 2 is installed at runtime; the AE2 integration is optional. */
    private static final boolean AE2_LOADED = detectAe2();

    public static boolean isAe2Loaded() {
        return AE2_LOADED;
    }

    private static boolean detectAe2() {
        try {
            Class.forName("appeng.api.stacks.AEKey", false, TinkersRainbow.class.getClassLoader());
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }


    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RAINBOW_TAB =
            CREATIVE_MODE_TABS.register("rainbow_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.tinkers_rainbow"))
                    .withTabsBefore(CreativeModeTabs.INGREDIENTS)
                    .icon(() -> RainbowItems.BLUE_STEEL_INGOT.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {

                        RainbowItems.ITEMS.getEntries().forEach(entry -> {
                            var item = entry.get();
                            if (item != Items.AIR) {
                                output.accept(item);
                            }
                        });

                        RainbowBlocks.BLOCKS.getEntries().forEach(entry -> {
                            var item = entry.get().asItem();
                            if (item != Items.AIR) {
                                output.accept(item);
                            }
                        });

                        RainbowFluids.ALL.forEach(fluid -> {
                            var bucket = fluid.getBucket();
                            if (bucket != Items.AIR && bucket != null) {
                                output.accept(bucket);
                            }
                        });

                        output.accept(org.moliyy.tinkers_rainbow.cooler.CoolerRegistry.HIGH_SPEED_COOLER_ITEM.get());

                    }).build());

    public TinkersRainbow(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(this::commonSetup);

        // GUI -> server sync for the seared pattern interface output limit
        modEventBus.addListener(org.moliyy.tinkers_rainbow.smeltery.SearedControlPayload::register);

        RainbowBlocks.BLOCKS.register(modEventBus);

        RainbowItems.ITEMS.register(modEventBus);

        RainbowFluids.FLUIDS.register(modEventBus);

        RainbowEffects.MOB_EFFECTS.register(modEventBus);

        CREATIVE_MODE_TABS.register(modEventBus);

        RainbowModifiers.init(modEventBus);

        RainbowEntityTypes.register(modEventBus);

        org.moliyy.tinkers_rainbow.cooler.CoolerRegistry.init(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        if (AE2_LOADED) {
            org.moliyy.tinkers_rainbow.integration.ae2.Ae2Integration.init(modEventBus);
            LOGGER.info("Applied Energistics 2 detected, seared pattern interface enabled");
        } else {
            LOGGER.info("Applied Energistics 2 not detected, seared pattern interface disabled");
        }

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.COMMON.logDirtBlock.get()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.COMMON.magicNumberIntroduction.get(), Config.COMMON.magicNumber.get());

        Config.COMMON.itemStrings.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }


    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(RainbowItems.BLUE_STEEL_INGOT.get());
        }
    }

    @SubscribeEvent
    public void onLivingFall(LivingFallEvent event) {
        HonorAndFaithModifier.onPlayerFall(event);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
