package fr.iglee42.resourcefulshulkers.network;

import fr.iglee42.resourcefulshulkers.network.data.AuraSyncPayload;
import fr.iglee42.resourcefulshulkers.network.data.GeneratingBoxSyncPayload;
import fr.iglee42.resourcefulshulkers.network.data.ItemSyncPayload;
import fr.iglee42.resourcefulshulkers.network.handler.AuraSyncPacketHandler;
import fr.iglee42.resourcefulshulkers.network.handler.GeneratingBoxSyncPacketHandler;
import fr.iglee42.resourcefulshulkers.network.handler.ItemSyncPacketHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

@EventBusSubscriber(modid = MODID,bus = EventBusSubscriber.Bus.MOD)
public class ModMessages {
    @SubscribeEvent
    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(MODID);

        registrar.playToClient(GeneratingBoxSyncPayload.TYPE, GeneratingBoxSyncPayload.STREAM_CODEC, GeneratingBoxSyncPacketHandler.instance()::handle);
        registrar.playToClient(AuraSyncPayload.TYPE, AuraSyncPayload.STREAM_CODEC, AuraSyncPacketHandler.instance()::handle);
        registrar.playToClient(ItemSyncPayload.TYPE, ItemSyncPayload.STREAM_CODEC, ItemSyncPacketHandler.instance()::handle);


    }

}
