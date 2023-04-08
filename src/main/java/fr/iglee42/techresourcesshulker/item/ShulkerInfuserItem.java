package fr.iglee42.techresourcesshulker.item;

import fr.iglee42.techresourcesshulker.ModContent;
import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class ShulkerInfuserItem extends Item {
    public ShulkerInfuserItem() {
        super(new Properties().tab(TechResourcesShulker.GROUP));
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().getBlockState(context.getClickedPos()).is(ModContent.ENERGY_INPUTER.get())){
            context.getLevel().setBlockAndUpdate(context.getClickedPos().offset(0,1,0),ModContent.SHULKER_INFUSER.get().defaultBlockState());
            if (!context.getPlayer().isCreative())context.getPlayer().getMainHandItem().setCount(context.getPlayer().getMainHandItem().getCount() - 1);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
}
