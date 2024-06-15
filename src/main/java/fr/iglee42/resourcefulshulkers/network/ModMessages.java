package fr.iglee42.resourcefulshulkers.network;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.network.packets.GeneratingTickSyncS2CPacket;
import fr.iglee42.resourcefulshulkers.network.packets.GeneratorDurabilitySyncS2CPacket;
import fr.iglee42.resourcefulshulkers.network.packets.ItemStackSyncS2CPacket;
import fr.iglee42.resourcefulshulkers.network.packets.ShulkerAuraSyncS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ResourcefulShulkers.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;


        net.messageBuilder(ItemStackSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ItemStackSyncS2CPacket::new)
                .encoder(ItemStackSyncS2CPacket::toBytes)
                .consumerMainThread(ItemStackSyncS2CPacket::handle)
                .add();
        net.messageBuilder(GeneratingTickSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GeneratingTickSyncS2CPacket::new)
                .encoder(GeneratingTickSyncS2CPacket::toBytes)
                .consumerMainThread(GeneratingTickSyncS2CPacket::handle)
                .add();
        net.messageBuilder(GeneratorDurabilitySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GeneratorDurabilitySyncS2CPacket::new)
                .encoder(GeneratorDurabilitySyncS2CPacket::toBytes)
                .consumerMainThread(GeneratorDurabilitySyncS2CPacket::handle)
                .add();
        net.messageBuilder(ShulkerAuraSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ShulkerAuraSyncS2CPacket::new)
                .encoder(ShulkerAuraSyncS2CPacket::toBytes)
                .consumerMainThread(ShulkerAuraSyncS2CPacket::handle)
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
