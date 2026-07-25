package fr.iglee42.resourcefulshulkers.network.handler;

import fr.iglee42.resourcefulshulkers.aura.AuraOverlay;
import fr.iglee42.resourcefulshulkers.network.data.AuraSyncPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AuraSyncPacketHandler {

    public static void handle(final AuraSyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            AuraOverlay.set(payload.aura());
        });
    }

}
