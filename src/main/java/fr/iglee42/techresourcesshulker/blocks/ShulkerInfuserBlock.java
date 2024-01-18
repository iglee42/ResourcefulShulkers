package fr.iglee42.techresourcesshulker.blocks;

import fr.iglee42.igleelib.api.utils.ModsUtils;
import fr.iglee42.techresourcesshulker.ModContent;
import fr.iglee42.techresourcesshulker.blocks.entites.ShulkerInfuserBlockEntity;
import fr.iglee42.techresourcesshulker.blocks.entites.ShulkerPedestalBlockEntity;
import fr.iglee42.techresourcesshulker.init.ModBlocks;
import fr.iglee42.techresourcesshulker.init.ModItems;
import fr.iglee42.techresourcesshulker.recipes.ShulkerItemInfusionRecipe;
import fr.iglee42.techresourcesshulker.recipes.ShulkerRecipeEnvironnement;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShulkerInfuserBlock extends BaseEntityBlock {

    public static final EnumProperty<ShulkerInfuserBlockEntity.Mode> MODE = EnumProperty.create("mode", ShulkerInfuserBlockEntity.Mode.class);

    public ShulkerInfuserBlock() {
        super(Properties.of(Material.METAL).noOcclusion());
        registerDefaultState(defaultBlockState().setValue(MODE, ShulkerInfuserBlockEntity.Mode.ENVIRONNEMENT_INFUSION));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShulkerInfuserBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModContent.SHULKER_INFUSER_BLOCK_ENTITY.get(), ShulkerInfuserBlockEntity::tick);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    public boolean propagatesSkylightDown(BlockState p_49100_, BlockGetter p_49101_, BlockPos p_49102_) {
        return true;
    }


    public float getShadeBrightness(BlockState p_49094_, BlockGetter p_49095_, BlockPos p_49096_) {
        return 1.0F;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        VoxelShape shape = Shapes.empty();
        if (p_60555_.getValue(MODE) == ShulkerInfuserBlockEntity.Mode.ENVIRONNEMENT_INFUSION) {
            shape = Shapes.join(shape, Shapes.box(0, 0.9375, 0, 1, 1, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.3125, 0.5625, 0.3125, 0.6875, 0.9375, 0.6875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(-0.0625, 0.9375, 0.25, 0, 1.125, 0.75), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(1, 0.9375, 0.25, 1.0625, 1.125, 0.75), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.25, 0.9375, -0.0625, 0.75, 1.125, 0), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.25, 0.9375, 1, 0.75, 1.125, 1.0625), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.1875, 0.375, 0.5625, 0.8125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.625, 0, 0.1875, 0.8125, 0.5625, 0.8125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.375, 0, 0.625, 0.625, 0.5625, 0.8125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.375, 0, 0.1875, 0.625, 0.5625, 0.375), BooleanOp.OR);
        } else if (p_60555_.getValue(MODE) == ShulkerInfuserBlockEntity.Mode.ITEM_INFUSION){
            shape = Shapes.join(shape, Shapes.box(0.5625, 0.9375, 0, 1, 1, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0, 0.9375, 0, 0.4375, 1, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.4375, 0.9375, 0, 0.5625, 1, 0.4375), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.4375, 0.9375, 0.5625, 0.5625, 1, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.5625, 0.625, 0.3125, 0.6875, 0.9375, 0.4375), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.5625, 0.625, 0.5625, 0.6875, 0.9375, 0.6875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.5625, 0.875, 0.4375, 0.6875, 0.9375, 0.5625), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.3125, 0.875, 0.4375, 0.4375, 0.9375, 0.5625), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.4375, 0.875, 0.3125, 0.5625, 0.9375, 0.4375), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.4375, 0.875, 0.5625, 0.5625, 0.9375, 0.6875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.3125, 0.625, 0.5625, 0.4375, 0.9375, 0.6875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.3125, 0.625, 0.3125, 0.4375, 0.9375, 0.4375), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(-0.0625, 0.9375, 0.25, 0, 1.125, 0.75), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(1, 0.9375, 0.25, 1.0625, 1.125, 0.75), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.25, 0.9375, -0.0625, 0.75, 1.125, 0), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.25, 0.9375, 1, 0.75, 1.125, 1.0625), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.3125, 0.5625, 0.3125, 0.6875, 0.625, 0.6875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.1875, 0.375, 0.5625, 0.8125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.625, 0, 0.1875, 0.8125, 0.5625, 0.8125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.375, 0, 0.625, 0.625, 0.5625, 0.8125), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.375, 0, 0.1875, 0.625, 0.5625, 0.375), BooleanOp.OR);

        }
        return shape;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(ModItems.SHULKER_INFUSER_ITEM.get());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if (level.isClientSide) return InteractionResult.sidedSuccess(true);
        if (player.getMainHandItem().is(Items.AIR)) {
            if (player.isCrouching()){
                switch (state.getValue(MODE)){
                    case ENVIRONNEMENT_INFUSION -> {
                        level.setBlockAndUpdate(pos,state.setValue(MODE, ShulkerInfuserBlockEntity.Mode.ITEM_INFUSION));
                        player.displayClientMessage(new TextComponent("Mode : " + ShulkerInfuserBlockEntity.Mode.ITEM_INFUSION.getTextName()),true);
                    }
                    case ITEM_INFUSION -> {
                        level.setBlockAndUpdate(pos,state.setValue(MODE, ShulkerInfuserBlockEntity.Mode.SHULKER_FUSION));
                        player.displayClientMessage(new TextComponent("Mode : " + ShulkerInfuserBlockEntity.Mode.SHULKER_FUSION.getTextName()),true);
                    }
                    case SHULKER_FUSION -> {
                        level.setBlockAndUpdate(pos,state.setValue(MODE, ShulkerInfuserBlockEntity.Mode.ENVIRONNEMENT_INFUSION));
                        player.displayClientMessage(new TextComponent("Mode : " + ShulkerInfuserBlockEntity.Mode.ENVIRONNEMENT_INFUSION.getTextName()),true);
                    }
                }
            } else {
                VoxelShape working = Shapes.box(0, 0, 0, 1, 2, 1).move(pos.getX(), pos.getY(), pos.getZ());
                LivingEntity target = level.getNearestEntity(level.getEntitiesOfClass(LivingEntity.class, working.bounds()), TargetingConditions.DEFAULT, null, pos.getX(), pos.getY(), pos.getZ());
                if (target == null) {
                    player.displayClientMessage(new TextComponent("There is no entity on the infuser").withStyle(ChatFormatting.RED), true);
                    return InteractionResult.FAIL;
                }
                if (level.getRecipeManager().getAllRecipesFor(ShulkerRecipeEnvironnement.Type.INSTANCE).stream().anyMatch(r -> {
                    boolean flag = r.getBaseEntity().equals(target.getType().getRegistryName());
                    Biome b = level.getBiomeManager().getBiome(pos).value();
                    boolean flag1 = r.getAllowedBiomes().contains(b.getRegistryName()) || r.getAllowedBiomesTags().stream().anyMatch(bt -> level.getBiomeManager().getBiome(pos).containsTag(new TagKey<>(Registry.BIOME_REGISTRY,bt)));
                    boolean flag2 = pos.getY() >= r.getMinY() && pos.getY() <= r.getMaxY();
                    return flag && flag1 && flag2 && state.getValue(MODE) == ShulkerInfuserBlockEntity.Mode.ENVIRONNEMENT_INFUSION;
                })) {
                    ShulkerRecipeEnvironnement recipe = level.getRecipeManager().getAllRecipesFor(ShulkerRecipeEnvironnement.Type.INSTANCE).stream().filter(r -> {
                        boolean flag = r.getBaseEntity().equals(target.getType().getRegistryName());
                        Biome b = level.getBiomeManager().getBiome(pos).value();
                        boolean flag1 = r.getAllowedBiomes().contains(b.getRegistryName()) || r.getAllowedBiomesTags().stream().anyMatch(bt -> level.getBiomeManager().getBiome(pos).containsTag(new TagKey<>(Registry.BIOME_REGISTRY,bt)));
                        boolean flag2 = pos.getY() >= r.getMinY() && pos.getY() <= r.getMaxY();
                        return flag && flag1 && flag2;
                    }).findFirst().get();
                    ((ShulkerInfuserBlockEntity) level.getBlockEntity(pos)).start(recipe);

                }

                if (level.getRecipeManager().getAllRecipesFor(ShulkerItemInfusionRecipe.Type.INSTANCE).stream().anyMatch(r -> {
                    boolean flag = true;
                    List<Ingredient> pedestalIngredients = new ArrayList<>(Arrays.stream(r.getPedestalsIngredients()).toList());
                    pedestalIngredients.removeIf(i->i==Ingredient.EMPTY);
                    for (int[] pedestalPos : ShulkerItemInfusionRecipe.PEDESTAL_POSITION){
                        if (!level.getBlockState(pos.offset(pedestalPos[0],pedestalPos[1],pedestalPos[2])).is(ModBlocks.SHULKER_PEDESTAL.get())) flag = false;
                        ItemStack stack = ((ShulkerPedestalBlockEntity)level.getBlockEntity(pos.offset(pedestalPos[0],pedestalPos[1],pedestalPos[2]))).getStack();
                        if (pedestalIngredients.stream().anyMatch(i->i.getItems()[0].equals(stack,false)))
                            pedestalIngredients.remove(pedestalIngredients.stream().filter(i->i.getItems()[0].equals(stack,false)).findFirst().get());
                    }

                    boolean flag1 = r.getBaseEntity().equals(target.getType().getRegistryName());
                    boolean flag2 = pedestalIngredients.isEmpty();

                    return flag && flag1 && flag2 && state.getValue(MODE) == ShulkerInfuserBlockEntity.Mode.ITEM_INFUSION;
                })) {
                    ShulkerItemInfusionRecipe recipe = level.getRecipeManager().getAllRecipesFor(ShulkerItemInfusionRecipe.Type.INSTANCE).stream().filter(r -> {
                        boolean flag = true;
                        List<Ingredient> pedestalIngredients = new ArrayList<>(Arrays.stream(r.getPedestalsIngredients()).toList());
                        pedestalIngredients.removeIf(i->i==Ingredient.EMPTY);
                        for (int[] pedestalPos : ShulkerItemInfusionRecipe.PEDESTAL_POSITION){
                            if (!level.getBlockState(pos.offset(pedestalPos[0],pedestalPos[1],pedestalPos[2])).is(ModBlocks.SHULKER_PEDESTAL.get())) flag = false;
                            ItemStack stack = ((ShulkerPedestalBlockEntity)level.getBlockEntity(pos.offset(pedestalPos[0],pedestalPos[1],pedestalPos[2]))).getStack();
                            if (pedestalIngredients.stream().anyMatch(i->i.getItems()[0].equals(stack,false)))
                                pedestalIngredients.remove(pedestalIngredients.stream().filter(i->i.getItems()[0].equals(stack,false)).findFirst().get());
                        }

                        boolean flag1 = r.getBaseEntity().equals(target.getType().getRegistryName());
                        boolean flag2 = pedestalIngredients.isEmpty();

                        return flag && flag1 && flag2 && state.getValue(MODE) == ShulkerInfuserBlockEntity.Mode.ITEM_INFUSION;
                    }).findFirst().get();
                    ((ShulkerInfuserBlockEntity) level.getBlockEntity(pos)).start(recipe);

                }
            }
        }
        return super.use(state, level, pos, player, p_60507_, p_60508_);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(MODE, ShulkerInfuserBlockEntity.Mode.ENVIRONNEMENT_INFUSION);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MODE);
    }
}

