package fr.iglee42.resourcefulshulkers.network.handler;

import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.network.data.GeneratingBoxSyncPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class GeneratingBoxSyncPacketHandler {

    public static final GeneratingBoxSyncPacketHandler INSTANCE = new GeneratingBoxSyncPacketHandler();

    public static GeneratingBoxSyncPacketHandler instance(){
        return INSTANCE;
    }


    public void handle(final GeneratingBoxSyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(payload.pos()) instanceof GeneratingBoxBlockEntity tile) {
                tile.setGeneratingTicks(payload.ticks());
                tile.setDurability(payload.durability());
            }
        });
    }

}
