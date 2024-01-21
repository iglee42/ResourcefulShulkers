package fr.iglee42.techresourcesshulker.network.packets;

import fr.iglee42.techresourcesshulker.blocks.entites.GeneratingBoxBlockEntity;
import fr.iglee42.techresourcesshulker.blocks.entites.ShulkerPedestalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ItemStackSyncS2CPacket {
    private final ItemStack stack;
    private final BlockPos pos;

    private final int slot;

    public ItemStackSyncS2CPacket(ItemStack stack,int slot, BlockPos pos) {
        this.stack = stack;
        this.slot = slot;
        this.pos = pos;
    }

    public ItemStackSyncS2CPacket(FriendlyByteBuf buf) {
        slot = buf.readInt();
        stack = buf.readItem();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(slot);
        buf.writeItem(stack);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof GeneratingBoxBlockEntity tile) {
                tile.getInventory().setStackInSlot(slot,stack);
            } else if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof ShulkerPedestalBlockEntity tile){
                tile.setStack(stack);
            }

        });
        return true;
    }
}