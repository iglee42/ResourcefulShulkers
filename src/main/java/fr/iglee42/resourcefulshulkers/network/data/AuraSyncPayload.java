package fr.iglee42.resourcefulshulkers.network.data;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public record AuraSyncPayload(
        int aura
) implements CustomPacketPayload {

    public static final Type<AuraSyncPayload> TYPE = new Type<>(new ResourceLocation(MODID, "aura_sync"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static final StreamCodec<RegistryFriendlyByteBuf, AuraSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, AuraSyncPayload::aura,
            AuraSyncPayload::new
    );
}
