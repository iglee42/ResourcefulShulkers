package fr.iglee42.resourcefulshulkers.network.packets;

import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GeneratorDurabilitySyncS2CPacket {
    private final BlockPos pos;

    private final int durability;

    public GeneratorDurabilitySyncS2CPacket(int durability, BlockPos pos) {
        this.durability = durability;
        this.pos = pos;
    }

    public GeneratorDurabilitySyncS2CPacket(FriendlyByteBuf buf) {
        durability = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(durability);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof GeneratingBoxBlockEntity tile) {
                tile.setDurability(durability);
            }

        });
        return true;
    }
}