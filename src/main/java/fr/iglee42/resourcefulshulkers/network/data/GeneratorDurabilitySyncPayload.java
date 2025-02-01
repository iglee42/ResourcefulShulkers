package fr.iglee42.resourcefulshulkers.network.data;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public record GeneratorDurabilitySyncPayload(
        BlockPos pos,
        int durability
) implements CustomPacketPayload {

    public static final Type<GeneratorDurabilitySyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "durability_sync"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static final StreamCodec<RegistryFriendlyByteBuf, GeneratorDurabilitySyncPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, GeneratorDurabilitySyncPayload::pos,
            ByteBufCodecs.INT, GeneratorDurabilitySyncPayload::durability,
            GeneratorDurabilitySyncPayload::new
    );
}
