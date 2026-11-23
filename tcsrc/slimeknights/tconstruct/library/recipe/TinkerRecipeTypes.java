/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  slimeknights.tconstruct.library.recipe.alloying.AlloyRecipe
 *  slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipe
 *  slimeknights.tconstruct.library.recipe.fuel.MeltingFuel
 *  slimeknights.tconstruct.library.recipe.material.MaterialRecipe
 *  slimeknights.tconstruct.library.recipe.melting.IMeltingRecipe
 *  slimeknights.tconstruct.library.recipe.modifiers.severing.SeveringRecipe
 *  slimeknights.tconstruct.library.recipe.molding.MoldingRecipe
 *  slimeknights.tconstruct.library.recipe.partbuilder.IPartBuilderRecipe
 *  slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe
 *  slimeknights.tconstruct.library.recipe.worktable.IModifierWorktableRecipe
 */
package slimeknights.tconstruct.library.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipe;
import slimeknights.tconstruct.library.recipe.casting.ICastingRecipe;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipe;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuel;
import slimeknights.tconstruct.library.recipe.material.MaterialRecipe;
import slimeknights.tconstruct.library.recipe.melting.IMeltingRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.severing.SeveringRecipe;
import slimeknights.tconstruct.library.recipe.molding.MoldingRecipe;
import slimeknights.tconstruct.library.recipe.partbuilder.IPartBuilderRecipe;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;
import slimeknights.tconstruct.library.recipe.worktable.IModifierWorktableRecipe;

public class TinkerRecipeTypes {
    private static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create((ResourceKey)Registries.RECIPE_TYPE, (String)"tconstruct");
    public static final DeferredHolder<RecipeType<?>, RecipeType<IPartBuilderRecipe>> PART_BUILDER = TinkerRecipeTypes.register("part_builder");
    public static final DeferredHolder<RecipeType<?>, RecipeType<MaterialRecipe>> MATERIAL = TinkerRecipeTypes.register("material");
    public static final DeferredHolder<RecipeType<?>, RecipeType<ITinkerStationRecipe>> TINKER_STATION = TinkerRecipeTypes.register("tinker_station");
    public static final DeferredHolder<RecipeType<?>, RecipeType<IModifierWorktableRecipe>> MODIFIER_WORKTABLE = TinkerRecipeTypes.register("modifier_worktable");
    public static final DeferredHolder<RecipeType<?>, RecipeType<ICastingRecipe>> CASTING_BASIN = TinkerRecipeTypes.register("casting_basin");
    public static final DeferredHolder<RecipeType<?>, RecipeType<ICastingRecipe>> CASTING_TABLE = TinkerRecipeTypes.register("casting_table");
    public static final DeferredHolder<RecipeType<?>, RecipeType<MoldingRecipe>> MOLDING_TABLE = TinkerRecipeTypes.register("molding_table");
    public static final DeferredHolder<RecipeType<?>, RecipeType<MoldingRecipe>> MOLDING_BASIN = TinkerRecipeTypes.register("molding_basin");
    public static final DeferredHolder<RecipeType<?>, RecipeType<IMeltingRecipe>> MELTING = TinkerRecipeTypes.register("melting");
    public static final DeferredHolder<RecipeType<?>, RecipeType<EntityMeltingRecipe>> ENTITY_MELTING = TinkerRecipeTypes.register("entity_melting");
    public static final DeferredHolder<RecipeType<?>, RecipeType<MeltingFuel>> FUEL = TinkerRecipeTypes.register("fuel");
    public static final DeferredHolder<RecipeType<?>, RecipeType<AlloyRecipe>> ALLOYING = TinkerRecipeTypes.register("alloying");
    public static final DeferredHolder<RecipeType<?>, RecipeType<SeveringRecipe>> SEVERING = TinkerRecipeTypes.register("severing");
    public static final DeferredHolder<RecipeType<?>, RecipeType<Recipe<?>>> DATA = TinkerRecipeTypes.register("data");

    public static void init(IEventBus bus) {
        TYPES.register(bus);
    }

    static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> register(String name) {
        return TYPES.register(name, () -> new /* Unavailable Anonymous Inner Class!! */);
    }
}
