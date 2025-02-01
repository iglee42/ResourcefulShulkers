package fr.iglee42.resourcefulshulkers.network.handler;

import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.network.data.GeneratorDurabilitySyncPayload;
import fr.iglee42.resourcefulshulkers.network.data.GeneratorTickSyncPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class GeneratorDurabilitySyncPacketHandler {

    public static final GeneratorDurabilitySyncPacketHandler INSTANCE = new GeneratorDurabilitySyncPacketHandler();

    public static GeneratorDurabilitySyncPacketHandler instance(){
        return INSTANCE;
    }


    public void handle(final GeneratorDurabilitySyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            context.enqueueWork(() -> {
                if(Minecraft.getInstance().level.getBlockEntity(payload.pos()) instanceof GeneratingBoxBlockEntity tile) {
                    tile.setDurability(payload.durability());
                }

            });
        });
    }

}
