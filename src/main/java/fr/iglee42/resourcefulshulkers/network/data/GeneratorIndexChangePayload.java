package fr.iglee42.resourcefulshulkers.network.data;

import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.structure.endcity.EndCityBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record GeneratorIndexChangePayload(
        BlockPos pos,
        int slot,
        int index
) {

    public GeneratorIndexChangePayload(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt(), buf.readInt());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(slot);
        buf.writeInt(index);
    }

    public static void handle(GeneratorIndexChangePayload payload, Supplier<NetworkEvent.Context> ctxSupplier){
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(()->{
            ServerPlayer player;
            if ((player = ctx.getSender()) == null) return;
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
