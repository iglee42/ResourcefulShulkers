package fr.iglee42.resourcefulshulkers.blocks;

import fr.iglee42.igleelib.api.utils.ITickableRecipe;
import fr.iglee42.igleelib.common.network.data.CreateGhostBlockPayload;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerInfuserBlockEntity;
import fr.iglee42.resourcefulshulkers.item.RSTooltipHandler;
import fr.iglee42.resourcefulshulkers.recipes.ItemInfusionRecipe;
import fr.iglee42.resourcefulshulkers.registries.RSBlockEntities;
import fr.iglee42.resourcefulshulkers.registries.RSBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShulkerInfuserBlock extends Block implements EntityBlock {

    private static final VoxelShape BASE_SHAPE = Shapes.or(
        box(0,15,0,16,16,16),
            box(4,14,4,12,15,12),
            box(5,9,5,7,14,7),
            box(9,9,5,11,14,7),
            box(5,9,9,7,14,11),
            box(9,9,9,11,14,11),
            box(4,8,4,12,9,12),
            box(4,0,4,12,8,12)
    );

    private static final VoxelShape INTERACTION_SHAPE = Shapes.or(
            BASE_SHAPE,
            box(4,15,-1,12,18,0),
            box(4,15,16,12,18,17),
            box(-1,15,4,0,18,12),
            box(16,15,4,17,18,12)
    );

    public ShulkerInfuserBlock() {
        super(Properties.of().noOcclusion().strength(1.5F, 6.0F));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShulkerInfuserBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type.equals(RSBlockEntities.SHULKER_INFUSER.get()) ? (lvl, pos, st, be) -> ((ShulkerInfuserBlockEntity) be).tickEntity(lvl, pos, st) : null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }


    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return BASE_SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return INTERACTION_SHAPE;
    }


    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult p_60508_) {
        if (level.isClientSide) return InteractionResult.sidedSuccess(true);
        if (!(level.getBlockEntity(pos) instanceof ShulkerInfuserBlockEntity be)) return InteractionResult.PASS;
        if (!player.getMainHandItem().isEmpty()) return InteractionResult.PASS;
        if (player.isCrouching()) {
            for (int[] pedestalOffset : ItemInfusionRecipe.PEDESTAL_POSITION) {
                BlockPos pedestalPos = new BlockPos(pos.getX() + pedestalOffset[0], pos.getY() + pedestalOffset[1], pos.getZ() + pedestalOffset[2]);
                PacketDistributor.sendToPlayer((ServerPlayer) player,new CreateGhostBlockPayload(pedestalPos, RSBlocks.SHULKER_PEDESTAL.get().defaultBlockState(), 100));
            }
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }

        Entity target = be.getCurrentTarget();
        if (target == null) {
            player.displayClientMessage(Component.literal("There is no entity on the infuser").withStyle(ChatFormatting.RED), true);
            return InteractionResult.FAIL;
        }

        ITickableRecipe<ShulkerInfuserBlockEntity> recipe = be.findRecipe().orElse(null);
        if (recipe == null) return InteractionResult.FAIL;
        be.start(recipe);
        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext p_49817_, List<Component> tooltips, TooltipFlag p_49819_) {
        RSTooltipHandler.addTooltip(stack, tooltips, Screen.hasShiftDown());
        super.appendHoverText(stack, p_49817_, tooltips, p_49819_);
    }
}

