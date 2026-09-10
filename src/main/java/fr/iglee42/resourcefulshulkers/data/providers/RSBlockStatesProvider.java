package fr.iglee42.resourcefulshulkers.data.providers;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.blocks.structure.StructureBlock;
import fr.iglee42.resourcefulshulkers.registries.RSBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RSBlockStatesProvider extends BlockStateProvider {

    public RSBlockStatesProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, RSIds.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(RSBlocks.PURPUR_TARGET.get());
        simpleBlockItem(RSBlocks.PURPUR_TARGET.get(),models().getExistingFile(RSIds.id("block/purpur_target")));
        simpleBlockWithItem(RSBlocks.SHULKER_ABSORBER.get(), models().getExistingFile(RSIds.id("block/shulker_absorber")));
        simpleBlockWithItem(RSBlocks.SHULKER_INFUSER.get(), models().getExistingFile(RSIds.id("block/shulker_infuser")));
        simpleBlockWithItem(RSBlocks.SHULKER_PEDESTAL.get(), models().getExistingFile(RSIds.id("block/shulker_pedestal")));
        simpleBlock(RSBlocks.SHULKER_HEAD.get(), models().getExistingFile(ResourceLocation.withDefaultNamespace("block/skull")));
        simpleBlock(RSBlocks.WALL_SHULKER_HEAD.get(), models().getExistingFile(ResourceLocation.withDefaultNamespace("block/skull")));

        simpleBlock(RSBlocks.END_CITY.get(), models().getExistingFile(RSIds.id("block/end_city/main_1")));
        simpleBlock(RSBlocks.END_CITY_TIER_2.get(), models().getExistingFile(RSIds.id("block/end_city/main_2")));
        simpleBlock(RSBlocks.TRAINER.get(), models().getExistingFile(RSIds.id("block/shulker_trainer")));
        generateStructure();

    }

    private void generateStructure(){
        getVariantBuilder(RSBlocks.STRUCTURE.get()).forAllStates(state->{
            StructureBlock.Part part = state.getValue(StructureBlock.PART);
            Direction dir = state.getValue(StructureBlock.FACING);
            if (!part.equals(StructureBlock.Part.TOP_CORNER) && !part.equals(StructureBlock.Part.TOP_EDGE)){
                ModelFile model = models().getExistingFile(RSIds.id("block/end_city/"+part.getSerializedName()));
                return ConfiguredModel.builder().uvLock(part.getSerializedName().startsWith("bottom_"))
                        .modelFile(model)
                        .rotationY((int) ((dir.toYRot() + 180) % 360))
                        .build();
            }
            if (part.equals(StructureBlock.Part.TOP_EDGE)) {
                boolean alt = dir.equals(Direction.EAST) || dir.equals(Direction.SOUTH);
                ModelFile model = models().getExistingFile(RSIds.id("block/end_city/"+part.getSerializedName() + (alt ? "_alt" : "")));
                return ConfiguredModel.builder()
                        .modelFile(model)
                        .rotationY((int) ((dir.toYRot() + 180) % 360))
                        .build();
            }
            String modelId = "_"+ switch (dir){
                case NORTH -> "se";
                case SOUTH -> "nw";
                case WEST -> "ne";
                case EAST -> "sw";
                default -> throw new IllegalStateException("Unexpected value: " + dir);
            };
            ModelFile model = models().getExistingFile(RSIds.id("block/end_city/"+part.getSerializedName() + modelId));
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .build();
        });
    }
}
