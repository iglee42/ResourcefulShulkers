package fr.iglee42.techresourcesshulker.item;

import fr.iglee42.techresourcesshulker.ModContent;
import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.init.ModBlocks;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class ShulkerInfuserItem extends Item {
    public ShulkerInfuserItem() {
        super(new Properties().tab(TechResourcesShulker.GROUP));
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().getBlockState(context.getClickedPos()).is(ModBlocks.ENERGY_INSERTER.get())){
            context.getLevel().setBlockAndUpdate(context.getClickedPos().offset(0,1,0),ModBlocks.SHULKER_INFUSER.get().defaultBlockState());
            if (!context.getPlayer().isCreative())context.getPlayer().getMainHandItem().setCount(context.getPlayer().getMainHandItem().getCount() - 1);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    @Override
    public String getDescriptionId() {
        return "block.techresourcesshulker.shulker_infuser";
    }
}
