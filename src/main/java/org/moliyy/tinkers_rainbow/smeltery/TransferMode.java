package org.moliyy.tinkers_rainbow.smeltery;

/**
 * How output is transferred out of the smeltery.
 */
public enum TransferMode {
    /** Transfer automatically every operation, up to the configured limit. */
    AUTO,
    /** Transfer only until exactly the configured limit has been moved, then stop until reconfigured. */
    BLOCK,
    /** Transfer only when the player presses the return button. */
    MANUAL;

    public static TransferMode byId(int id) {
        TransferMode[] values = values();
        return id >= 0 && id < values.length ? values[id] : AUTO;
    }
}
