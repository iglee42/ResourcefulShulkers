package fr.iglee42.resourcefulshulkers.network.handler;

import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.network.data.GeneratorDurabilitySyncPayload;
import fr.iglee42.resourcefulshulkers.network.data.GeneratorIndexChangePayload;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class GeneratorIndexChangePacketHandler {

    public static final GeneratorIndexChangePacketHandler INSTANCE = new GeneratorIndexChangePacketHandler();

    public static GeneratorIndexChangePacketHandler instance(){
        return INSTANCE;
    }


    public void handle(final GeneratorIndexChangePayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            context.enqueueWork(() -> {
                if(ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(payload.player()).level().getBlockEntity(payload.pos()) instanceof GeneratingBoxBlockEntity tile) {
                    tile.setGeneratedIndex(payload.index());
                }

            });
        });
    }

}
