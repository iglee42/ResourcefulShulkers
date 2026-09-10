package fr.iglee42.resourcefulshulkers.registries;

import com.google.common.collect.ImmutableSet;
import fr.iglee42.resourcefulshulkers.RSIds;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = RSIds.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public enum RSSkullTypes implements SkullBlock.Type, StringRepresentable {

    SHULKER("shulker", ()-> RSBlocks.SHULKER_HEAD.get(), ()-> RSBlocks.WALL_SHULKER_HEAD.get());

    private final String name;
    private final Supplier<Block> block;
    private final Supplier<Block> wallBlock;

    RSSkullTypes(String name, Supplier<Block> block, Supplier<Block> wallBlock) {
        this.name = name;
        this.block = block;
        this.wallBlock = wallBlock;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    @SubscribeEvent
    public static void modifyBlockEntities(FMLCommonSetupEvent event){
        event.enqueueWork(()->{
            ImmutableSet.Builder<Block> builder = ImmutableSet.builder();
            builder.addAll(BlockEntityType.SKULL.validBlocks);
            builder.add(RSSkullTypes.SHULKER.block.get(), RSSkullTypes.SHULKER.wallBlock.get());
            BlockEntityType.SKULL.validBlocks = builder.build();
        });
    }

}
