package org.moliyy.tinkers_rainbow.datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.moliyy.tinkers_rainbow.TinkersRainbow;

@EventBusSubscriber(modid = TinkersRainbow.MODID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

        var gen = event.getGenerator();
        var output = gen.getPackOutput();
        var lookup = event.getLookupProvider();
        var efh = event.getExistingFileHelper();

        if (event.includeClient()) {
            gen.addProvider(true, new ModBlockStateProvider(output, efh));
            gen.addProvider(true, new ModItemModelProvider(output, efh));
        }

        if (event.includeServer()) {
            gen.addProvider(true, new ModBlockTagsProvider(output, lookup, efh));
            gen.addProvider(true, new ModLootTableProvider(output, lookup));
            gen.addProvider(true, new ModWorldGenProvider(output, lookup));
        }
    }
}
