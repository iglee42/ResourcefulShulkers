package fr.iglee42.resourcefulshulkers.network.client;

import fr.iglee42.resourcefulshulkers.aura.AuraOverlay;
import fr.iglee42.resourcefulshulkers.network.data.AuraSyncPayload;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AuraSyncPacketHandler {

    public static void handle(AuraSyncPayload payload, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            AuraOverlay.set(payload.aura());
        });
    }

}
