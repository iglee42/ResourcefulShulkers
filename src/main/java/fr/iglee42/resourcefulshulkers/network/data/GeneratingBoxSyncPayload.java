package fr.iglee42.resourcefulshulkers.network.data;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public record GeneratingBoxSyncPayload(
        BlockPos pos,
        int ticks,
        int durability
) implements CustomPacketPayload {

    public static final Type<GeneratingBoxSyncPayload> TYPE = new Type<>(new ResourceLocation(MODID, "generating_box_sync"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static final StreamCodec<RegistryFriendlyByteBuf, GeneratingBoxSyncPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, GeneratingBoxSyncPayload::pos,
            ByteBufCodecs.INT, GeneratingBoxSyncPayload::ticks,
            ByteBufCodecs.INT, GeneratingBoxSyncPayload::durability,
            GeneratingBoxSyncPayload::new
    );
}
