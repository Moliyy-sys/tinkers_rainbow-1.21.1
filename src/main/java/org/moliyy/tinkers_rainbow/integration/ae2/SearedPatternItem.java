package org.moliyy.tinkers_rainbow.integration.ae2;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import org.jetbrains.annotations.Nullable;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;

/**
 * The "seared pattern" item. It stores {@link SearedPatternData} and is written from the
 * {@link SearedPatternInterfaceBlockEntity} GUI. A pattern tells the interface which items to pull from the
 * ME network into the smeltery and, optionally, which basin casting recipe to use when returning items.
 */
public class SearedPatternItem extends Item {

    public SearedPatternItem() {
        super(new Item.Properties().stacksTo(1));
    }

    public static SearedPatternData getData(ItemStack stack) {
        SearedPatternData data = stack.get(Ae2Integration.SEARED_PATTERN_DATA.get());
        return data == null ? SearedPatternData.EMPTY : data;
    }

    public static void setData(ItemStack stack, SearedPatternData data) {
        stack.set(Ae2Integration.SEARED_PATTERN_DATA.get(), data);
    }

    public static boolean isPattern(@Nullable ItemStack stack) {
        return stack != null && !stack.isEmpty() && stack.getItem() instanceof SearedPatternItem;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        SearedPatternData data = getData(stack);
        if (data.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.tinkers_rainbow.seared_pattern.blank")
                    .withStyle(ChatFormatting.GRAY));
        } else {
            for (SearedPatternData.MeltingEntry entry : data.inputs()) {
                tooltip.add(Component.translatable("tooltip.tinkers_rainbow.seared_pattern.input",
                        entry.item().getDisplayName(),
                        entry.count(),
                        entry.fluid().getDisplayName(),
                        entry.fluidAmount()).withStyle(ChatFormatting.AQUA));
            }
            for (SearedPatternData.CastEntry cast : data.casts()) {
                tooltip.add(Component.translatable("tooltip.tinkers_rainbow.seared_pattern.cast",
                        cast.fluid().getDisplayName(),
                        cast.fluidAmount(),
                        cast.result().getDisplayName(),
                        cast.resultCount()).withStyle(ChatFormatting.GOLD));
            }
        }
        tooltip.add(Component.translatable("tooltip.tinkers_rainbow.seared_pattern.hint")
                .withStyle(ChatFormatting.DARK_GRAY));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    /** Convenience factory used by the interface GUI. */
    public static boolean isValidItemKey(@Nullable AEItemKey key) {
        return key != null;
    }

    @Nullable
    public static AEFluidKey fluidKey(@Nullable net.neoforged.neoforge.fluids.FluidStack stack) {
        return stack == null || stack.isEmpty() ? null : AEFluidKey.of(stack);
    }
}
