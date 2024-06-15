package fr.iglee42.resourcefulshulkers.network.handler;

import fr.iglee42.resourcefulshulkers.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerPedestalBlockEntity;
import fr.iglee42.resourcefulshulkers.network.data.ItemSyncPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ItemSyncPacketHandler {

    public static final ItemSyncPacketHandler INSTANCE = new ItemSyncPacketHandler();

    public static ItemSyncPacketHandler instance(){
        return INSTANCE;
    }


    public void handle(final ItemSyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(payload.pos()) instanceof GeneratingBoxBlockEntity tile) {
                tile.getInventory().setStackInSlot(payload.slot(),payload.stack());
            } else if (Minecraft.getInstance().level.getBlockEntity(payload.pos()) instanceof ShulkerPedestalBlockEntity tile){
                tile.setStack(payload.stack());
            }
        });
    }

}
