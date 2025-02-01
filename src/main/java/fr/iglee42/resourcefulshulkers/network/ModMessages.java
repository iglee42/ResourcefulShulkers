package fr.iglee42.resourcefulshulkers.network;


import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.network.data.*;
import fr.iglee42.resourcefulshulkers.network.handler.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ResourcefulShulkers.MODID,bus = EventBusSubscriber.Bus.MOD)
public class ModMessages {
    @SubscribeEvent
    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(ResourcefulShulkers.MODID);

        registrar.playToClient(GeneratorTickSyncPayload.TYPE, GeneratorTickSyncPayload.STREAM_CODEC, GeneratorTickSyncPacketHandler.instance()::handle);
        registrar.playToClient(GeneratorDurabilitySyncPayload.TYPE, GeneratorDurabilitySyncPayload.STREAM_CODEC, GeneratorDurabilitySyncPacketHandler.instance()::handle);
        registrar.playToClient(ItemStackSyncPayload.TYPE, ItemStackSyncPayload.STREAM_CODEC, ItemStackSyncPacketHandler.instance()::handle);
        registrar.playToClient(AuraSyncPayload.TYPE, AuraSyncPayload.STREAM_CODEC, AuraSyncPacketHandler.instance()::handle);


        registrar.playToServer(GeneratorIndexChangePayload.TYPE, GeneratorIndexChangePayload.STREAM_CODEC, GeneratorIndexChangePacketHandler.instance()::handle);

    }

}
