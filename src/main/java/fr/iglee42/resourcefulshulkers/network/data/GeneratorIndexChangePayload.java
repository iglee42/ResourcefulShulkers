package fr.iglee42.resourcefulshulkers.network.data;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.structure.endcity.EndCityBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GeneratorIndexChangePayload(
        BlockPos pos,
        int slot,
        int index
) implements CustomPacketPayload {

    public static final Type<GeneratorIndexChangePayload> TYPE = new Type<>(RSIds.id("index_change"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static final StreamCodec<RegistryFriendlyByteBuf, GeneratorIndexChangePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, GeneratorIndexChangePayload::pos,
            ByteBufCodecs.INT, GeneratorIndexChangePayload::slot,
            ByteBufCodecs.INT, GeneratorIndexChangePayload::index,
            GeneratorIndexChangePayload::new
    );

    public static void handle(GeneratorIndexChangePayload payload, IPayloadContext ctx){
        ctx.enqueueWork(()->{
            if (!(ctx.player() instanceof ServerPlayer player)) return;
            Level level = player.level();
            BlockPos pos = payload.pos();

            if (level.getBlockEntity(pos) instanceof GeneratingBoxBlockEntity be){
                be.setItemIndex(payload.index());
            }

            if (level.getBlockEntity(pos) instanceof EndCityBlockEntity be){
                be.setItemIndex(payload.slot(), payload.index());
            }
        });
    }
}
