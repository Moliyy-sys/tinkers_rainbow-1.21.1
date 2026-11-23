/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  appeng.api.networking.IGridNodeListener
 *  appeng.api.networking.IGridNodeListener$State
 *  appeng.me.helpers.IGridConnectedBlockEntity
 */
package appeng.me.helpers;

import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridNodeListener;
import appeng.me.helpers.IGridConnectedBlockEntity;

public class BlockEntityNodeListener<T extends IGridConnectedBlockEntity>
implements IGridNodeListener<T> {
    public static final BlockEntityNodeListener<IGridConnectedBlockEntity> INSTANCE = new BlockEntityNodeListener();

    public void onSaveChanges(T nodeOwner, IGridNode node) {
        nodeOwner.saveChanges();
    }

    public void onStateChanged(T nodeOwner, IGridNode node, IGridNodeListener.State state) {
        nodeOwner.onMainNodeStateChanged(state);
    }
}
