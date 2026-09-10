package fr.iglee42.resourcefulshulkers.network;


import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.network.data.*;
import fr.iglee42.resourcefulshulkers.network.client.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = RSIds.MODID)
public class RSPayloads {

    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(RSIds.id("messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;


        // S2C
        net.messageBuilder(AuraSyncPayload.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(AuraSyncPayload::new)
                .encoder(AuraSyncPayload::toBytes)
                .consumerMainThread(AuraSyncPacketHandler::handle)
                .add();

        // C2S
        net.messageBuilder(GeneratorIndexChangePayload.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(GeneratorIndexChangePayload::new)
                .encoder(GeneratorIndexChangePayload::toBytes)
                .consumerMainThread(GeneratorIndexChangePayload::handle)
                .add();

    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

}
