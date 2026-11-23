package org.moliyy.tinkers_rainbow.integration.ae2;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Immutable data stored on a {@link SearedPatternItem}.
 *
 * <p>The pattern is split into two independent parts:</p>
 * <ul>
 *     <li>{@code inputs} describe which items should be pulled out of the ME network and dropped into the
 *     melting inventory of a smeltery, together with the fluid and amount the melting recipe will produce
 *     for a single item. Those values are cached when the pattern is written so that the interface can
 *     check tank space without doing recipe lookups every tick.</li>
 *     <li>{@code casts} describe the basin casting recipe attached to the produced fluid. They are only used
 *     by the interface while it is in its plain (no mode) mode, where the interface may cast the fluid back
 *     into items before returning the remaining fluid to the network.</li>
 * </ul>
 */
public record SearedPatternData(List<MeltingEntry> inputs, List<CastEntry> casts) {

    public static final int MAX_INPUTS = 8;
    public static final int MAX_CASTS = 8;

    public static final SearedPatternData EMPTY = new SearedPatternData(List.of(), List.of());

    public static final Codec<SearedPatternData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MeltingEntry.CODEC.listOf().optionalFieldOf("inputs", List.of()).forGetter(SearedPatternData::inputs),
            CastEntry.CODEC.listOf().optionalFieldOf("casts", List.of()).forGetter(SearedPatternData::casts)
    ).apply(instance, SearedPatternData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SearedPatternData> STREAM_CODEC =
            StreamCodec.of(SearedPatternData::writeToBuffer, SearedPatternData::readFromBuffer);

    public SearedPatternData {
        inputs = List.copyOf(inputs);
        casts = List.copyOf(casts);
    }

    public boolean isEmpty() {
        return inputs.isEmpty() && casts.isEmpty();
    }

    public int inputCount() {
        int count = 0;
        for (MeltingEntry entry : inputs) {
            count += entry.count();
        }
        return count;
    }

    /** Returns a copy with the given entry replacing any previous entry for the same item. */
    public SearedPatternData withInput(MeltingEntry entry) {
        List<MeltingEntry> newInputs = new ArrayList<>(inputs);
        newInputs.removeIf(existing -> existing.item().equals(entry.item()));
        if (newInputs.size() < MAX_INPUTS) {
            newInputs.add(entry);
        }
        return new SearedPatternData(newInputs, casts);
    }

    /** Adds a cast entry when no entry for the same fluid exists yet. */
    public SearedPatternData withCast(CastEntry cast) {
        if (casts.stream().anyMatch(existing -> existing.fluid().equals(cast.fluid()))) {
            return this;
        }
        List<CastEntry> newCasts = new ArrayList<>(casts);
        if (newCasts.size() < MAX_CASTS) {
            newCasts.add(cast);
        }
        return new SearedPatternData(inputs, newCasts);
    }

    private static void writeToBuffer(RegistryFriendlyByteBuf buffer, SearedPatternData data) {
        buffer.writeVarInt(data.inputs.size());
        for (MeltingEntry entry : data.inputs) {
            entry.item().writeToPacket(buffer);
            buffer.writeVarInt(entry.count());
            entry.fluid().writeToPacket(buffer);
            buffer.writeVarInt(entry.fluidAmount());
        }
        buffer.writeVarInt(data.casts.size());
        for (CastEntry cast : data.casts) {
            cast.fluid().writeToPacket(buffer);
            buffer.writeVarInt(cast.fluidAmount());
            cast.result().writeToPacket(buffer);
            buffer.writeVarInt(cast.resultCount());
        }
    }

    private static SearedPatternData readFromBuffer(RegistryFriendlyByteBuf buffer) {
        int inputCount = Math.min(buffer.readVarInt(), 64);
        List<MeltingEntry> inputs = new ArrayList<>(inputCount);
        for (int i = 0; i < inputCount; i++) {
            AEItemKey item = AEItemKey.fromPacket(buffer);
            int count = buffer.readVarInt();
            AEFluidKey fluid = AEFluidKey.fromPacket(buffer);
            int fluidAmount = buffer.readVarInt();
            if (item != null && fluid != null) {
                inputs.add(new MeltingEntry(item, Math.max(1, count), fluid, Math.max(1, fluidAmount)));
            }
        }
        int castCount = Math.min(buffer.readVarInt(), 64);
        List<CastEntry> casts = new ArrayList<>(castCount);
        for (int i = 0; i < castCount; i++) {
            AEFluidKey fluid = AEFluidKey.fromPacket(buffer);
            int fluidAmount = buffer.readVarInt();
            AEItemKey result = AEItemKey.fromPacket(buffer);
            int resultCount = buffer.readVarInt();
            if (fluid != null && result != null) {
                casts.add(new CastEntry(fluid, Math.max(1, fluidAmount), result, Math.max(1, resultCount)));
            }
        }
        return new SearedPatternData(inputs, casts);
    }

    /** One item that will be pushed into the smeltery, with the fluid the melting recipe yields. */
    public record MeltingEntry(AEItemKey item, int count, AEFluidKey fluid, int fluidAmount) {

        public static final Codec<MeltingEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                AEItemKey.CODEC.fieldOf("item").forGetter(MeltingEntry::item),
                Codec.INT.fieldOf("count").forGetter(MeltingEntry::count),
                AEFluidKey.CODEC.fieldOf("fluid").forGetter(MeltingEntry::fluid),
                Codec.INT.fieldOf("fluid_amount").forGetter(MeltingEntry::fluidAmount)
        ).apply(instance, MeltingEntry::new));
    }

    /** A basin casting recipe that converts the pattern fluid back into an item. */
    public record CastEntry(AEFluidKey fluid, int fluidAmount, AEItemKey result, int resultCount) {

        public static final Codec<CastEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                AEFluidKey.CODEC.fieldOf("fluid").forGetter(CastEntry::fluid),
                Codec.INT.fieldOf("fluid_amount").forGetter(CastEntry::fluidAmount),
                AEItemKey.CODEC.fieldOf("result").forGetter(CastEntry::result),
                Codec.INT.fieldOf("result_count").forGetter(CastEntry::resultCount)
        ).apply(instance, CastEntry::new));
    }
}
