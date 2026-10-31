package org.moliyy.tinkers_rainbow.fluids;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;
import slimeknights.mantle.registration.object.FlowingFluidObject;

import java.util.ArrayList;
import java.util.List;

public class RainbowFluids {

    public static final FluidDeferredRegister FLUIDS =
            new FluidDeferredRegister(TinkersRainbow.MODID);

    public static final List<FlowingFluidObject<BaseFlowingFluid>> ALL = new ArrayList<>();



    public static final FlowingFluidObject<BaseFlowingFluid> molten_blue_steel =
            register("molten_blue_steel", 800, 10, 8000);

    public static final FlowingFluidObject<BaseFlowingFluid> molten_wakamo =
            register("molten_wakamo", 1500, 10, 10000);

    public static final FlowingFluidObject<BaseFlowingFluid> molten_himari =
            register("molten_himari", 1500, 10, 10000);

    public static final FlowingFluidObject<BaseFlowingFluid> molten_nodoka =
            register("molten_nodoka", 1500, 10, 10000);

    public static final FlowingFluidObject<BaseFlowingFluid> molten_sumire =
            register("molten_sumire", 1500, 10, 10000);

    public static final FlowingFluidObject<BaseFlowingFluid> molten_airi =
            register("molten_airi", 1500, 10, 10000);

    public static final FlowingFluidObject<BaseFlowingFluid> molten_aobo =
            register("molten_aoba", 1500, 10, 10000);

    public static final FlowingFluidObject<BaseFlowingFluid> molten_izumi =
            register("molten_izumi", 1500, 10, 10000);

    public static final FlowingFluidObject<BaseFlowingFluid> molten_kisaki =
            register("molten_kisaki", 1500, 10, 10000);

    public static final FlowingFluidObject<BaseFlowingFluid> molten_mine =
            register("molten_mine", 1500, 10, 10000);

    public static final FlowingFluidObject<BaseFlowingFluid> molten_hiyori =
            register("molten_hiyori", 1500, 10, 10000);

    public static final FlowingFluidObject<BaseFlowingFluid> molten_gene_metal =
            register("molten_gene_metal", 900, 10, 6000);

    public static final FlowingFluidObject<BaseFlowingFluid> molten_aurora_crystal =
            register("molten_aurora_crystal", 800, 7, 8000);

    public static final FlowingFluidObject<BaseFlowingFluid> molten_starcore_ingot =
            register("molten_starcore_ingot", 800, 8, 6000);




    private static FluidType.Properties hot(String name, int temp, int visk) {
        return FluidType.Properties.create()
                .density(2000).viscosity(visk).temperature(temp)
                .descriptionId("fluid." + TinkersRainbow.MODID + "." + name)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA);
    }

    private static FlowingFluidObject<BaseFlowingFluid> register(String name, int temp, int light, int visk) {
        FlowingFluidObject<BaseFlowingFluid> fluid = FLUIDS.register(name)
                .type(hot(name, temp, visk).lightLevel(light))
                .block(MapColor.COLOR_BLUE, light)
                .bucket()
                .flowing();
        ALL.add(fluid);
        return fluid;
    }
}
