package fr.iglee42.resourcefulshulkers.blocks;

import fr.iglee42.igleelib.api.utils.ModsUtils;
import fr.iglee42.resourcefulshulkers.init.ModBlockEntities;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerInfuserBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerPedestalBlockEntity;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerItemInfusionRecipe;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerRecipeEnvironnement;
import fr.iglee42.resourcefulshulkers.utils.ShulkersManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ShulkerInfuserBlock extends BaseEntityBlock {


    public ShulkerInfuserBlock() {
        super(Properties.of(Material.METAL).noOcclusion().strength(1.5F,6.0F));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShulkerInfuserBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.SHULKER_INFUSER_BLOCK_ENTITY.get(), ShulkerInfuserBlockEntity::tick);
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

        shape = Shapes.join(shape, Shapes.box(0, 0.9375, 0, 1, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5625, 0.625, 0.3125, 0.6875, 0.875, 0.4375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5625, 0.625, 0.5625, 0.6875, 0.875, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.875, 0.3125, 0.6875, 0.9375, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.625, 0.5625, 0.4375, 0.875, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.625, 0.3125, 0.4375, 0.875, 0.4375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.0625, 0.9375, 0.25, 0, 1.125, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(1, 0.9375, 0.25, 1.0625, 1.125, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.9375, -0.0625, 0.75, 1.125, 0), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.9375, 1, 0.75, 1.125, 1.0625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.5625, 0.3125, 0.6875, 0.625, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.5625, 0.8125), BooleanOp.OR);

        return shape;
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if (level.isClientSide) return InteractionResult.sidedSuccess(true);
        if (player.getMainHandItem().is(Items.AIR)) {
            if (player.isCrouching()){
                for (int[] pedestalPos : ShulkerItemInfusionRecipe.PEDESTAL_POSITION) {
                    if (level.getBlockState(new BlockPos(pos.getX() + pedestalPos[0],pos.getY() + pedestalPos[1], pos.getZ() + pedestalPos[2])).isAir())
                        ModsUtils.placeGhostBlock((ServerLevel) level,new BlockPos(pos.getX() + pedestalPos[0],pos.getY() + pedestalPos[1], pos.getZ() + pedestalPos[2]),ModBlocks.SHULKER_PEDESTAL.get().defaultBlockState(),10*20);
                }
                return InteractionResult.SUCCESS;
            }
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
                    return flag && flag1 && flag2;
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
                        if (pedestalIngredients.stream().anyMatch(i->i.test(stack)))
                            pedestalIngredients.remove(pedestalIngredients.stream().filter(i->i.test(stack)).findFirst().get());
                    }

                    boolean flag1 = r.getBaseEntity().startsWith("#")?
                            ForgeRegistries.ENTITIES.tags().getTag(ForgeRegistries.ENTITIES.tags().createTagKey(new ResourceLocation(r.getBaseEntity().substring(1)))).contains(target.getType()) :
                            new ResourceLocation(r.getBaseEntity()).equals(target.getType().getRegistryName());
                    //boolean flag1 = r.getBaseEntity().equals(target.getType().getRegistryName());
                    boolean flag2 = pedestalIngredients.isEmpty();

                    return flag && flag1 && flag2;
                })) {
                    ShulkerItemInfusionRecipe recipe = level.getRecipeManager().getAllRecipesFor(ShulkerItemInfusionRecipe.Type.INSTANCE).stream().filter(r -> {
                        boolean flag = true;
                        List<Ingredient> pedestalIngredients = new ArrayList<>(Arrays.stream(r.getPedestalsIngredients()).toList());
                        pedestalIngredients.removeIf(i->i==Ingredient.EMPTY);
                        for (int[] pedestalPos : ShulkerItemInfusionRecipe.PEDESTAL_POSITION){
                            if (!level.getBlockState(pos.offset(pedestalPos[0],pedestalPos[1],pedestalPos[2])).is(ModBlocks.SHULKER_PEDESTAL.get())) flag = false;
                            ItemStack stack = ((ShulkerPedestalBlockEntity)level.getBlockEntity(pos.offset(pedestalPos[0],pedestalPos[1],pedestalPos[2]))).getStack();
                            if (pedestalIngredients.stream().anyMatch(i->i.test(stack)))
                                pedestalIngredients.remove(pedestalIngredients.stream().filter(i->i.test(stack)).findFirst().get());
                        }

                        boolean flag1 = r.getBaseEntity().startsWith("#")?
                            ForgeRegistries.ENTITIES.tags().getTag(ForgeRegistries.ENTITIES.tags().createTagKey(new ResourceLocation(r.getBaseEntity().substring(1)))).contains(target.getType()) :
                            new ResourceLocation(r.getBaseEntity()).equals(target.getType().getRegistryName());
                        //boolean flag1 = r.getBaseEntity().equals(target.getType().getRegistryName());
                        boolean flag2 = pedestalIngredients.isEmpty();

                        return flag && flag1 && flag2;
                    }).findFirst().get();
                    ((ShulkerInfuserBlockEntity) level.getBlockEntity(pos)).start(recipe);

            }
        }
        return super.use(state, level, pos, player, p_60507_, p_60508_);
    }

}

