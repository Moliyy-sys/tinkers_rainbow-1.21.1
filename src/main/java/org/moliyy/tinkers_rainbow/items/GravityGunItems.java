package org.moliyy.tinkers_rainbow.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import org.moliyy.tinkers_rainbow.TinkersRainbow;

/**
 * Registration of the gravity gun tool.
 *
 * <p>The tool definition is created first so that Tinkers' data driven definitions
 * ({@code data/tinkers_rainbow/tinkering/tool_definitions/gravity_gun.json}) can attach their modules to
 * it. The tool itself is assembled exclusively at the Tinker's Anvil because it uses four parts:
 * adze head, pick head, tool binding and tough handle.</p>
 */
public final class GravityGunItems {

    public static final ToolDefinition GRAVITY_GUN_DEFINITION = ToolDefinition.create(
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "gravity_gun"));

    public static final DeferredItem<GravityGunItem> GRAVITY_GUN = RainbowItems.ITEMS.register(
            "gravity_gun", () -> new GravityGunItem(new Item.Properties(), GRAVITY_GUN_DEFINITION));

    private GravityGunItems() {
    }

    /** Builds a fully assembled stack so the creative menu entry is usable and renders properly. */
    public static ItemStack createCreativeStack() {
        try {
            MaterialNBT materials = MaterialNBT.of(
                    MaterialVariant.of(MaterialVariantId.parse("tconstruct:flint")),
                    MaterialVariant.of(MaterialVariantId.parse("tconstruct:copper")),
                    MaterialVariant.of(MaterialVariantId.parse("tconstruct:wood")),
                    MaterialVariant.of(MaterialVariantId.parse("tconstruct:stone")));
            ToolStack tool = ToolStack.createTool(GRAVITY_GUN.get(), GRAVITY_GUN_DEFINITION, materials);
            return tool.createStack();
        } catch (Throwable ignored) {
            // data not ready (for example very early on the client side), fall back to the plain item
            return new ItemStack(GRAVITY_GUN.get());
        }
    }
}
