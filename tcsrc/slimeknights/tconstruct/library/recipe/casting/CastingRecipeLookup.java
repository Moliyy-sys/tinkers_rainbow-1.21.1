/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.ItemLike
 *  slimeknights.mantle.recipe.helper.ItemOutput
 *  slimeknights.mantle.util.RegistryHelper
 *  slimeknights.tconstruct.common.recipe.RecipeCacheInvalidator
 *  slimeknights.tconstruct.common.recipe.RecipeCacheInvalidator$DuelSidedListener
 */
package slimeknights.tconstruct.library.recipe.casting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.mantle.util.RegistryHelper;
import slimeknights.tconstruct.common.recipe.RecipeCacheInvalidator;

public class CastingRecipeLookup {
    private static final Map<Item, Boolean> CASTABLE_ITEMS = new HashMap<Item, Boolean>();
    private static final List<TagKey<Item>> CASTABLE_TAGS = new ArrayList<TagKey<Item>>();
    private static final Function<Item, Boolean> COMPUTE_CASTABLE = item -> {
        for (TagKey<Item> tag : CASTABLE_TAGS) {
            if (!RegistryHelper.contains(tag, (Item)item)) continue;
            return true;
        }
        return false;
    };
    private static final RecipeCacheInvalidator.DuelSidedListener INVALIDATOR = RecipeCacheInvalidator.addDuelSidedListener(() -> {
        CASTABLE_ITEMS.clear();
        CASTABLE_TAGS.clear();
    });

    private CastingRecipeLookup() {
    }

    public static void registerCastable(ItemLike item) {
        INVALIDATOR.checkClear();
        CASTABLE_ITEMS.put(item.asItem(), true);
    }

    public static void registerCastable(TagKey<Item> tag) {
        INVALIDATOR.checkClear();
        CASTABLE_TAGS.add(tag);
    }

    public static void registerCastable(ItemOutput output) {
        TagKey tag = output.getTag();
        if (tag != null) {
            CastingRecipeLookup.registerCastable((TagKey<Item>)tag);
        } else if (!output.isEmpty()) {
            CastingRecipeLookup.registerCastable((ItemLike)output.get().getItem());
        }
    }

    public static boolean isCastable(ItemLike item) {
        return CASTABLE_ITEMS.computeIfAbsent(item.asItem(), COMPUTE_CASTABLE);
    }
}
