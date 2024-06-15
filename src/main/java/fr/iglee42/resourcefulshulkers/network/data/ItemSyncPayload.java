package fr.iglee42.resourcefulshulkers.network.data;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

public record ItemSyncPayload(
        BlockPos pos,
        int slot,
        ItemStack stack
) implements CustomPacketPayload {

    public static final Type<ItemSyncPayload> TYPE = new Type<>(new ResourceLocation(MODID, "item_sync"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static final StreamCodec<RegistryFriendlyByteBuf, ItemSyncPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ItemSyncPayload::pos,
            ByteBufCodecs.INT, ItemSyncPayload::slot,
            ItemStack.OPTIONAL_STREAM_CODEC, ItemSyncPayload::stack,
            ItemSyncPayload::new
    );
}
