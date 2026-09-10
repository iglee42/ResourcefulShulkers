package fr.iglee42.resourcefulshulkers.network.data;

import net.minecraft.network.FriendlyByteBuf;

public record AuraSyncPayload(
        int aura
) {

    public AuraSyncPayload(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(aura);
    }
}