package fr.iglee42.resourcefulshulkers.network;


import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.network.data.*;
import fr.iglee42.resourcefulshulkers.network.client.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = RSIds.MODID)
public class RSPayloads {
    @SubscribeEvent
    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(RSIds.MODID);

        // S2C
        registrar.playToClient(AuraSyncPayload.TYPE, AuraSyncPayload.STREAM_CODEC, AuraSyncPacketHandler::handle);

        // C2S
        registrar.playToServer(GeneratorIndexChangePayload.TYPE, GeneratorIndexChangePayload.STREAM_CODEC, GeneratorIndexChangePayload::handle);

    }

}
