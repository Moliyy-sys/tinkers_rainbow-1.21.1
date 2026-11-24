package org.moliyy.tinkers_rainbow.smeltery;

import java.util.List;
import java.util.Optional;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.casting.ICastingContainer;
import slimeknights.tconstruct.library.recipe.casting.ICastingRecipe;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipe;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialCastingRecipe;

/**
 * Reusable casting recipe lookup shared by the AE interface and the high speed cooler.
 *
 * <p>Plain item casting and material casting (tool parts) are supported. Container filling and part
 * swapping recipes would have to transform the item in the cast slot, so they are skipped.</p>
 */
public final class CastingRecipeCache {

    @Nullable
    private ICastingRecipe cachedRecipe;
    @Nullable
    private RecipeType<?> cachedType;
    private ItemStack cachedCast = ItemStack.EMPTY;
    private FluidStack cachedFluid = FluidStack.EMPTY;

    @Nullable
    public ICastingRecipe find(Level level, RecipeType<ICastingRecipe> recipeType,
                               ItemStack castStack, FluidStack fluid) {
        if (this.cachedRecipe != null && this.cachedType == recipeType
                && ItemStack.matches(this.cachedCast, castStack)
                && FluidStack.isSameFluidSameComponents(this.cachedFluid, fluid)) {
            return this.cachedRecipe;
        }

        // iterate all recipes manually: RecipeManager.getRecipeFor relies on Ingredient.test() which does
        // not match empty cast ingredients, so basin recipes without a cast would never be found
        ICastingRecipe best = null;
        int bestAmount = 0;
        for (RecipeHolder<ICastingRecipe> holder : level.getRecipeManager().getAllRecipesFor(recipeType)) {
            ICastingRecipe recipe = holder.value();
            if (!(recipe instanceof ItemCastingRecipe || recipe instanceof MaterialCastingRecipe)) {
                continue;
            }
            SimpleCastingContainer container = new SimpleCastingContainer(castStack.copy(), fluid.copy());
            if (!recipe.matches(container, level)) {
                continue;
            }
            int amount = recipe.getFluidAmount(container);
            if (amount > 0 && fluid.getAmount() >= amount && amount >= bestAmount) {
                if (recipe.assemble(container, level.registryAccess()).isEmpty()) {
                    continue;
                }
                best = recipe;
                bestAmount = amount;
            }
        }
        if (best == null) {
            clear();
            return null;
        }
        this.cachedRecipe = best;
        this.cachedType = recipeType;
        this.cachedCast = castStack.copy();
        this.cachedFluid = fluid.copy();
        return best;
    }

    public void clear() {
        this.cachedRecipe = null;
        this.cachedType = null;
        this.cachedCast = ItemStack.EMPTY;
        this.cachedFluid = FluidStack.EMPTY;
    }

    /** Recipe type used for the given output mode, or {@code null} for pure fluid output. */
    @Nullable
    public static RecipeType<ICastingRecipe> recipeTypeFor(OutputMode mode) {
        return switch (mode) {
            case FLUID -> null;
            case BASIN -> TinkerRecipeTypes.CASTING_BASIN.get();
            case TABLE -> TinkerRecipeTypes.CASTING_TABLE.get();
        };
    }

    /** Minimal casting container implementation shared by both blocks. */
    public static final class SimpleCastingContainer implements ICastingContainer {
        private final ItemStack stack;
        private final FluidStack fluid;

        public SimpleCastingContainer(ItemStack stack, FluidStack fluid) {
            this.stack = stack;
            this.fluid = fluid;
        }

        @Override
        public ItemStack getStack() {
            return stack;
        }

        @Override
        public Fluid getFluid() {
            return fluid.getFluid();
        }

        @Override
        public FluidStack getFluidStack() {
            return fluid;
        }
    }
}
