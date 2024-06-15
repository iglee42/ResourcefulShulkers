package fr.iglee42.resourcefulshulkers.network.handler;

import fr.iglee42.resourcefulshulkers.aura.ClientAuraData;
import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.network.data.AuraSyncPayload;
import fr.iglee42.resourcefulshulkers.network.data.GeneratingBoxSyncPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AuraSyncPacketHandler {

    public static final AuraSyncPacketHandler INSTANCE = new AuraSyncPacketHandler();

    public static AuraSyncPacketHandler instance(){
        return INSTANCE;
    }


    public void handle(final AuraSyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientAuraData.set(payload.aura());
        });
    }

}
