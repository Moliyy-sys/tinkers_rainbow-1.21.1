/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  appeng.api.stacks.AEKey
 *  appeng.api.stacks.GenericStackListCodec
 *  appeng.items.misc.WrappedGenericStack
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.Codec$ResultFunction
 *  com.mojang.serialization.DynamicOps
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.NbtOps
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.resources.RegistryOps
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.fluids.FluidStack
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.Nullable
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package appeng.api.stacks;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStackListCodec;
import appeng.items.misc.WrappedGenericStack;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record GenericStack(AEKey what, long amount) {
    @ApiStatus.Internal
    public static final String AMOUNT_FIELD = "#";
    private static final Logger LOG = LoggerFactory.getLogger(GenericStack.class);
    public static final Codec<GenericStack> CODEC = RecordCodecBuilder.create(builder -> builder.group((App)AEKey.MAP_CODEC.forGetter(GenericStack::what), (App)Codec.LONG.fieldOf(AMOUNT_FIELD).forGetter(GenericStack::amount)).apply((Applicative)builder, GenericStack::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, GenericStack> STREAM_CODEC = StreamCodec.ofMember(GenericStack::writeBuffer, GenericStack::readBuffer);
    private static final Codec.ResultFunction<GenericStack> MISSING_CONTENT_GENERICSTACK_RESULT = new /* Unavailable Anonymous Inner Class!! */;
    public static final Codec<List<@Nullable GenericStack>> FAULT_TOLERANT_NULLABLE_LIST_CODEC = new GenericStackListCodec(CODEC.mapResult(MISSING_CONTENT_GENERICSTACK_RESULT));
    public static final Codec<List<GenericStack>> FAULT_TOLERANT_LIST_CODEC = CODEC.mapResult(MISSING_CONTENT_GENERICSTACK_RESULT).listOf();

    public GenericStack {
        Objects.requireNonNull(what, "what");
    }

    @Nullable
    public static GenericStack readBuffer(RegistryFriendlyByteBuf buffer) {
        if (!buffer.readBoolean()) {
            return null;
        }
        AEKey what = AEKey.readKey((RegistryFriendlyByteBuf)buffer);
        if (what == null) {
            return null;
        }
        return new GenericStack(what, buffer.readVarLong());
    }

    public static void writeBuffer(@Nullable GenericStack stack, RegistryFriendlyByteBuf buffer) {
        if (stack == null) {
            buffer.writeBoolean(false);
        } else {
            buffer.writeBoolean(true);
            AEKey.writeKey((RegistryFriendlyByteBuf)buffer, (AEKey)stack.what);
            buffer.writeVarLong(stack.amount);
        }
    }

    @Nullable
    public static GenericStack readTag(HolderLookup.Provider registries, CompoundTag tag) {
        if (tag.isEmpty()) {
            return null;
        }
        RegistryOps ops = registries.createSerializationContext((DynamicOps)NbtOps.INSTANCE);
        return (GenericStack)((Pair)CODEC.decode((DynamicOps)ops, (Object)tag).ifError(err -> LOG.error("Failed to decode GenericStack from {}: {}", (Object)tag, (Object)err.message())).getPartialOrThrow()).getFirst();
    }

    public static CompoundTag writeTag(HolderLookup.Provider registries, @Nullable GenericStack stack) {
        if (stack == null) {
            return new CompoundTag();
        }
        RegistryOps ops = registries.createSerializationContext((DynamicOps)NbtOps.INSTANCE);
        return (CompoundTag)CODEC.encodeStart((DynamicOps)ops, (Object)stack).getOrThrow();
    }

    @Nullable
    public static GenericStack fromItemStack(ItemStack stack) {
        GenericStack genericStack = GenericStack.unwrapItemStack(stack);
        if (genericStack != null) {
            return genericStack;
        }
        AEItemKey key = AEItemKey.of(stack);
        if (key == null) {
            return null;
        }
        return new GenericStack(key, stack.getCount());
    }

    @Nullable
    public static GenericStack fromFluidStack(FluidStack stack) {
        AEFluidKey key = AEFluidKey.of(stack);
        if (key == null) {
            return null;
        }
        return new GenericStack(key, stack.getAmount());
    }

    public static long getStackSizeOrZero(@Nullable GenericStack stack) {
        return stack == null ? 0L : stack.amount;
    }

    public static ItemStack wrapInItemStack(@Nullable GenericStack stack) {
        if (stack != null) {
            return GenericStack.wrapInItemStack(stack.what(), stack.amount());
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack wrapInItemStack(AEKey what, long amount) {
        return WrappedGenericStack.wrap((AEKey)what, (long)amount);
    }

    public static boolean isWrapped(ItemStack stack) {
        return stack.getItem() instanceof WrappedGenericStack;
    }

    public static GenericStack unwrapItemStack(ItemStack stack) {
        WrappedGenericStack item;
        AEKey what;
        Item item2;
        if (!stack.isEmpty() && (item2 = stack.getItem()) instanceof WrappedGenericStack && (what = (item = (WrappedGenericStack)item2).unwrapWhat(stack)) != null) {
            long amount = item.unwrapAmount(stack);
            return new GenericStack(what, amount);
        }
        return null;
    }

    public static GenericStack sum(GenericStack left, GenericStack right) {
        if (!left.what.equals(right.what)) {
            throw new IllegalArgumentException("Cannot sum generic stacks of " + String.valueOf(left.what) + " and " + String.valueOf(right.what));
        }
        return new GenericStack(left.what, left.amount + right.amount);
    }
}
