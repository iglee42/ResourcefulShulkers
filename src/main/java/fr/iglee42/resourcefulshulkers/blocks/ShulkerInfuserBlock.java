package fr.iglee42.resourcefulshulkers.blocks;

import fr.iglee42.igleelib.api.utils.ModsUtils;
import fr.iglee42.igleelib.common.network.data.CreateGhostBlockPayload;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkersConfig;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerInfuserBlockEntity;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerPedestalBlockEntity;
import fr.iglee42.resourcefulshulkers.init.ModBlockEntities;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerItemInfusionRecipe;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerRecipeEnvironment;
import fr.iglee42.resourcefulshulkers.utils.CommonUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ShulkerInfuserBlock extends Block implements EntityBlock {


    public ShulkerInfuserBlock() {
        super(Properties.of().noOcclusion().strength(1.5F,6.0F));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShulkerInfuserBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type.equals(ModBlockEntities.SHULKER_INFUSER_BLOCK_ENTITY.get()) ? (lvl,pos,st,be)->((ShulkerInfuserBlockEntity)be).tickEntity(lvl,pos,st) : null;
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
    protected VoxelShape getCollisionShape(BlockState p_60572_, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
        return Shapes.join(getShape(p_60572_, p_60573_, p_60574_, p_60575_), Shapes.box(0, 15/16f, 0, 1, 1, 1), BooleanOp.OR);
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
        //shape = Shapes.join(shape, Shapes.box(-0.0625, 0.9375, 0.25, 0, 1.125, 0.75), BooleanOp.OR);
        //shape = Shapes.join(shape, Shapes.box(1, 0.9375, 0.25, 1.0625, 1.125, 0.75), BooleanOp.OR);
        //shape = Shapes.join(shape, Shapes.box(0.25, 0.9375, -0.0625, 0.75, 1.125, 0), BooleanOp.OR);
        //shape = Shapes.join(shape, Shapes.box(0.25, 0.9375, 1, 0.75, 1.125, 1.0625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.5625, 0.3125, 0.6875, 0.625, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.5625, 0.8125), BooleanOp.OR);

        return shape;
    }


    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult p_60508_) {
        if (level.isClientSide) return InteractionResult.sidedSuccess(true);
        Registry<Biome> biomeRegistry = level.registryAccess().registryOrThrow(Registries.BIOME);
        if (player.getMainHandItem().is(Items.AIR)) {
            if (player.isCrouching()){
                for (int[] pedestalPos : ShulkerItemInfusionRecipe.PEDESTAL_POSITION) {
                    PacketDistributor.sendToAllPlayers(new CreateGhostBlockPayload(new BlockPos(pos.getX() + pedestalPos[0],pos.getY() + pedestalPos[1], pos.getZ() + pedestalPos[2]),ModBlocks.SHULKER_PEDESTAL.get().defaultBlockState(),200));
                    //if (level.getBlockState(new BlockPos(pos.getX() + pedestalPos[0],pos.getY() + pedestalPos[1], pos.getZ() + pedestalPos[2])).isAir())

                        //ModsUtils.placeGhostBlock((ServerLevel) level,new BlockPos(pos.getX() + pedestalPos[0],pos.getY() + pedestalPos[1], pos.getZ() + pedestalPos[2]),ModBlocks.SHULKER_PEDESTAL.get().defaultBlockState(),10*20);
                }
                return InteractionResult.SUCCESS;
            }
                Entity target = CommonUtils.getEntityOnBlock((ServerLevel) level, pos);
                if (target == null) {
                    player.displayClientMessage(Component.literal("There is no entity on the infuser").withStyle(ChatFormatting.RED), true);
                    return InteractionResult.FAIL;
                }
                if (level.getRecipeManager().getAllRecipesFor(ShulkerRecipeEnvironment.Type.INSTANCE).stream().map(RecipeHolder::value).anyMatch(r -> {
                    boolean flag = r.getBaseEntity().equals(BuiltInRegistries.ENTITY_TYPE.getKey(target.getType()));
                    Biome b = level.getBiomeManager().getBiome(pos).value();
                    boolean flag1 = r.getAllowedBiomes().contains(biomeRegistry.getKey(b)) || r.getAllowedBiomesTags().stream().anyMatch(bt -> level.getBiomeManager().getBiome(pos).is(new TagKey<>(Registries.BIOME,bt)));
                    boolean flag2 = pos.getY() >= r.getMinY() && pos.getY() <= r.getMaxY();
                    return flag && flag1 && flag2;
                })) {
                    ShulkerRecipeEnvironment recipe = level.getRecipeManager().getAllRecipesFor(ShulkerRecipeEnvironment.Type.INSTANCE).stream().map(RecipeHolder::value).filter(r -> {
                        boolean flag = r.getBaseEntity().equals(BuiltInRegistries.ENTITY_TYPE.getKey(target.getType()));
                        Biome b = level.getBiomeManager().getBiome(pos).value();
                        boolean flag1 = r.getAllowedBiomes().contains(biomeRegistry.getKey(b)) || r.getAllowedBiomesTags().stream().anyMatch(bt -> level.getBiomeManager().getBiome(pos).is(new TagKey<>(Registries.BIOME,bt)));
                        boolean flag2 = pos.getY() >= r.getMinY() && pos.getY() <= r.getMaxY();
                        return flag && flag1 && flag2;
                    }).findFirst().get();
                    ((ShulkerInfuserBlockEntity) level.getBlockEntity(pos)).start(recipe);

                }

                if (level.getRecipeManager().getAllRecipesFor(ShulkerItemInfusionRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).anyMatch(r -> {
                    boolean flag = true;
                    List<Ingredient> pedestalIngredients = new ArrayList<>(r.getPedestalsIngredients());
                    pedestalIngredients.removeIf(i->i==Ingredient.EMPTY);
                    for (int[] pedestalPos : ShulkerItemInfusionRecipe.PEDESTAL_POSITION){
                        if (!level.getBlockState(pos.offset(pedestalPos[0],pedestalPos[1],pedestalPos[2])).is(ModBlocks.SHULKER_PEDESTAL.get())) flag = false;
                        else {
                            ItemStack stack = ((ShulkerPedestalBlockEntity) level.getBlockEntity(pos.offset(pedestalPos[0], pedestalPos[1], pedestalPos[2]))).getStack();
                            if (pedestalIngredients.stream().anyMatch(i -> i.test(stack)))
                                pedestalIngredients.remove(pedestalIngredients.stream().filter(i -> i.test(stack)).findFirst().get());
                        }
                    }

                    boolean flag1 = r.getBaseEntity().startsWith("#")?
                            BuiltInRegistries.ENTITY_TYPE.getTag(TagKey.create(Registries.ENTITY_TYPE,ResourceLocation.parse(r.getBaseEntity().substring(1)))).get().stream().anyMatch(h->h.value().equals(target.getType())) :
                            ResourceLocation.parse(r.getBaseEntity()).equals(BuiltInRegistries.ENTITY_TYPE.getKey(target.getType()));
                    //boolean flag1 = r.getBaseEntity().equals(target.getType().getRegistryName());
                    boolean flag2 = pedestalIngredients.isEmpty();

                    return flag && flag1 && flag2;
                })) {
                    ShulkerItemInfusionRecipe recipe = level.getRecipeManager().getAllRecipesFor(ShulkerItemInfusionRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).filter(r -> {
                        boolean flag = true;
                        List<Ingredient> pedestalIngredients = new ArrayList<>(r.getPedestalsIngredients());
                        pedestalIngredients.removeIf(i->i==Ingredient.EMPTY);
                        for (int[] pedestalPos : ShulkerItemInfusionRecipe.PEDESTAL_POSITION){
                            if (!level.getBlockState(pos.offset(pedestalPos[0],pedestalPos[1],pedestalPos[2])).is(ModBlocks.SHULKER_PEDESTAL.get())) flag = false;
                            ItemStack stack = ((ShulkerPedestalBlockEntity)level.getBlockEntity(pos.offset(pedestalPos[0],pedestalPos[1],pedestalPos[2]))).getStack();
                            if (pedestalIngredients.stream().anyMatch(i->i.test(stack)))
                                pedestalIngredients.remove(pedestalIngredients.stream().filter(i->i.test(stack)).findFirst().get());
                        }

                        boolean flag1 = r.getBaseEntity().startsWith("#")?
                            BuiltInRegistries.ENTITY_TYPE.getTag(TagKey.create(Registries.ENTITY_TYPE,ResourceLocation.parse(r.getBaseEntity().substring(1)))).get().stream().anyMatch(h->h.value().equals(target.getType())) :
                            ResourceLocation.parse(r.getBaseEntity()).equals(BuiltInRegistries.ENTITY_TYPE.getKey(target.getType()));
                        //boolean flag1 = r.getBaseEntity().equals(target.getType().getRegistryName());
                        boolean flag2 = pedestalIngredients.isEmpty();

                        return flag && flag1 && flag2;
                    }).findFirst().get();
                    ((ShulkerInfuserBlockEntity) level.getBlockEntity(pos)).start(recipe);

            }
        }
        return super.useWithoutItem(state, level, pos, player, p_60508_);
    }
    

    @Override
    public void appendHoverText(ItemStack p_49816_, @Nullable Item.TooltipContext p_49817_, List<Component> tooltips, TooltipFlag p_49819_) {
        if (Screen.hasShiftDown()){
            tooltips.add(Component.translatable("tooltip.resourcefulshulkers.shulker_infuser"));
            tooltips.add(Component.translatable("tooltip.resourcefulshulkers.shulker_infuser1"));
            tooltips.add(Component.translatable("tooltip.resourcefulshulkers.shulker_infuser2",Component.literal(ResourcefulShulkersConfig.NO_AI_AURA.get().toString()).withStyle(ChatFormatting.LIGHT_PURPLE)));
        }
        else tooltips.add(Component.translatable("tooltip.resourcefulshulkers.press_shift"));
        super.appendHoverText(p_49816_, p_49817_, tooltips, p_49819_);
    }
}

