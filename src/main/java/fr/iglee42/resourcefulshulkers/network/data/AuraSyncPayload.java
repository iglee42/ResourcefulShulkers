package fr.iglee42.resourcefulshulkers.network.data;

import fr.iglee42.resourcefulshulkers.RSIds;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record AuraSyncPayload(
        int aura
) implements CustomPacketPayload {

    public static final Type<AuraSyncPayload> TYPE = new Type<>(RSIds.id("aura_sync"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static final StreamCodec<RegistryFriendlyByteBuf, AuraSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, AuraSyncPayload::aura,
            AuraSyncPayload::new
    );
}