package fr.iglee42.techresourcesshulker.network.packets;

import fr.iglee42.techresourcesshulker.blocks.entites.GeneratingBoxBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GeneratingTickSyncS2CPacket {
    private final BlockPos pos;

    private final int ticks;

    public GeneratingTickSyncS2CPacket(int ticks, BlockPos pos) {
        this.ticks = ticks;
        this.pos = pos;
    }

    public GeneratingTickSyncS2CPacket(FriendlyByteBuf buf) {
        ticks = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(ticks);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof GeneratingBoxBlockEntity tile) {
                tile.setGeneratingTicks(ticks);
            }

        });
        return true;
    }
}