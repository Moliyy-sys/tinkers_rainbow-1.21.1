/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  appeng.api.stacks.AEKey
 *  appeng.api.stacks.AEKeyType
 *  appeng.core.AppEng
 *  appeng.core.localization.GuiText
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.TagKey
 */
package appeng.api.stacks;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import appeng.core.AppEng;
import appeng.core.localization.GuiText;
import com.mojang.serialization.MapCodec;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

final class AEFluidKeys
extends AEKeyType {
    private static final ResourceLocation ID = AppEng.makeId((String)"f");
    static final AEFluidKeys INSTANCE = new AEFluidKeys();

    private AEFluidKeys() {
        super(ID, AEFluidKey.class, (Component)GuiText.Fluids.text());
    }

    public MapCodec<? extends AEKey> codec() {
        return AEFluidKey.MAP_CODEC;
    }

    public int getAmountPerOperation() {
        return 125;
    }

    public int getAmountPerByte() {
        return 8000;
    }

    public AEFluidKey readFromPacket(RegistryFriendlyByteBuf input) {
        Objects.requireNonNull(input);
        return AEFluidKey.fromPacket(input);
    }

    public AEFluidKey loadKeyFromTag(HolderLookup.Provider registries, CompoundTag tag) {
        return AEFluidKey.fromTag(registries, tag);
    }

    public int getAmountPerUnit() {
        return 1000;
    }

    public Stream<TagKey<?>> getTagNames() {
        return BuiltInRegistries.FLUID.getTagNames().map(t -> t);
    }

    public String getUnitSymbol() {
        return "B";
    }
}
