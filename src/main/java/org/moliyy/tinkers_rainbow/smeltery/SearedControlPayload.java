package org.moliyy.tinkers_rainbow.smeltery;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import org.moliyy.tinkers_rainbow.TinkersRainbow;

/**
 * Client -> server control packet shared by the AE seared interface and the high speed cooler.
 *
 * <p>All control actions (limit change, transfer mode change, manual return, output mode change) go
 * through this single payload and are applied to the block entity of the player's currently open menu,
 * so a forged packet cannot touch a block the player is not using.</p>
 */
public record SearedControlPayload(int action, int value) implements CustomPacketPayload {

    public static final Type<SearedControlPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "seared_control"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SearedControlPayload> STREAM_CODEC =
            StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.VAR_INT,
                    SearedControlPayload::action,
                    net.minecraft.network.codec.ByteBufCodecs.VAR_INT,
                    SearedControlPayload::value,
                    SearedControlPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void register(net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(TYPE, STREAM_CODEC, SearedControlPayload::handle);
    }

    private static void handle(SearedControlPayload payload, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer serverPlayer)) {
            return;
        }
        if (serverPlayer.containerMenu instanceof ISearedControlMenu menu) {
            AbstractSearedOutputBlockEntity blockEntity = menu.getSearedOutputEntity();
            if (blockEntity != null && blockEntity.stillValid(serverPlayer)) {
                blockEntity.applyControlAction(payload.action(), payload.value());
            }
        }
    }
}
