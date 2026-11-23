/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.dispenser.DispenseItemBehavior
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.item.ArmorItem$Type
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.CreativeModeTab
 *  net.minecraft.world.item.CreativeModeTab$ItemDisplayParameters
 *  net.minecraft.world.item.CreativeModeTab$Output
 *  net.minecraft.world.item.CreativeModeTab$TabVisibility
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.DispenserBlock
 *  net.minecraft.world.level.block.SoundType
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockBehaviour$StatePredicate
 *  net.minecraft.world.level.block.state.properties.NoteBlockInstrument
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.level.material.Fluids
 *  net.minecraft.world.level.material.MapColor
 *  net.minecraft.world.level.material.PushReaction
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
 *  net.neoforged.neoforge.capabilities.Capabilities$FluidHandler
 *  net.neoforged.neoforge.capabilities.Capabilities$ItemHandler
 *  net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
 *  net.neoforged.neoforge.data.event.GatherDataEvent
 *  net.neoforged.neoforge.fluids.capability.IFluidHandler
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.registries.RegisterEvent
 *  slimeknights.mantle.Mantle
 *  slimeknights.mantle.block.GaugeBlock
 *  slimeknights.mantle.data.loadable.record.RecordLoadable
 *  slimeknights.mantle.fluid.transfer.FluidContainerTransferManager
 *  slimeknights.mantle.recipe.helper.LoadableRecipeSerializer
 *  slimeknights.mantle.recipe.helper.TypeAwareRecipeSerializer
 *  slimeknights.mantle.registration.RegistryObject
 *  slimeknights.mantle.registration.object.BuildingBlockObject
 *  slimeknights.mantle.registration.object.EnumObject
 *  slimeknights.mantle.registration.object.FenceBuildingBlockObject
 *  slimeknights.mantle.registration.object.ItemObject
 *  slimeknights.mantle.registration.object.WallBuildingBlockObject
 *  slimeknights.mantle.util.RetexturedHelper
 *  slimeknights.tconstruct.TConstruct
 *  slimeknights.tconstruct.common.TinkerModule
 *  slimeknights.tconstruct.common.TinkerTags$Items
 *  slimeknights.tconstruct.common.registration.CastItemObject
 *  slimeknights.tconstruct.fluids.TinkerFluids
 *  slimeknights.tconstruct.fluids.item.EmptyPotionTransfer
 *  slimeknights.tconstruct.library.recipe.alloying.AlloyRecipe
 *  slimeknights.tconstruct.library.recipe.casting.CastDuplicationRecipe
 *  slimeknights.tconstruct.library.recipe.casting.PotionCastingRecipe
 *  slimeknights.tconstruct.library.recipe.casting.RetexturedCastingRecipe
 *  slimeknights.tconstruct.library.recipe.casting.TipClearingCastingRecipe
 *  slimeknights.tconstruct.library.recipe.casting.TippingCastingRecipe
 *  slimeknights.tconstruct.library.recipe.casting.container.ContainerFillingRecipe
 *  slimeknights.tconstruct.library.recipe.casting.material.CompositeCastingRecipe
 *  slimeknights.tconstruct.library.recipe.casting.material.MaterialCastingRecipe
 *  slimeknights.tconstruct.library.recipe.casting.material.MaterialFluidRecipe
 *  slimeknights.tconstruct.library.recipe.casting.material.PartSwapCastingRecipe
 *  slimeknights.tconstruct.library.recipe.casting.material.ToolCastingRecipe
 *  slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipe
 *  slimeknights.tconstruct.library.recipe.fuel.MeltingFuel
 *  slimeknights.tconstruct.library.recipe.melting.DamageableMeltingRecipe
 *  slimeknights.tconstruct.library.recipe.melting.MaterialMeltingRecipe
 *  slimeknights.tconstruct.library.recipe.melting.MeltingRecipe
 *  slimeknights.tconstruct.library.recipe.melting.OreMeltingRecipe
 *  slimeknights.tconstruct.library.recipe.molding.MoldingRecipe
 *  slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial
 *  slimeknights.tconstruct.library.tools.part.IMaterialItem
 *  slimeknights.tconstruct.library.tools.part.PartCastItem
 *  slimeknights.tconstruct.shared.TinkerCommons
 *  slimeknights.tconstruct.shared.block.ClearGlassPaneBlock
 *  slimeknights.tconstruct.shared.block.PlaceBlockDispenserBehavior
 *  slimeknights.tconstruct.shared.block.SoulGlassPaneBlock
 *  slimeknights.tconstruct.smeltery.block.CastingBasinBlock
 *  slimeknights.tconstruct.smeltery.block.CastingTableBlock
 *  slimeknights.tconstruct.smeltery.block.CastingTankBlock
 *  slimeknights.tconstruct.smeltery.block.ChannelBlock
 *  slimeknights.tconstruct.smeltery.block.FaucetBlock
 *  slimeknights.tconstruct.smeltery.block.FluidCannonBlock
 *  slimeknights.tconstruct.smeltery.block.KnightMetalFluidCannonBlock
 *  slimeknights.tconstruct.smeltery.block.ProxyTankBlock
 *  slimeknights.tconstruct.smeltery.block.RenderingGaugeBlock
 *  slimeknights.tconstruct.smeltery.block.SearedLanternBlock
 *  slimeknights.tconstruct.smeltery.block.component.RetexturedOrientableSmelteryBlock
 *  slimeknights.tconstruct.smeltery.block.component.SearedBlock
 *  slimeknights.tconstruct.smeltery.block.component.SearedDrainBlock
 *  slimeknights.tconstruct.smeltery.block.component.SearedDuctBlock
 *  slimeknights.tconstruct.smeltery.block.component.SearedGlassBlock
 *  slimeknights.tconstruct.smeltery.block.component.SearedLadderBlock
 *  slimeknights.tconstruct.smeltery.block.component.SearedPillarBlock
 *  slimeknights.tconstruct.smeltery.block.component.SearedSoulGlassBlock
 *  slimeknights.tconstruct.smeltery.block.component.SearedTankBlock
 *  slimeknights.tconstruct.smeltery.block.component.SearedTankBlock$TankType
 *  slimeknights.tconstruct.smeltery.block.component.SearedTintedGlassBlock
 *  slimeknights.tconstruct.smeltery.block.controller.AlloyerBlock
 *  slimeknights.tconstruct.smeltery.block.controller.ControllerBlock
 *  slimeknights.tconstruct.smeltery.block.controller.FoundryControllerBlock
 *  slimeknights.tconstruct.smeltery.block.controller.HeaterBlock
 *  slimeknights.tconstruct.smeltery.block.controller.MelterBlock
 *  slimeknights.tconstruct.smeltery.block.controller.SmelteryControllerBlock
 *  slimeknights.tconstruct.smeltery.block.entity.CastingBlockEntity$Basin
 *  slimeknights.tconstruct.smeltery.block.entity.CastingBlockEntity$Table
 *  slimeknights.tconstruct.smeltery.block.entity.CastingTankBlockEntity
 *  slimeknights.tconstruct.smeltery.block.entity.ChannelBlockEntity
 *  slimeknights.tconstruct.smeltery.block.entity.FaucetBlockEntity
 *  slimeknights.tconstruct.smeltery.block.entity.FluidCannonBlockEntity
 *  slimeknights.tconstruct.smeltery.block.entity.GaugeBlockEntity
 *  slimeknights.tconstruct.smeltery.block.entity.HeaterBlockEntity
 *  slimeknights.tconstruct.smeltery.block.entity.LanternBlockEntity
 *  slimeknights.tconstruct.smeltery.block.entity.ProxyTankBlockEntity
 *  slimeknights.tconstruct.smeltery.block.entity.component.DrainBlockEntity
 *  slimeknights.tconstruct.smeltery.block.entity.component.DuctBlockEntity
 *  slimeknights.tconstruct.smeltery.block.entity.component.SmelteryInputOutputBlockEntity$ChuteBlockEntity
 *  slimeknights.tconstruct.smeltery.block.entity.component.SmelteryInputOutputBlockEntity$SmelteryFluidIO
 *  slimeknights.tconstruct.smeltery.block.entity.component.TankBlockEntity
 *  slimeknights.tconstruct.smeltery.block.entity.controller.AlloyerBlockEntity
 *  slimeknights.tconstruct.smeltery.block.entity.controller.FoundryBlockEntity
 *  slimeknights.tconstruct.smeltery.block.entity.controller.MelterBlockEntity
 *  slimeknights.tconstruct.smeltery.item.CopperCanFluidHandler
 *  slimeknights.tconstruct.smeltery.item.CopperCanItem
 *  slimeknights.tconstruct.smeltery.item.DummyMaterialItem
 *  slimeknights.tconstruct.smeltery.item.TankItem
 *  slimeknights.tconstruct.smeltery.item.TankItemFluidHandler
 *  slimeknights.tconstruct.smeltery.menu.AlloyerContainerMenu
 *  slimeknights.tconstruct.smeltery.menu.HeatingStructureContainerMenu
 *  slimeknights.tconstruct.smeltery.menu.MelterContainerMenu
 *  slimeknights.tconstruct.smeltery.menu.SingleItemContainerMenu
 *  slimeknights.tconstruct.tools.TinkerToolParts
 */
package slimeknights.tconstruct.smeltery;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.registries.RegisterEvent;
import slimeknights.mantle.Mantle;
import slimeknights.mantle.block.GaugeBlock;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.fluid.transfer.FluidContainerTransferManager;
import slimeknights.mantle.recipe.helper.LoadableRecipeSerializer;
import slimeknights.mantle.recipe.helper.TypeAwareRecipeSerializer;
import slimeknights.mantle.registration.RegistryObject;
import slimeknights.mantle.registration.object.BuildingBlockObject;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.mantle.registration.object.FenceBuildingBlockObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.mantle.registration.object.WallBuildingBlockObject;
import slimeknights.mantle.util.RetexturedHelper;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerModule;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.fluids.item.EmptyPotionTransfer;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipe;
import slimeknights.tconstruct.library.recipe.casting.CastDuplicationRecipe;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipe;
import slimeknights.tconstruct.library.recipe.casting.PotionCastingRecipe;
import slimeknights.tconstruct.library.recipe.casting.RetexturedCastingRecipe;
import slimeknights.tconstruct.library.recipe.casting.TipClearingCastingRecipe;
import slimeknights.tconstruct.library.recipe.casting.TippingCastingRecipe;
import slimeknights.tconstruct.library.recipe.casting.container.ContainerFillingRecipe;
import slimeknights.tconstruct.library.recipe.casting.material.CompositeCastingRecipe;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialCastingRecipe;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialFluidRecipe;
import slimeknights.tconstruct.library.recipe.casting.material.PartSwapCastingRecipe;
import slimeknights.tconstruct.library.recipe.casting.material.ToolCastingRecipe;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipe;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuel;
import slimeknights.tconstruct.library.recipe.melting.DamageableMeltingRecipe;
import slimeknights.tconstruct.library.recipe.melting.MaterialMeltingRecipe;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipe;
import slimeknights.tconstruct.library.recipe.melting.OreMeltingRecipe;
import slimeknights.tconstruct.library.recipe.molding.MoldingRecipe;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.library.tools.part.PartCastItem;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.block.ClearGlassPaneBlock;
import slimeknights.tconstruct.shared.block.PlaceBlockDispenserBehavior;
import slimeknights.tconstruct.shared.block.SoulGlassPaneBlock;
import slimeknights.tconstruct.smeltery.block.CastingBasinBlock;
import slimeknights.tconstruct.smeltery.block.CastingTableBlock;
import slimeknights.tconstruct.smeltery.block.CastingTankBlock;
import slimeknights.tconstruct.smeltery.block.ChannelBlock;
import slimeknights.tconstruct.smeltery.block.FaucetBlock;
import slimeknights.tconstruct.smeltery.block.FluidCannonBlock;
import slimeknights.tconstruct.smeltery.block.KnightMetalFluidCannonBlock;
import slimeknights.tconstruct.smeltery.block.ProxyTankBlock;
import slimeknights.tconstruct.smeltery.block.RenderingGaugeBlock;
import slimeknights.tconstruct.smeltery.block.SearedLanternBlock;
import slimeknights.tconstruct.smeltery.block.component.RetexturedOrientableSmelteryBlock;
import slimeknights.tconstruct.smeltery.block.component.SearedBlock;
import slimeknights.tconstruct.smeltery.block.component.SearedDrainBlock;
import slimeknights.tconstruct.smeltery.block.component.SearedDuctBlock;
import slimeknights.tconstruct.smeltery.block.component.SearedGlassBlock;
import slimeknights.tconstruct.smeltery.block.component.SearedLadderBlock;
import slimeknights.tconstruct.smeltery.block.component.SearedPillarBlock;
import slimeknights.tconstruct.smeltery.block.component.SearedSoulGlassBlock;
import slimeknights.tconstruct.smeltery.block.component.SearedTankBlock;
import slimeknights.tconstruct.smeltery.block.component.SearedTintedGlassBlock;
import slimeknights.tconstruct.smeltery.block.controller.AlloyerBlock;
import slimeknights.tconstruct.smeltery.block.controller.ControllerBlock;
import slimeknights.tconstruct.smeltery.block.controller.FoundryControllerBlock;
import slimeknights.tconstruct.smeltery.block.controller.HeaterBlock;
import slimeknights.tconstruct.smeltery.block.controller.MelterBlock;
import slimeknights.tconstruct.smeltery.block.controller.SmelteryControllerBlock;
import slimeknights.tconstruct.smeltery.block.entity.CastingBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.CastingTankBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.ChannelBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.FaucetBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.FluidCannonBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.GaugeBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.HeaterBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.LanternBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.ProxyTankBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.component.DrainBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.component.DuctBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.component.SmelteryComponentBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.component.SmelteryInputOutputBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.component.TankBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.controller.AlloyerBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.controller.FoundryBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.controller.MelterBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.controller.SmelteryBlockEntity;
import slimeknights.tconstruct.smeltery.item.CopperCanFluidHandler;
import slimeknights.tconstruct.smeltery.item.CopperCanItem;
import slimeknights.tconstruct.smeltery.item.DummyMaterialItem;
import slimeknights.tconstruct.smeltery.item.TankItem;
import slimeknights.tconstruct.smeltery.item.TankItemFluidHandler;
import slimeknights.tconstruct.smeltery.menu.AlloyerContainerMenu;
import slimeknights.tconstruct.smeltery.menu.HeatingStructureContainerMenu;
import slimeknights.tconstruct.smeltery.menu.MelterContainerMenu;
import slimeknights.tconstruct.smeltery.menu.SingleItemContainerMenu;
import slimeknights.tconstruct.tools.TinkerToolParts;

public final class TinkerSmeltery
extends TinkerModule {
    private static final BlockBehaviour.StatePredicate NEVER = (state, level, pos) -> false;
    public static final RegistryObject<CreativeModeTab> tabSmeltery = CREATIVE_TABS.register("smeltery", () -> CreativeModeTab.builder().title((Component)TConstruct.makeTranslation((String)"itemGroup", (String)"smeltery")).icon(() -> new ItemStack(smelteryController)).displayItems(TinkerSmeltery::addTabItems).withTabsBefore(new ResourceLocation[]{TinkerToolParts.tabToolParts.getId()}).build());
    public static final ItemObject<Block> grout = BLOCKS.register("grout", TinkerSmeltery.builder((MapColor)MapColor.COLOR_LIGHT_GRAY, (SoundType)SoundType.SAND).instrument(NoteBlockInstrument.SNARE).strength(3.0f).friction(0.8f), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<Block> netherGrout = BLOCKS.register("nether_grout", TinkerSmeltery.builder((MapColor)MapColor.COLOR_BROWN, (SoundType)SoundType.SOUL_SOIL).instrument(NoteBlockInstrument.SNARE).strength(3.0f).friction(0.8f), TOOLTIP_BLOCK_ITEM);
    public static final BuildingBlockObject searedStone;
    public static final BuildingBlockObject searedPaver;
    public static final WallBuildingBlockObject searedCobble;
    public static final WallBuildingBlockObject searedBricks;
    public static final ItemObject<Block> searedCrackedBricks;
    public static final ItemObject<Block> searedFancyBricks;
    public static final ItemObject<Block> searedTriangleBricks;
    public static final ItemObject<Block> searedLamp;
    public static final ItemObject<Block> scorchedStone;
    public static final ItemObject<Block> polishedScorchedStone;
    public static final ItemObject<Block> chiseledScorchedBricks;
    public static final FenceBuildingBlockObject scorchedBricks;
    public static final BuildingBlockObject scorchedRoad;
    public static final ItemObject<Block> scorchedLamp;
    public static final ItemObject<SearedGlassBlock> searedGlass;
    public static final ItemObject<SearedGlassBlock> scorchedGlass;
    public static final ItemObject<ClearGlassPaneBlock> searedGlassPane;
    public static final ItemObject<ClearGlassPaneBlock> scorchedGlassPane;
    public static final ItemObject<SearedTintedGlassBlock> searedTintedGlass;
    public static final ItemObject<SearedTintedGlassBlock> scorchedTintedGlass;
    public static final ItemObject<SearedSoulGlassBlock> searedSoulGlass;
    public static final ItemObject<SearedSoulGlassBlock> scorchedSoulGlass;
    public static final ItemObject<SoulGlassPaneBlock> searedSoulGlassPane;
    public static final ItemObject<SoulGlassPaneBlock> scorchedSoulGlassPane;
    public static final ItemObject<Block> searedDrain;
    public static final ItemObject<Block> searedDuct;
    public static final ItemObject<Block> searedChute;
    public static final ItemObject<Block> scorchedDrain;
    public static final ItemObject<Block> scorchedDuct;
    public static final ItemObject<Block> scorchedChute;
    public static final ItemObject<SearedLadderBlock> searedLadder;
    public static final ItemObject<SearedLadderBlock> scorchedLadder;
    public static final ItemObject<FaucetBlock> searedFaucet;
    public static final ItemObject<FaucetBlock> scorchedFaucet;
    public static final ItemObject<ChannelBlock> searedChannel;
    public static final ItemObject<ChannelBlock> scorchedChannel;
    public static final ItemObject<CastingBasinBlock> searedBasin;
    public static final ItemObject<CastingBasinBlock> scorchedBasin;
    public static final ItemObject<CastingTableBlock> searedTable;
    public static final ItemObject<CastingTableBlock> scorchedTable;
    public static final ItemObject<ProxyTankBlock> scorchedProxyTank;
    public static final EnumObject<SearedTankBlock.TankType, SearedTankBlock> searedTank;
    public static final EnumObject<SearedTankBlock.TankType, SearedTankBlock> scorchedTank;
    public static final ItemObject<CastingTankBlock> searedCastingTank;
    public static final ItemObject<FluidCannonBlock> searedFluidCannon;
    public static final ItemObject<FluidCannonBlock> scorchedFluidCannon;
    public static final ItemObject<FluidCannonBlock> endFluidCannon;
    public static final ItemObject<SearedLanternBlock> searedLantern;
    public static final ItemObject<SearedLanternBlock> scorchedLantern;
    public static final ItemObject<GaugeBlock> copperGauge;
    public static final ItemObject<GaugeBlock> obsidianGauge;
    public static final ItemObject<SmelteryControllerBlock> smelteryController;
    public static final ItemObject<FoundryControllerBlock> foundryController;
    public static final ItemObject<MelterBlock> searedMelter;
    public static final ItemObject<HeaterBlock> searedHeater;
    public static final ItemObject<AlloyerBlock> scorchedAlloyer;
    public static final RegistryObject<BlockEntityType<SmelteryComponentBlockEntity>> smelteryComponent;
    public static final RegistryObject<BlockEntityType<SmelteryInputOutputBlockEntity.SmelteryFluidIO>> drain;
    public static final RegistryObject<BlockEntityType<SmelteryInputOutputBlockEntity.ChuteBlockEntity>> chute;
    public static final RegistryObject<BlockEntityType<DuctBlockEntity>> duct;
    public static final RegistryObject<BlockEntityType<TankBlockEntity>> tank;
    public static final RegistryObject<BlockEntityType<FluidCannonBlockEntity>> fluidCannon;
    public static final RegistryObject<BlockEntityType<LanternBlockEntity>> lantern;
    public static final RegistryObject<BlockEntityType<MelterBlockEntity>> melter;
    public static final RegistryObject<BlockEntityType<SmelteryBlockEntity>> smeltery;
    public static final RegistryObject<BlockEntityType<FoundryBlockEntity>> foundry;
    public static final RegistryObject<BlockEntityType<HeaterBlockEntity>> heater;
    public static final RegistryObject<BlockEntityType<AlloyerBlockEntity>> alloyer;
    public static final RegistryObject<BlockEntityType<FaucetBlockEntity>> faucet;
    public static final RegistryObject<BlockEntityType<ChannelBlockEntity>> channel;
    public static final RegistryObject<BlockEntityType<GaugeBlockEntity>> gauge;
    public static final RegistryObject<BlockEntityType<CastingBlockEntity>> basin;
    public static final RegistryObject<BlockEntityType<CastingBlockEntity>> table;
    public static final RegistryObject<BlockEntityType<ProxyTankBlockEntity>> proxyTank;
    public static final RegistryObject<BlockEntityType<CastingTankBlockEntity>> castingTank;
    public static final ItemObject<Item> searedBrick;
    public static final ItemObject<Item> scorchedBrick;
    public static final ItemObject<Item> copperCan;
    public static final ItemObject<Item> blankSandCast;
    public static final ItemObject<Item> blankRedSandCast;
    public static final CastItemObject ingotCast;
    public static final CastItemObject nuggetCast;
    public static final CastItemObject gemCast;
    public static final CastItemObject rodCast;
    public static final CastItemObject repairKitCast;
    public static final CastItemObject plateCast;
    public static final CastItemObject gearCast;
    public static final CastItemObject coinCast;
    public static final CastItemObject wireCast;
    public static final CastItemObject pickHeadCast;
    public static final CastItemObject smallAxeHeadCast;
    public static final CastItemObject smallBladeCast;
    public static final CastItemObject adzeHeadCast;
    public static final CastItemObject hammerHeadCast;
    public static final CastItemObject broadBladeCast;
    public static final CastItemObject broadAxeHeadCast;
    public static final CastItemObject largePlateCast;
    public static final CastItemObject toolBindingCast;
    public static final CastItemObject toughBindingCast;
    public static final CastItemObject toolHandleCast;
    public static final CastItemObject toughHandleCast;
    public static final CastItemObject bowLimbCast;
    public static final CastItemObject bowGripCast;
    public static final ItemObject<Item> arrowCast;
    public static final CastItemObject helmetPlatingCast;
    public static final CastItemObject chestplatePlatingCast;
    public static final CastItemObject leggingsPlatingCast;
    public static final CastItemObject bootsPlatingCast;
    public static final CastItemObject mailleCast;
    public static final EnumObject<ArmorItem.Type, DummyMaterialItem> dummyPlating;
    public static final RegistryObject<TypeAwareRecipeSerializer<ItemCastingRecipe>> basinRecipeSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<ItemCastingRecipe>> tableRecipeSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<ContainerFillingRecipe>> basinFillingRecipeSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<ContainerFillingRecipe>> tableFillingRecipeSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<CastDuplicationRecipe>> basinDuplicationRecipeSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<CastDuplicationRecipe>> tableDuplicationRecipeSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<PotionCastingRecipe>> basinPotionRecipeSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<PotionCastingRecipe>> tablePotionRecipeSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<TippingCastingRecipe>> basinTippingRecipeSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<TippingCastingRecipe>> tableTippingRecipeSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<TipClearingCastingRecipe>> basinTipClearingRecipeSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<TipClearingCastingRecipe>> tableTipClearingRecipeSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<RetexturedCastingRecipe>> retexturedBasinRecipeSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<RetexturedCastingRecipe>> retexturedTableRecipeSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<MaterialCastingRecipe>> basinMaterialSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<MaterialCastingRecipe>> tableMaterialSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<CompositeCastingRecipe>> basinCompositeSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<CompositeCastingRecipe>> tableCompositeSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<ToolCastingRecipe>> basinToolSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<ToolCastingRecipe>> tableToolSerializer;
    public static final RegistryObject<RecipeSerializer<MaterialFluidRecipe>> materialFluidRecipe;
    public static final RegistryObject<TypeAwareRecipeSerializer<PartSwapCastingRecipe>> basinPartSwappingSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<PartSwapCastingRecipe>> tablePartSwappingSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<MoldingRecipe>> moldingBasinSerializer;
    public static final RegistryObject<TypeAwareRecipeSerializer<MoldingRecipe>> moldingTableSerializer;
    public static final RegistryObject<RecipeSerializer<MeltingRecipe>> meltingSerializer;
    public static final RegistryObject<RecipeSerializer<OreMeltingRecipe>> oreMeltingSerializer;
    public static final RegistryObject<RecipeSerializer<DamageableMeltingRecipe>> damagableMeltingSerializer;
    public static final RegistryObject<RecipeSerializer<MaterialMeltingRecipe>> materialMeltingSerializer;
    public static final RegistryObject<RecipeSerializer<MeltingFuel>> fuelSerializer;
    public static final RegistryObject<RecipeSerializer<EntityMeltingRecipe>> entityMeltingSerializer;
    public static final RegistryObject<RecipeSerializer<AlloyRecipe>> alloyingSerializer;
    public static final RegistryObject<MenuType<MelterContainerMenu>> melterContainer;
    public static final RegistryObject<MenuType<HeatingStructureContainerMenu>> smelteryContainer;
    public static final RegistryObject<MenuType<SingleItemContainerMenu>> singleItemContainer;
    public static final RegistryObject<MenuType<AlloyerContainerMenu>> alloyerContainer;

    @SubscribeEvent
    void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Consumer<Block> dispenserBehavior = block -> DispenserBlock.registerBehavior((ItemLike)block.asItem(), (DispenseItemBehavior)PlaceBlockDispenserBehavior.INSTANCE);
            searedTank.forEach(dispenserBehavior);
            scorchedTank.forEach(dispenserBehavior);
        });
    }

    @SubscribeEvent
    void registerSerializers(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
            FluidContainerTransferManager.TRANSFER_LOADERS.registerDeserializer(EmptyPotionTransfer.ID, EmptyPotionTransfer.DESERIALIZER);
        }
    }

    @SubscribeEvent
    void gatherData(GatherDataEvent event) {
    }

    @SubscribeEvent
    void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType)tank.get(), (blockEntity, side) -> blockEntity.getFluidHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType)lantern.get(), (blockEntity, side) -> blockEntity.getFluidHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType)fluidCannon.get(), (blockEntity, side) -> blockEntity.getFluidHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)fluidCannon.get(), (blockEntity, side) -> blockEntity.getItemHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType)castingTank.get(), (blockEntity, side) -> blockEntity.getTank());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)castingTank.get(), (blockEntity, side) -> blockEntity.getItemHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType)basin.get(), (blockEntity, side) -> blockEntity.getTank());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType)table.get(), (blockEntity, side) -> blockEntity.getTank());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType)proxyTank.get(), (blockEntity, side) -> blockEntity.getItemTank());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)proxyTank.get(), (blockEntity, side) -> blockEntity.getItemTank());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType)melter.get(), (blockEntity, side) -> blockEntity.getTank());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)melter.get(), (blockEntity, side) -> blockEntity.getItemHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType)alloyer.get(), (blockEntity, side) -> blockEntity.getTank());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)smeltery.get(), (blockEntity, side) -> blockEntity.getMeltingInventory());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)foundry.get(), (blockEntity, side) -> blockEntity.getMeltingInventory());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)heater.get(), (blockEntity, side) -> blockEntity.getItemHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType)drain.get(), (blockEntity, side) -> (IFluidHandler)blockEntity.getHandler(side));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType)duct.get(), (blockEntity, side) -> (IFluidHandler)blockEntity.getHandler(side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)duct.get(), (blockEntity, side) -> blockEntity.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)chute.get(), (blockEntity, side) -> (IItemHandler)blockEntity.getHandler(side));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType)channel.get(), (blockEntity, side) -> blockEntity.getFluidHandler(side));
        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ignored) -> new CopperCanFluidHandler(stack), new ItemLike[]{copperCan});
        searedTank.forEach(item -> event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ignored) -> new TankItemFluidHandler((TankItem)stack.getItem(), stack), new ItemLike[]{item}));
        scorchedTank.forEach(item -> event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ignored) -> new TankItemFluidHandler((TankItem)stack.getItem(), stack), new ItemLike[]{item}));
        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ignored) -> new TankItemFluidHandler((TankItem)stack.getItem(), stack), new ItemLike[]{searedCastingTank, searedFluidCannon, scorchedFluidCannon, endFluidCannon, searedLantern, scorchedLantern});
    }

    private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        output.accept(grout);
        output.accept(searedBrick);
        output.accept(netherGrout);
        output.accept(scorchedBrick);
        output.accept(copperCan);
        output.accept(searedMelter);
        output.accept(searedHeater);
        output.accept(scorchedAlloyer);
        output.accept(smelteryController);
        output.accept(foundryController);
        output.accept(searedDrain);
        output.accept(searedDuct);
        output.accept(searedChute);
        output.accept(scorchedDrain);
        output.accept(scorchedDuct);
        output.accept(scorchedChute);
        output.accept(copperGauge);
        output.accept(obsidianGauge);
        TinkerSmeltery.accept((CreativeModeTab.Output)output, searedTank);
        output.accept(TankItem.fillTank(searedTank, (SearedTankBlock.TankType)SearedTankBlock.TankType.FUEL_TANK, (Fluid)Fluids.LAVA));
        output.accept(TankItem.fillTank(searedTank, (SearedTankBlock.TankType)SearedTankBlock.TankType.FUEL_TANK, (Fluid)TinkerFluids.blazingBlood.get()));
        output.accept(searedLantern);
        TinkerSmeltery.accept((CreativeModeTab.Output)output, scorchedTank);
        output.accept(TankItem.fillTank(scorchedTank, (SearedTankBlock.TankType)SearedTankBlock.TankType.FUEL_TANK, (Fluid)Fluids.LAVA));
        output.accept(TankItem.fillTank(scorchedTank, (SearedTankBlock.TankType)SearedTankBlock.TankType.FUEL_TANK, (Fluid)TinkerFluids.blazingBlood.get()));
        output.accept(scorchedLantern);
        output.accept(searedFaucet);
        output.accept(scorchedFaucet);
        output.accept(searedChannel);
        output.accept(scorchedChannel);
        output.accept(searedTable);
        output.accept(scorchedTable);
        output.accept((ItemLike)TinkerCommons.goldBars, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        output.accept(searedBasin);
        output.accept(scorchedBasin);
        output.accept((ItemLike)TinkerCommons.goldPlatform, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        output.accept(searedCastingTank);
        output.accept(scorchedProxyTank);
        output.accept(searedFluidCannon);
        output.accept(scorchedFluidCannon);
        output.accept(endFluidCannon);
        TinkerSmeltery.accept((CreativeModeTab.Output)output, (BuildingBlockObject)searedBricks);
        TinkerSmeltery.accept((CreativeModeTab.Output)output, (BuildingBlockObject)searedStone);
        output.accept(searedCrackedBricks);
        output.accept(searedFancyBricks);
        output.accept(searedTriangleBricks);
        TinkerSmeltery.accept((CreativeModeTab.Output)output, (BuildingBlockObject)searedCobble);
        TinkerSmeltery.accept((CreativeModeTab.Output)output, (BuildingBlockObject)searedPaver);
        output.accept(searedLamp);
        output.accept(searedLadder);
        output.accept(searedGlass);
        output.accept(searedTintedGlass);
        output.accept(searedSoulGlass);
        output.accept(searedGlassPane);
        output.accept(searedSoulGlassPane);
        TinkerSmeltery.accept((CreativeModeTab.Output)output, (BuildingBlockObject)scorchedBricks);
        output.accept(chiseledScorchedBricks);
        output.accept(scorchedStone);
        output.accept(polishedScorchedStone);
        TinkerSmeltery.accept((CreativeModeTab.Output)output, (BuildingBlockObject)scorchedRoad);
        output.accept(scorchedLamp);
        output.accept(scorchedLadder);
        output.accept(scorchedGlass);
        output.accept(scorchedTintedGlass);
        output.accept(scorchedSoulGlass);
        output.accept(scorchedGlassPane);
        output.accept(scorchedSoulGlassPane);
        TinkerSmeltery.addCasts(output, ItemObject::get);
        output.accept(blankSandCast);
        TinkerSmeltery.addCasts(output, CastItemObject::getSand);
        output.accept(blankRedSandCast);
        TinkerSmeltery.addCasts(output, CastItemObject::getRedSand);
        Predicate<ItemStack> variant = stack -> {
            output.accept(stack);
            return false;
        };
        RetexturedHelper.addTagVariants(variant, smelteryController, (TagKey)TinkerTags.Items.SEARED_BLOCKS);
        RetexturedHelper.addTagVariants(variant, searedDrain, (TagKey)TinkerTags.Items.SEARED_BLOCKS);
        RetexturedHelper.addTagVariants(variant, searedDuct, (TagKey)TinkerTags.Items.SEARED_BLOCKS);
        RetexturedHelper.addTagVariants(variant, searedChute, (TagKey)TinkerTags.Items.SEARED_BLOCKS);
        RetexturedHelper.addTagVariants(variant, foundryController, (TagKey)TinkerTags.Items.SCORCHED_BLOCKS);
        RetexturedHelper.addTagVariants(variant, scorchedDrain, (TagKey)TinkerTags.Items.SCORCHED_BLOCKS);
        RetexturedHelper.addTagVariants(variant, scorchedDuct, (TagKey)TinkerTags.Items.SCORCHED_BLOCKS);
        RetexturedHelper.addTagVariants(variant, scorchedChute, (TagKey)TinkerTags.Items.SCORCHED_BLOCKS);
    }

    private static void addCasts(CreativeModeTab.Output output, Function<CastItemObject, ItemLike> getter) {
        TinkerSmeltery.accept(output, getter, ingotCast);
        TinkerSmeltery.accept(output, getter, nuggetCast);
        TinkerSmeltery.accept(output, getter, gemCast);
        TinkerSmeltery.accept(output, getter, rodCast);
        TinkerSmeltery.accept(output, getter, repairKitCast);
        TinkerSmeltery.acceptIfTag(output, getter, plateCast);
        TinkerSmeltery.acceptIfTag(output, getter, gearCast);
        TinkerSmeltery.acceptIfTag(output, getter, coinCast);
        TinkerSmeltery.acceptIfTag(output, getter, wireCast);
        TinkerSmeltery.accept(output, getter, pickHeadCast);
        TinkerSmeltery.accept(output, getter, smallAxeHeadCast);
        TinkerSmeltery.accept(output, getter, smallBladeCast);
        TinkerSmeltery.accept(output, getter, adzeHeadCast);
        TinkerSmeltery.accept(output, getter, hammerHeadCast);
        TinkerSmeltery.accept(output, getter, broadAxeHeadCast);
        TinkerSmeltery.accept(output, getter, broadBladeCast);
        TinkerSmeltery.accept(output, getter, largePlateCast);
        TinkerSmeltery.accept(output, getter, toolHandleCast);
        TinkerSmeltery.accept(output, getter, toolBindingCast);
        TinkerSmeltery.accept(output, getter, toughHandleCast);
        TinkerSmeltery.accept(output, getter, toughBindingCast);
        TinkerSmeltery.accept(output, getter, bowLimbCast);
        TinkerSmeltery.accept(output, getter, bowGripCast);
        output.accept(arrowCast);
        TinkerSmeltery.accept(output, getter, helmetPlatingCast);
        TinkerSmeltery.accept(output, getter, chestplatePlatingCast);
        TinkerSmeltery.accept(output, getter, leggingsPlatingCast);
        TinkerSmeltery.accept(output, getter, bootsPlatingCast);
        TinkerSmeltery.accept(output, getter, mailleCast);
    }

    private static void accept(CreativeModeTab.Output output, Function<CastItemObject, ItemLike> getter, CastItemObject cast) {
        output.accept(getter.apply(cast));
    }

    private static void acceptIfTag(CreativeModeTab.Output output, Function<CastItemObject, ItemLike> getter, CastItemObject cast) {
        TinkerSmeltery.acceptIfTag((CreativeModeTab.Output)output, (ItemLike)getter.apply(cast), (TagKey)ItemTags.create((ResourceLocation)Mantle.commonResource((String)(cast.getName().getPath() + "s"))));
    }

    private static BlockBehaviour.Properties structureProps(MapColor color, SoundType sound) {
        return TinkerSmeltery.builder((MapColor)color, (SoundType)sound).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().isValidSpawn(SearedBlock.VALID_SPAWN);
    }

    private static BlockBehaviour.Properties structureNonSolid(MapColor color, SoundType sound) {
        return TinkerSmeltery.structureProps(color, sound).isValidSpawn(Blocks::never).isRedstoneConductor(NEVER).isSuffocating(NEVER).isViewBlocking(NEVER).noOcclusion().forceSolidOn();
    }

    private static BlockBehaviour.Properties searedSolidProps(int factor) {
        return TinkerSmeltery.structureProps(MapColor.COLOR_GRAY, SoundType.METAL).strength(3.0f * (float)factor, 9.0f * (float)factor);
    }

    private static BlockBehaviour.Properties searedNonSolidProps(SoundType sound) {
        return TinkerSmeltery.structureNonSolid(MapColor.COLOR_GRAY, sound).strength(3.0f, 9.0f);
    }

    private static BlockBehaviour.Properties scorchedSolidProps(int factor) {
        return TinkerSmeltery.structureProps(MapColor.TERRACOTTA_BROWN, SoundType.BASALT).strength(2.5f * (float)factor, 8.0f * (float)factor);
    }

    private static BlockBehaviour.Properties scorchedNonSolidProps(SoundType sound) {
        return TinkerSmeltery.structureNonSolid(MapColor.TERRACOTTA_BROWN, sound).strength(2.5f, 8.0f);
    }

    private static /* synthetic */ AlloyerBlock lambda$static$60(Supplier scorched) {
        return new AlloyerBlock(((BlockBehaviour.Properties)scorched.get()).noOcclusion());
    }

    private static /* synthetic */ HeaterBlock lambda$static$59(Supplier seared) {
        return new HeaterBlock((BlockBehaviour.Properties)seared.get());
    }

    private static /* synthetic */ MelterBlock lambda$static$58(Supplier seared) {
        return new MelterBlock(((BlockBehaviour.Properties)seared.get()).noOcclusion());
    }

    private static /* synthetic */ FoundryControllerBlock lambda$static$57(Supplier scorched) {
        return new FoundryControllerBlock((BlockBehaviour.Properties)scorched.get());
    }

    private static /* synthetic */ SmelteryControllerBlock lambda$static$56(Supplier seared) {
        return new SmelteryControllerBlock((BlockBehaviour.Properties)seared.get());
    }

    private static /* synthetic */ ProxyTankBlock lambda$static$38(BlockBehaviour.Properties scorched) {
        return new ProxyTankBlock(scorched);
    }

    private static /* synthetic */ CastingTableBlock lambda$static$37(BlockBehaviour.Properties scorched) {
        return new CastingTableBlock(scorched, true);
    }

    private static /* synthetic */ CastingBasinBlock lambda$static$36(BlockBehaviour.Properties scorched) {
        return new CastingBasinBlock(scorched, true);
    }

    private static /* synthetic */ ChannelBlock lambda$static$35(BlockBehaviour.Properties scorched) {
        return new ChannelBlock(scorched);
    }

    private static /* synthetic */ FaucetBlock lambda$static$34(BlockBehaviour.Properties scorched) {
        return new FaucetBlock(scorched);
    }

    private static /* synthetic */ SearedLadderBlock lambda$static$33(BlockBehaviour.Properties scorched) {
        return new SearedLadderBlock(scorched);
    }

    private static /* synthetic */ CastingTableBlock lambda$static$32(BlockBehaviour.Properties seared) {
        return new CastingTableBlock(seared, false);
    }

    private static /* synthetic */ CastingBasinBlock lambda$static$31(BlockBehaviour.Properties seared) {
        return new CastingBasinBlock(seared, false);
    }

    private static /* synthetic */ ChannelBlock lambda$static$30(BlockBehaviour.Properties seared) {
        return new ChannelBlock(seared);
    }

    private static /* synthetic */ FaucetBlock lambda$static$29(BlockBehaviour.Properties seared) {
        return new FaucetBlock(seared);
    }

    private static /* synthetic */ SearedLadderBlock lambda$static$28(BlockBehaviour.Properties seared) {
        return new SearedLadderBlock(seared);
    }

    private static /* synthetic */ Block lambda$static$27(BlockBehaviour.Properties scorched) {
        return new RetexturedOrientableSmelteryBlock(scorched, SmelteryInputOutputBlockEntity.ChuteBlockEntity::new);
    }

    private static /* synthetic */ Block lambda$static$26(BlockBehaviour.Properties scorched) {
        return new SearedDuctBlock(scorched);
    }

    private static /* synthetic */ Block lambda$static$25(BlockBehaviour.Properties scorched) {
        return new SearedDrainBlock(scorched);
    }

    private static /* synthetic */ Block lambda$static$24(BlockBehaviour.Properties seared) {
        return new RetexturedOrientableSmelteryBlock(seared, SmelteryInputOutputBlockEntity.ChuteBlockEntity::new);
    }

    private static /* synthetic */ Block lambda$static$23(BlockBehaviour.Properties seared) {
        return new SearedDuctBlock(seared);
    }

    private static /* synthetic */ Block lambda$static$22(BlockBehaviour.Properties seared) {
        return new SearedDrainBlock(seared);
    }

    private static /* synthetic */ SoulGlassPaneBlock lambda$static$21(BlockBehaviour.Properties scorched) {
        return new SoulGlassPaneBlock(scorched);
    }

    private static /* synthetic */ SearedSoulGlassBlock lambda$static$20(BlockBehaviour.Properties scorched) {
        return new SearedSoulGlassBlock(scorched);
    }

    private static /* synthetic */ SoulGlassPaneBlock lambda$static$18(BlockBehaviour.Properties seared) {
        return new SoulGlassPaneBlock(seared);
    }

    private static /* synthetic */ SearedSoulGlassBlock lambda$static$17(BlockBehaviour.Properties seared) {
        return new SearedSoulGlassBlock(seared);
    }

    private static /* synthetic */ ClearGlassPaneBlock lambda$static$15(BlockBehaviour.Properties scorched) {
        return new ClearGlassPaneBlock(scorched);
    }

    private static /* synthetic */ SearedTintedGlassBlock lambda$static$14(BlockBehaviour.Properties scorched) {
        return new SearedTintedGlassBlock(scorched);
    }

    private static /* synthetic */ SearedGlassBlock lambda$static$13(BlockBehaviour.Properties scorched) {
        return new SearedGlassBlock(scorched);
    }

    private static /* synthetic */ SearedTintedGlassBlock lambda$static$12(BlockBehaviour.Properties seared) {
        return new SearedTintedGlassBlock(seared);
    }

    private static /* synthetic */ ClearGlassPaneBlock lambda$static$11(BlockBehaviour.Properties seared) {
        return new ClearGlassPaneBlock(seared);
    }

    private static /* synthetic */ SearedGlassBlock lambda$static$10(BlockBehaviour.Properties seared) {
        return new SearedGlassBlock(seared);
    }

    static {
        BlockBehaviour.Properties properties = TinkerSmeltery.searedSolidProps(1);
        Supplier<SearedBlock> searedBlock = () -> new SearedBlock(properties, false);
        searedStone = BLOCKS.registerBuilding("seared_stone", searedBlock, TOOLTIP_BLOCK_ITEM);
        searedCobble = BLOCKS.registerWallBuilding("seared_cobble", searedBlock, TOOLTIP_BLOCK_ITEM);
        searedPaver = BLOCKS.registerBuilding("seared_paver", searedBlock, TOOLTIP_BLOCK_ITEM);
        searedBricks = BLOCKS.registerWallBuilding("seared_bricks", searedBlock, TOOLTIP_BLOCK_ITEM);
        searedCrackedBricks = BLOCKS.register("seared_cracked_bricks", searedBlock, TOOLTIP_BLOCK_ITEM);
        searedFancyBricks = BLOCKS.register("seared_fancy_bricks", searedBlock, TOOLTIP_BLOCK_ITEM);
        searedTriangleBricks = BLOCKS.register("seared_triangle_bricks", searedBlock, TOOLTIP_BLOCK_ITEM);
        searedLamp = BLOCKS.register("seared_lamp", () -> new SearedBlock(TinkerSmeltery.searedSolidProps(1).lightLevel(state -> 15), false), TOOLTIP_BLOCK_ITEM);
        properties = TinkerSmeltery.scorchedSolidProps(1);
        Supplier<SearedPillarBlock> pillar = () -> new SearedPillarBlock(properties, false);
        scorchedStone = BLOCKS.register("scorched_stone", pillar, TOOLTIP_BLOCK_ITEM);
        polishedScorchedStone = BLOCKS.register("polished_scorched_stone", pillar, TOOLTIP_BLOCK_ITEM);
        Supplier<SearedBlock> block = () -> new SearedBlock(properties, false);
        scorchedBricks = BLOCKS.registerFenceBuilding("scorched_bricks", block, TOOLTIP_BLOCK_ITEM);
        scorchedRoad = BLOCKS.registerBuilding("scorched_road", block, TOOLTIP_BLOCK_ITEM);
        chiseledScorchedBricks = BLOCKS.register("chiseled_scorched_bricks", block, TOOLTIP_BLOCK_ITEM);
        scorchedLamp = BLOCKS.register("scorched_lamp", () -> new SearedBlock(TinkerSmeltery.scorchedSolidProps(1).lightLevel(state -> 15), false), TOOLTIP_BLOCK_ITEM);
        Object seared = TinkerSmeltery.searedNonSolidProps(SoundType.GLASS);
        searedGlass = BLOCKS.register("seared_glass", () -> TinkerSmeltery.lambda$static$10((BlockBehaviour.Properties)seared), TOOLTIP_BLOCK_ITEM);
        searedGlassPane = BLOCKS.register("seared_glass_pane", () -> TinkerSmeltery.lambda$static$11((BlockBehaviour.Properties)seared), TOOLTIP_BLOCK_ITEM);
        searedTintedGlass = BLOCKS.register("seared_tinted_glass", () -> TinkerSmeltery.lambda$static$12((BlockBehaviour.Properties)seared), TOOLTIP_BLOCK_ITEM);
        Object scorched = TinkerSmeltery.scorchedNonSolidProps(SoundType.GLASS);
        scorchedGlass = BLOCKS.register("scorched_glass", () -> TinkerSmeltery.lambda$static$13((BlockBehaviour.Properties)scorched), TOOLTIP_BLOCK_ITEM);
        scorchedTintedGlass = BLOCKS.register("scorched_tinted_glass", () -> TinkerSmeltery.lambda$static$14((BlockBehaviour.Properties)scorched), TOOLTIP_BLOCK_ITEM);
        scorchedGlassPane = BLOCKS.register("scorched_glass_pane", () -> TinkerSmeltery.lambda$static$15((BlockBehaviour.Properties)scorched), TOOLTIP_BLOCK_ITEM);
        seared = TinkerSmeltery.searedNonSolidProps(SoundType.GLASS).noCollission().speedFactor(0.1f).isViewBlocking((state, getter, pos) -> true);
        searedSoulGlass = BLOCKS.register("seared_soul_glass", () -> TinkerSmeltery.lambda$static$17((BlockBehaviour.Properties)seared), TOOLTIP_BLOCK_ITEM);
        searedSoulGlassPane = BLOCKS.register("seared_soul_glass_pane", () -> TinkerSmeltery.lambda$static$18((BlockBehaviour.Properties)seared), TOOLTIP_BLOCK_ITEM);
        scorched = TinkerSmeltery.scorchedNonSolidProps(SoundType.GLASS).noCollission().speedFactor(0.1f).isViewBlocking((state, getter, pos) -> true);
        scorchedSoulGlass = BLOCKS.register("scorched_soul_glass", () -> TinkerSmeltery.lambda$static$20((BlockBehaviour.Properties)scorched), TOOLTIP_BLOCK_ITEM);
        scorchedSoulGlassPane = BLOCKS.register("scorched_soul_glass_pane", () -> TinkerSmeltery.lambda$static$21((BlockBehaviour.Properties)scorched), TOOLTIP_BLOCK_ITEM);
        seared = TinkerSmeltery.searedSolidProps(2);
        searedDrain = BLOCKS.register("seared_drain", () -> TinkerSmeltery.lambda$static$22((BlockBehaviour.Properties)seared), TOOLTIP_BLOCK_ITEM);
        searedDuct = BLOCKS.register("seared_duct", () -> TinkerSmeltery.lambda$static$23((BlockBehaviour.Properties)seared), TOOLTIP_BLOCK_ITEM);
        searedChute = BLOCKS.register("seared_chute", () -> TinkerSmeltery.lambda$static$24((BlockBehaviour.Properties)seared), TOOLTIP_BLOCK_ITEM);
        scorched = TinkerSmeltery.scorchedSolidProps(3);
        scorchedDrain = BLOCKS.register("scorched_drain", () -> TinkerSmeltery.lambda$static$25((BlockBehaviour.Properties)scorched), TOOLTIP_BLOCK_ITEM);
        scorchedDuct = BLOCKS.register("scorched_duct", () -> TinkerSmeltery.lambda$static$26((BlockBehaviour.Properties)scorched), TOOLTIP_BLOCK_ITEM);
        scorchedChute = BLOCKS.register("scorched_chute", () -> TinkerSmeltery.lambda$static$27((BlockBehaviour.Properties)scorched), TOOLTIP_BLOCK_ITEM);
        seared = TinkerSmeltery.searedNonSolidProps(SoundType.METAL);
        searedLadder = BLOCKS.register("seared_ladder", () -> TinkerSmeltery.lambda$static$28((BlockBehaviour.Properties)seared), TOOLTIP_BLOCK_ITEM);
        searedFaucet = BLOCKS.register("seared_faucet", () -> TinkerSmeltery.lambda$static$29((BlockBehaviour.Properties)seared), TOOLTIP_BLOCK_ITEM);
        searedChannel = BLOCKS.register("seared_channel", () -> TinkerSmeltery.lambda$static$30((BlockBehaviour.Properties)seared), TOOLTIP_BLOCK_ITEM);
        searedBasin = BLOCKS.register("seared_basin", () -> TinkerSmeltery.lambda$static$31((BlockBehaviour.Properties)seared), TOOLTIP_BLOCK_ITEM);
        searedTable = BLOCKS.register("seared_table", () -> TinkerSmeltery.lambda$static$32((BlockBehaviour.Properties)seared), TOOLTIP_BLOCK_ITEM);
        scorched = TinkerSmeltery.scorchedNonSolidProps(SoundType.BASALT);
        scorchedLadder = BLOCKS.register("scorched_ladder", () -> TinkerSmeltery.lambda$static$33((BlockBehaviour.Properties)scorched), TOOLTIP_BLOCK_ITEM);
        scorchedFaucet = BLOCKS.register("scorched_faucet", () -> TinkerSmeltery.lambda$static$34((BlockBehaviour.Properties)scorched), TOOLTIP_BLOCK_ITEM);
        scorchedChannel = BLOCKS.register("scorched_channel", () -> TinkerSmeltery.lambda$static$35((BlockBehaviour.Properties)scorched), TOOLTIP_BLOCK_ITEM);
        scorchedBasin = BLOCKS.register("scorched_basin", () -> TinkerSmeltery.lambda$static$36((BlockBehaviour.Properties)scorched), TOOLTIP_BLOCK_ITEM);
        scorchedTable = BLOCKS.register("scorched_table", () -> TinkerSmeltery.lambda$static$37((BlockBehaviour.Properties)scorched), TOOLTIP_BLOCK_ITEM);
        scorchedProxyTank = BLOCKS.register("scorched_proxy_tank", () -> TinkerSmeltery.lambda$static$38((BlockBehaviour.Properties)scorched), TOOLTIP_BLOCK_ITEM);
        Function<Block, BlockItem> tankItem = b -> new TankItem(b, ITEM_PROPS, true);
        Function<Block, BlockItem> lanternItem = b -> new TankItem(b, ITEM_PROPS, false);
        BlockBehaviour.Properties seared2 = TinkerSmeltery.searedNonSolidProps(SoundType.METAL).lightLevel(SearedTankBlock.LIGHT_GETTER);
        searedTank = BLOCKS.registerEnum("seared", (Enum[])SearedTankBlock.TankType.values(), type -> new SearedTankBlock(seared2, type.getCapacity(), PushReaction.DESTROY), tankItem);
        searedCastingTank = BLOCKS.register("seared_casting_tank", () -> new CastingTankBlock(seared2), b -> new TankItem((Block)b, ITEM_PROPS, true));
        searedFluidCannon = BLOCKS.register("seared_fluid_cannon", () -> new FluidCannonBlock(seared2, 2000, 1.0f, 1.1f, 6.0f), tankItem);
        searedLantern = BLOCKS.register("seared_lantern", () -> new SearedLanternBlock(TinkerSmeltery.searedNonSolidProps(SoundType.LANTERN).lightLevel(SearedTankBlock.LIGHT_GETTER), 50), lanternItem);
        BlockBehaviour.Properties scorched2 = TinkerSmeltery.scorchedNonSolidProps(SoundType.BASALT).lightLevel(SearedTankBlock.LIGHT_GETTER);
        scorchedTank = BLOCKS.registerEnum("scorched", (Enum[])SearedTankBlock.TankType.values(), type -> new SearedTankBlock(scorched2, type.getCapacity(), PushReaction.DESTROY), tankItem);
        scorchedFluidCannon = BLOCKS.register("scorched_fluid_cannon", () -> new FluidCannonBlock(scorched2, 2000, 2.0f, 1.5f, 7.0f), tankItem);
        scorchedLantern = BLOCKS.register("scorched_lantern", () -> new SearedLanternBlock(TinkerSmeltery.scorchedNonSolidProps(SoundType.LANTERN).lightLevel(SearedTankBlock.LIGHT_GETTER), 50), lanternItem);
        endFluidCannon = BLOCKS.register("end_fluid_cannon", () -> new KnightMetalFluidCannonBlock(seared2, 4000, 1.5f, 3.0f, 6.0f), tankItem);
        BlockBehaviour.Properties gaugeProperties = BlockBehaviour.Properties.of().mapColor(MapColor.NONE).pushReaction(PushReaction.DESTROY).noCollission().strength(0.5f).noOcclusion().requiresCorrectToolForDrops();
        copperGauge = BLOCKS.register("copper_gauge", () -> new GaugeBlock(gaugeProperties), TOOLTIP_BLOCK_ITEM);
        obsidianGauge = BLOCKS.register("obsidian_gauge", () -> new RenderingGaugeBlock(gaugeProperties), TOOLTIP_BLOCK_ITEM);
        seared = () -> TinkerSmeltery.builder((MapColor)MapColor.COLOR_GRAY, (SoundType)SoundType.METAL).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(8.0f, 28.0f).lightLevel(s -> (Boolean)s.getValue((Property)ControllerBlock.ACTIVE) != false ? 13 : 0);
        scorched = () -> TinkerSmeltery.builder((MapColor)MapColor.TERRACOTTA_BROWN, (SoundType)SoundType.BASALT).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(9.0f, 35.0f).lightLevel(s -> (Boolean)s.getValue((Property)ControllerBlock.ACTIVE) != false ? 13 : 0);
        smelteryController = BLOCKS.register("smeltery_controller", () -> TinkerSmeltery.lambda$static$56((Supplier)seared), TOOLTIP_BLOCK_ITEM);
        foundryController = BLOCKS.register("foundry_controller", () -> TinkerSmeltery.lambda$static$57((Supplier)scorched), TOOLTIP_BLOCK_ITEM);
        searedMelter = BLOCKS.register("seared_melter", () -> TinkerSmeltery.lambda$static$58((Supplier)seared), TOOLTIP_BLOCK_ITEM);
        searedHeater = BLOCKS.register("seared_heater", () -> TinkerSmeltery.lambda$static$59((Supplier)seared), TOOLTIP_BLOCK_ITEM);
        scorchedAlloyer = BLOCKS.register("scorched_alloyer", () -> TinkerSmeltery.lambda$static$60((Supplier)scorched), TOOLTIP_BLOCK_ITEM);
        smelteryComponent = BLOCK_ENTITIES.register("smeltery_component", SmelteryComponentBlockEntity::new, set -> {
            set.addAll((Iterable)searedStone.values());
            set.addAll((Iterable)searedCobble.values());
            set.addAll((Iterable)searedBricks.values());
            set.addAll((Iterable)searedPaver.values());
            set.add((Object[])new Block[]{(Block)searedCrackedBricks.get(), (Block)searedFancyBricks.get(), (Block)searedTriangleBricks.get(), (Block)searedLadder.get(), (Block)searedLamp.get(), (Block)searedGlass.get(), (Block)searedSoulGlass.get(), (Block)searedTintedGlass.get()});
            set.add((Object[])new Block[]{(Block)scorchedStone.get(), (Block)polishedScorchedStone.get(), (Block)chiseledScorchedBricks.get(), (Block)scorchedLadder.get(), (Block)scorchedLamp.get(), (Block)scorchedGlass.get(), (Block)scorchedSoulGlass.get(), (Block)scorchedTintedGlass.get()});
            set.addAll((Iterable)scorchedBricks.values());
            set.addAll((Iterable)scorchedRoad.values());
        });
        drain = BLOCK_ENTITIES.register("drain", DrainBlockEntity::new, set -> set.add((Object[])new Block[]{(Block)searedDrain.get(), (Block)scorchedDrain.get()}));
        chute = BLOCK_ENTITIES.register("chute", SmelteryInputOutputBlockEntity.ChuteBlockEntity::new, set -> set.add((Object[])new Block[]{(Block)searedChute.get(), (Block)scorchedChute.get()}));
        duct = BLOCK_ENTITIES.register("duct", DuctBlockEntity::new, set -> set.add((Object[])new Block[]{(Block)searedDuct.get(), (Block)scorchedDuct.get()}));
        tank = BLOCK_ENTITIES.register("tank", TankBlockEntity::new, set -> {
            set.addAll((Iterable)searedTank.values());
            set.addAll((Iterable)scorchedTank.values());
        });
        fluidCannon = BLOCK_ENTITIES.register("fluid_cannon", FluidCannonBlockEntity::new, set -> set.add((Object[])new Block[]{(Block)searedFluidCannon.get(), (Block)scorchedFluidCannon.get(), (Block)endFluidCannon.get()}));
        lantern = BLOCK_ENTITIES.register("lantern", LanternBlockEntity::new, set -> set.add((Object[])new Block[]{(Block)searedLantern.get(), (Block)scorchedLantern.get()}));
        melter = BLOCK_ENTITIES.register("melter", MelterBlockEntity::new, searedMelter);
        smeltery = BLOCK_ENTITIES.register("smeltery", SmelteryBlockEntity::new, smelteryController);
        foundry = BLOCK_ENTITIES.register("foundry", FoundryBlockEntity::new, foundryController);
        heater = BLOCK_ENTITIES.register("heater", HeaterBlockEntity::new, searedHeater);
        alloyer = BLOCK_ENTITIES.register("alloyer", AlloyerBlockEntity::new, scorchedAlloyer);
        faucet = BLOCK_ENTITIES.register("faucet", FaucetBlockEntity::new, set -> set.add((Object[])new Block[]{(Block)searedFaucet.get(), (Block)scorchedFaucet.get()}));
        channel = BLOCK_ENTITIES.register("channel", ChannelBlockEntity::new, set -> set.add((Object[])new Block[]{(Block)searedChannel.get(), (Block)scorchedChannel.get()}));
        gauge = BLOCK_ENTITIES.register("gauge", GaugeBlockEntity::new, obsidianGauge);
        basin = BLOCK_ENTITIES.register("basin", CastingBlockEntity.Basin::new, set -> set.add((Object[])new Block[]{(Block)searedBasin.get(), (Block)scorchedBasin.get()}));
        table = BLOCK_ENTITIES.register("table", CastingBlockEntity.Table::new, set -> set.add((Object[])new Block[]{(Block)searedTable.get(), (Block)scorchedTable.get()}));
        proxyTank = BLOCK_ENTITIES.register("proxy_tank", ProxyTankBlockEntity::new, scorchedProxyTank);
        castingTank = BLOCK_ENTITIES.register("casting_tank", CastingTankBlockEntity::new, set -> set.add((Object)((Block)searedCastingTank.get())));
        searedBrick = ITEMS.register("seared_brick", ITEM_PROPS);
        scorchedBrick = ITEMS.register("scorched_brick", ITEM_PROPS);
        copperCan = ITEMS.register("copper_can", () -> new CopperCanItem(new Item.Properties().stacksTo(16)));
        blankSandCast = ITEMS.register("blank_sand_cast", ITEM_PROPS);
        blankRedSandCast = ITEMS.register("blank_red_sand_cast", ITEM_PROPS);
        ingotCast = ITEMS.registerCast("ingot", ITEM_PROPS);
        nuggetCast = ITEMS.registerCast("nugget", ITEM_PROPS);
        gemCast = ITEMS.registerCast("gem", ITEM_PROPS);
        rodCast = ITEMS.registerCast("rod", ITEM_PROPS);
        repairKitCast = ITEMS.registerCast(TinkerToolParts.repairKit, ITEM_PROPS);
        plateCast = ITEMS.registerCast("plate", ITEM_PROPS);
        gearCast = ITEMS.registerCast("gear", ITEM_PROPS);
        coinCast = ITEMS.registerCast("coin", ITEM_PROPS);
        wireCast = ITEMS.registerCast("wire", ITEM_PROPS);
        pickHeadCast = ITEMS.registerCast(TinkerToolParts.pickHead, ITEM_PROPS);
        smallAxeHeadCast = ITEMS.registerCast(TinkerToolParts.smallAxeHead, ITEM_PROPS);
        smallBladeCast = ITEMS.registerCast(TinkerToolParts.smallBlade, ITEM_PROPS);
        adzeHeadCast = ITEMS.registerCast(TinkerToolParts.adzeHead, ITEM_PROPS);
        hammerHeadCast = ITEMS.registerCast(TinkerToolParts.hammerHead, ITEM_PROPS);
        broadBladeCast = ITEMS.registerCast(TinkerToolParts.broadBlade, ITEM_PROPS);
        broadAxeHeadCast = ITEMS.registerCast(TinkerToolParts.broadAxeHead, ITEM_PROPS);
        largePlateCast = ITEMS.registerCast(TinkerToolParts.largePlate, ITEM_PROPS);
        toolBindingCast = ITEMS.registerCast(TinkerToolParts.toolBinding, ITEM_PROPS);
        toughBindingCast = ITEMS.registerCast(TinkerToolParts.toughBinding, ITEM_PROPS);
        toolHandleCast = ITEMS.registerCast(TinkerToolParts.toolHandle, ITEM_PROPS);
        toughHandleCast = ITEMS.registerCast(TinkerToolParts.toughHandle, ITEM_PROPS);
        bowLimbCast = ITEMS.registerCast(TinkerToolParts.bowLimb, ITEM_PROPS);
        bowGripCast = ITEMS.registerCast(TinkerToolParts.bowGrip, ITEM_PROPS);
        arrowCast = ITEMS.register("arrow_cast", TOOLTIP_ITEM);
        helmetPlatingCast = ITEMS.registerCast("helmet_plating", () -> new PartCastItem(ITEM_PROPS, () -> (IMaterialItem)TinkerToolParts.plating.get((Enum)ArmorItem.Type.HELMET)));
        chestplatePlatingCast = ITEMS.registerCast("chestplate_plating", () -> new PartCastItem(ITEM_PROPS, () -> (IMaterialItem)TinkerToolParts.plating.get((Enum)ArmorItem.Type.CHESTPLATE)));
        leggingsPlatingCast = ITEMS.registerCast("leggings_plating", () -> new PartCastItem(ITEM_PROPS, () -> (IMaterialItem)TinkerToolParts.plating.get((Enum)ArmorItem.Type.LEGGINGS)));
        bootsPlatingCast = ITEMS.registerCast("boots_plating", () -> new PartCastItem(ITEM_PROPS, () -> (IMaterialItem)TinkerToolParts.plating.get((Enum)ArmorItem.Type.BOOTS)));
        mailleCast = ITEMS.registerCast(TinkerToolParts.maille, ITEM_PROPS);
        dummyPlating = ITEMS.registerEnum((Enum[])ModifiableArmorMaterial.ARMOR_TYPES, "plating_dummy", type -> new DummyMaterialItem(ITEM_PROPS));
        basinRecipeSerializer = RECIPE_SERIALIZERS.register("casting_basin", () -> LoadableRecipeSerializer.of(ItemCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_BASIN));
        tableRecipeSerializer = RECIPE_SERIALIZERS.register("casting_table", () -> LoadableRecipeSerializer.of(ItemCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_TABLE));
        basinFillingRecipeSerializer = RECIPE_SERIALIZERS.register("basin_filling", () -> LoadableRecipeSerializer.of((RecordLoadable)ContainerFillingRecipe.LOADER, TinkerRecipeTypes.CASTING_BASIN));
        tableFillingRecipeSerializer = RECIPE_SERIALIZERS.register("table_filling", () -> LoadableRecipeSerializer.of((RecordLoadable)ContainerFillingRecipe.LOADER, TinkerRecipeTypes.CASTING_TABLE));
        basinDuplicationRecipeSerializer = RECIPE_SERIALIZERS.register("basin_duplication", () -> LoadableRecipeSerializer.of((RecordLoadable)CastDuplicationRecipe.LOADER, TinkerRecipeTypes.CASTING_BASIN));
        tableDuplicationRecipeSerializer = RECIPE_SERIALIZERS.register("table_duplication", () -> LoadableRecipeSerializer.of((RecordLoadable)CastDuplicationRecipe.LOADER, TinkerRecipeTypes.CASTING_TABLE));
        basinPotionRecipeSerializer = RECIPE_SERIALIZERS.register("casting_basin_potion", () -> LoadableRecipeSerializer.of((RecordLoadable)PotionCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_BASIN));
        tablePotionRecipeSerializer = RECIPE_SERIALIZERS.register("casting_table_potion", () -> LoadableRecipeSerializer.of((RecordLoadable)PotionCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_TABLE));
        basinTippingRecipeSerializer = RECIPE_SERIALIZERS.register("casting_basin_tipping", () -> LoadableRecipeSerializer.of((RecordLoadable)TippingCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_BASIN));
        tableTippingRecipeSerializer = RECIPE_SERIALIZERS.register("casting_table_tipping", () -> LoadableRecipeSerializer.of((RecordLoadable)TippingCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_TABLE));
        basinTipClearingRecipeSerializer = RECIPE_SERIALIZERS.register("casting_basin_tipped_clearing", () -> LoadableRecipeSerializer.of((RecordLoadable)TipClearingCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_BASIN));
        tableTipClearingRecipeSerializer = RECIPE_SERIALIZERS.register("casting_table_tipped_clearing", () -> LoadableRecipeSerializer.of((RecordLoadable)TipClearingCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_TABLE));
        retexturedBasinRecipeSerializer = RECIPE_SERIALIZERS.register("retextured_casting_basin", () -> LoadableRecipeSerializer.of((RecordLoadable)RetexturedCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_BASIN));
        retexturedTableRecipeSerializer = RECIPE_SERIALIZERS.register("retextured_casting_table", () -> LoadableRecipeSerializer.of((RecordLoadable)RetexturedCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_TABLE));
        basinMaterialSerializer = RECIPE_SERIALIZERS.register("basin_casting_material", () -> LoadableRecipeSerializer.of((RecordLoadable)MaterialCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_BASIN));
        tableMaterialSerializer = RECIPE_SERIALIZERS.register("table_casting_material", () -> LoadableRecipeSerializer.of((RecordLoadable)MaterialCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_TABLE));
        basinCompositeSerializer = RECIPE_SERIALIZERS.register("basin_casting_composite", () -> LoadableRecipeSerializer.of((RecordLoadable)CompositeCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_BASIN));
        tableCompositeSerializer = RECIPE_SERIALIZERS.register("table_casting_composite", () -> LoadableRecipeSerializer.of((RecordLoadable)CompositeCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_TABLE));
        basinToolSerializer = RECIPE_SERIALIZERS.register("basin_tool_casting", () -> LoadableRecipeSerializer.of((RecordLoadable)ToolCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_BASIN));
        tableToolSerializer = RECIPE_SERIALIZERS.register("table_tool_casting", () -> LoadableRecipeSerializer.of((RecordLoadable)ToolCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_TABLE));
        materialFluidRecipe = RECIPE_SERIALIZERS.register("material_fluid", () -> LoadableRecipeSerializer.of((RecordLoadable)MaterialFluidRecipe.LOADER));
        basinPartSwappingSerializer = RECIPE_SERIALIZERS.register("basin_casting_part_swapping", () -> LoadableRecipeSerializer.of((RecordLoadable)PartSwapCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_BASIN));
        tablePartSwappingSerializer = RECIPE_SERIALIZERS.register("table_casting_part_swapping", () -> LoadableRecipeSerializer.of((RecordLoadable)PartSwapCastingRecipe.LOADER, TinkerRecipeTypes.CASTING_TABLE));
        moldingBasinSerializer = RECIPE_SERIALIZERS.register("molding_basin", () -> LoadableRecipeSerializer.of((RecordLoadable)MoldingRecipe.LOADER, TinkerRecipeTypes.MOLDING_BASIN));
        moldingTableSerializer = RECIPE_SERIALIZERS.register("molding_table", () -> LoadableRecipeSerializer.of((RecordLoadable)MoldingRecipe.LOADER, TinkerRecipeTypes.MOLDING_TABLE));
        meltingSerializer = RECIPE_SERIALIZERS.register("melting", () -> LoadableRecipeSerializer.of((RecordLoadable)MeltingRecipe.LOADER));
        oreMeltingSerializer = RECIPE_SERIALIZERS.register("ore_melting", () -> LoadableRecipeSerializer.of((RecordLoadable)OreMeltingRecipe.LOADER));
        damagableMeltingSerializer = RECIPE_SERIALIZERS.register("damagable_melting", () -> LoadableRecipeSerializer.of((RecordLoadable)DamageableMeltingRecipe.LOADER));
        materialMeltingSerializer = RECIPE_SERIALIZERS.register("material_melting", () -> LoadableRecipeSerializer.of((RecordLoadable)MaterialMeltingRecipe.LOADER));
        fuelSerializer = RECIPE_SERIALIZERS.register("melting_fuel", () -> LoadableRecipeSerializer.of((RecordLoadable)MeltingFuel.LOADER));
        entityMeltingSerializer = RECIPE_SERIALIZERS.register("entity_melting", () -> LoadableRecipeSerializer.of((RecordLoadable)EntityMeltingRecipe.LOADER));
        alloyingSerializer = RECIPE_SERIALIZERS.register("alloy", () -> LoadableRecipeSerializer.of((RecordLoadable)AlloyRecipe.LOADER));
        melterContainer = MENUS.register("melter", MelterContainerMenu::new);
        smelteryContainer = MENUS.register("smeltery", HeatingStructureContainerMenu::new);
        singleItemContainer = MENUS.register("single_item", SingleItemContainerMenu::new);
        alloyerContainer = MENUS.register("alloyer", AlloyerContainerMenu::new);
    }
}
