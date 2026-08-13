package fr.iglee42.resourcefulshulkers.registries;

import fr.iglee42.resourcefulshulkers.RSIds;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@EventBusSubscriber(modid = RSIds.MODID)
public enum RSSkullTypes implements SkullBlock.Type, StringRepresentable {

    SHULKER("shulker", ()-> RSBlocks.SHULKER_HEAD.get(), ()-> RSBlocks.WALL_SHULKER_HEAD.get());

    private final String name;
    private final Supplier<Block> block;
    private final Supplier<Block> wallBlock;

    RSSkullTypes(String name, Supplier<Block> block, Supplier<Block> wallBlock) {
        this.name = name;
        this.block = block;
        this.wallBlock = wallBlock;
        SkullBlock.Type.TYPES.put(name, this);
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    @SubscribeEvent
    public static void modifyBlockEntities(BlockEntityTypeAddBlocksEvent event){
        for (RSSkullTypes type : RSSkullTypes.values()) {
            event.modify(BlockEntityType.SKULL, type.block.get(), type.wallBlock.get());
        }
    }

}
