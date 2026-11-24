package org.moliyy.tinkers_rainbow.smeltery;

/**
 * What the smeltery output is converted into before it leaves the block.
 *
 * <p>The declaration order is kept stable on purpose: it is stored as an ordinal in NBT, so old worlds
 * keep their mode.</p>
 */
public enum OutputMode {
    /** Tank fluid is exported as fluid. */
    FLUID,
    /** Tank fluid is cast with basin recipes (no cast) and exported as items. */
    BASIN,
    /** Tank fluid is cast with table recipes using the cast in the cast slot. */
    TABLE;

    public static OutputMode byId(int id) {
        OutputMode[] values = values();
        return id >= 0 && id < values.length ? values[id] : FLUID;
    }
}
