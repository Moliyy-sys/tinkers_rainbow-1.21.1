/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.RegistryAccess
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.fluids.FluidStack
 *  slimeknights.mantle.data.loadable.field.ContextKey
 *  slimeknights.mantle.data.loadable.field.LoadableField
 *  slimeknights.mantle.data.loadable.field.RecordField
 *  slimeknights.mantle.data.loadable.primitive.IntLoadable
 *  slimeknights.mantle.data.loadable.record.RecordLoadable
 *  slimeknights.mantle.recipe.helper.ItemOutput
 *  slimeknights.mantle.recipe.helper.ItemOutput$Loadable
 *  slimeknights.mantle.recipe.helper.LoadableRecipeSerializer
 *  slimeknights.mantle.recipe.helper.TypeAwareRecipeSerializer
 *  slimeknights.mantle.recipe.ingredient.FluidIngredient
 *  slimeknights.tconstruct.library.recipe.casting.AbstractCastingRecipe
 *  slimeknights.tconstruct.library.recipe.casting.ICastingContainer
 *  slimeknights.tconstruct.library.recipe.casting.IDisplayableCastingRecipe
 */
package slimeknights.tconstruct.library.recipe.casting;

import java.util.Arrays;
import java.util.List;
import lombok.Generated;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import slimeknights.mantle.data.loadable.field.ContextKey;
import slimeknights.mantle.data.loadable.field.LoadableField;
import slimeknights.mantle.data.loadable.field.RecordField;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.mantle.recipe.helper.LoadableRecipeSerializer;
import slimeknights.mantle.recipe.helper.TypeAwareRecipeSerializer;
import slimeknights.mantle.recipe.ingredient.FluidIngredient;
import slimeknights.tconstruct.library.recipe.casting.AbstractCastingRecipe;
import slimeknights.tconstruct.library.recipe.casting.CastingRecipeLookup;
import slimeknights.tconstruct.library.recipe.casting.ICastingContainer;
import slimeknights.tconstruct.library.recipe.casting.IDisplayableCastingRecipe;

public class ItemCastingRecipe
extends AbstractCastingRecipe
implements IDisplayableCastingRecipe {
    protected static final LoadableField<FluidIngredient, ItemCastingRecipe> FLUID_FIELD = FluidIngredient.LOADABLE.requiredField("fluid", ItemCastingRecipe::getFluid);
    protected static final LoadableField<ItemOutput, ItemCastingRecipe> RESULT_FIELD = ItemOutput.Loadable.REQUIRED_ITEM.requiredField("result", r -> r.result);
    protected static final LoadableField<Integer, ItemCastingRecipe> COOLING_TIME_FIELD = IntLoadable.FROM_ONE.requiredField("cooling_time", ItemCastingRecipe::getCoolingTime);
    public static final RecordLoadable<ItemCastingRecipe> LOADER = RecordLoadable.create((RecordField)LoadableRecipeSerializer.TYPED_SERIALIZER.requiredField(), (RecordField)ContextKey.ID.requiredField(), (RecordField)LoadableRecipeSerializer.RECIPE_GROUP, (RecordField)CAST_FIELD, FLUID_FIELD, RESULT_FIELD, COOLING_TIME_FIELD, (RecordField)CAST_CONSUMED_FIELD, (RecordField)SWITCH_SLOTS_FIELD, ItemCastingRecipe::new);
    private final TypeAwareRecipeSerializer<?> serializer;
    protected final FluidIngredient fluid;
    protected final ItemOutput result;
    protected final int coolingTime;

    public ItemCastingRecipe(TypeAwareRecipeSerializer<?> serializer, ResourceLocation id, String group, Ingredient cast, FluidIngredient fluid, ItemOutput result, int coolingTime, boolean consumed, boolean switchSlots) {
        super(serializer.getType(), id, group, cast, consumed, switchSlots);
        this.serializer = serializer;
        this.fluid = fluid;
        this.result = result;
        this.coolingTime = coolingTime;
        CastingRecipeLookup.registerCastable(result);
    }

    public int getFluidAmount(ICastingContainer inv) {
        return this.fluid.getAmount(inv.getFluid());
    }

    public boolean matches(ICastingContainer inv, Level worldIn) {
        return this.getCast().test(inv.getStack()) && this.fluid.test(inv.getFluid());
    }

    public ItemStack getResultItem(HolderLookup.Provider access) {
        return this.resolveResult();
    }

    @Deprecated
    public ItemStack getResultItem(RegistryAccess access) {
        return this.resolveResult();
    }

    public ItemStack assemble(ICastingContainer inv, HolderLookup.Provider access) {
        return this.resolveResult();
    }

    @Deprecated
    public ItemStack assemble(ICastingContainer inv, RegistryAccess access) {
        return this.resolveResult();
    }

    private ItemStack resolveResult() {
        TagKey tag;
        ItemStack output = this.result.get();
        if (output.isEmpty() && (tag = this.result.getTag()) != null) {
            output = BuiltInRegistries.ITEM.getTag(tag).flatMap(holders -> holders.stream().findFirst().map(Holder::value)).map(item -> new ItemStack((ItemLike)item, this.result.getCount())).orElse(ItemStack.EMPTY);
        }
        return output.copy();
    }

    public int getCoolingTime(ICastingContainer inv) {
        return this.coolingTime;
    }

    public ResourceLocation getRecipeId() {
        return this.getId();
    }

    public boolean hasCast() {
        return this.getCast() != Ingredient.EMPTY;
    }

    public List<ItemStack> getCastItems() {
        return Arrays.asList(this.getCast().getItems());
    }

    public ItemStack getOutput() {
        return this.resolveResult();
    }

    public List<FluidStack> getFluids() {
        return this.fluid.getFluids();
    }

    @Generated
    public TypeAwareRecipeSerializer<?> getSerializer() {
        return this.serializer;
    }

    @Generated
    public FluidIngredient getFluid() {
        return this.fluid;
    }

    @Generated
    public ItemOutput getResult() {
        return this.result;
    }

    @Generated
    public int getCoolingTime() {
        return this.coolingTime;
    }
}
