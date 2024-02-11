package fr.iglee42.techresourcesshulker.network.packets;

import fr.iglee42.techresourcesshulker.aura.ClientAuraData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShulkerAuraSyncS2CPacket {

    private final int chunkMana;

    public ShulkerAuraSyncS2CPacket(int chunkMana) {
        this.chunkMana = chunkMana;
    }

    public ShulkerAuraSyncS2CPacket(FriendlyByteBuf buf) {
        chunkMana = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(chunkMana);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ClientAuraData.set(chunkMana);
        });
        return true;
    }
}