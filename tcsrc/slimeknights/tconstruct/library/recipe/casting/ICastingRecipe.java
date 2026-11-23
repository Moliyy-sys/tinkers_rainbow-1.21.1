/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.fluids.FluidStack
 *  slimeknights.mantle.recipe.ICommonRecipe
 *  slimeknights.tconstruct.library.recipe.TinkerRecipeTypes
 *  slimeknights.tconstruct.library.recipe.casting.ICastingContainer
 *  slimeknights.tconstruct.library.recipe.melting.IMeltingRecipe
 */
package slimeknights.tconstruct.library.recipe.casting;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import slimeknights.mantle.recipe.ICommonRecipe;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.casting.ICastingContainer;
import slimeknights.tconstruct.library.recipe.melting.IMeltingRecipe;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

public interface ICastingRecipe
extends ICommonRecipe<ICastingContainer> {
    default public ItemStack getToastSymbol() {
        return new ItemStack(this.getType() == TinkerRecipeTypes.CASTING_TABLE.get() ? TinkerSmeltery.searedTable : TinkerSmeltery.searedBasin);
    }

    public int getFluidAmount(ICastingContainer var1);

    public boolean isConsumed();

    default public boolean isConsumed(ICastingContainer inv) {
        return this.isConsumed();
    }

    public boolean switchSlots();

    public int getCoolingTime(ICastingContainer var1);

    public static int calcCoolingTime(int temperature, int amount) {
        return IMeltingRecipe.calcTimeForAmount((int)temperature, (int)amount);
    }

    public static int calcCoolingTime(FluidStack fluid) {
        return ICastingRecipe.calcCoolingTime(IMeltingRecipe.getTemperature((FluidStack)fluid), fluid.getAmount());
    }
}
