package fr.iglee42.resourcefulshulkers.network.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public record GeneratorIndexChangePayload(
        UUID player,
        BlockPos pos,
        int index
) implements CustomPacketPayload {

    public static final Type<GeneratorIndexChangePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "index_change"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static final StreamCodec<RegistryFriendlyByteBuf, GeneratorIndexChangePayload> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, GeneratorIndexChangePayload::player,
            BlockPos.STREAM_CODEC, GeneratorIndexChangePayload::pos,
            ByteBufCodecs.INT, GeneratorIndexChangePayload::index,
            GeneratorIndexChangePayload::new
    );
}
