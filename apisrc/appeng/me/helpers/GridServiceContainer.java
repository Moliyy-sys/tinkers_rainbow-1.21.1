/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  appeng.api.networking.IGridServiceProvider
 */
package appeng.me.helpers;

import appeng.api.networking.IGridServiceProvider;
import java.util.Map;

public record GridServiceContainer(Map<Class<?>, IGridServiceProvider> services, IGridServiceProvider[] serverStartTickServices, IGridServiceProvider[] levelStartTickServices, IGridServiceProvider[] levelEndtickServices, IGridServiceProvider[] serverEndTickServices) {
}
