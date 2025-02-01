package fr.iglee42.resourcefulshulkers.network.data;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public record GeneratorTickSyncPayload(
        BlockPos pos,
        int ticks
) implements CustomPacketPayload {

    public static final Type<GeneratorTickSyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "ticks_sync"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static final StreamCodec<RegistryFriendlyByteBuf, GeneratorTickSyncPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, GeneratorTickSyncPayload::pos,
            ByteBufCodecs.INT, GeneratorTickSyncPayload::ticks,
            GeneratorTickSyncPayload::new
    );
}
