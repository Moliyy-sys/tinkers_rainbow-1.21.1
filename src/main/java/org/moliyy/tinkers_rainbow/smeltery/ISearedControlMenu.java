package org.moliyy.tinkers_rainbow.smeltery;

import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * Implemented by the menus of both blocks that share the seared output controls (AE interface and high
 * speed cooler). The control payload uses it to reach the block entity without referencing AE2 classes.
 */
public interface ISearedControlMenu {

    AbstractSearedOutputBlockEntity getSearedOutputEntity();

    /** Reads one of the synchronised {@link SearedMenuData} values (client side safe). */
    int getMenuData(int index);

    /** Convenience accessor kept for callers that need the menu itself. */
    default AbstractContainerMenu asMenu() {
        return (AbstractContainerMenu) this;
    }
}
