package fr.iglee42.resourcefulshulkers.blocks.structure;

import fr.iglee42.resourcefulshulkers.blocks.entites.structure.endcity.EndCityBlockEntity;
import fr.iglee42.resourcefulshulkers.item.RSTooltipHandler;
import fr.iglee42.resourcefulshulkers.registries.RSBlockEntities;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EndCityBlock extends StructuredBlock {

    private static final VoxelShape SHAPE = box(0, 0, 0, 16, 8, 16);
    private final int tier;

    public EndCityBlock(Properties properties, int tier) {
        super(properties);
        this.tier = tier;
    }


    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EndCityBlockEntity(blockPos, blockState, tier);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> current) {
        return current.equals(RSBlockEntities.END_CITY.get()) ? (lvl, pos, state, be) -> EndCityBlockEntity.staticTick(lvl, pos, state, (EndCityBlockEntity) be) : null;
    }


    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, List<Component> tooltips, TooltipFlag p_49819_) {
        RSTooltipHandler.Builder builder = RSTooltipHandler.tooltip(stack, tooltips)
                .shift(Screen.hasShiftDown());
        if (tier == 1)
            builder.args(0, tier * 4);
        else
            builder.append("tooltip.resourcefulshulkers.end_city", tier * 4);
        builder.apply();
        super.appendHoverText(stack, ctx, tooltips, p_49819_);
    }
}
