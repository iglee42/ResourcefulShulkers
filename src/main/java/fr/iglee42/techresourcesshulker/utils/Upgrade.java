package fr.iglee42.techresourcesshulker.utils;

import fr.iglee42.igleelib.api.utils.ModsUtils;
import fr.iglee42.techresourcesshulker.item.UpgradeItem;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import static fr.iglee42.techresourcesshulker.TechResourcesShulker.MODID;

public enum Upgrade {
    SPEED(),
    DURABILITY(),

    ;
    public static final int MAX = 4;



    Upgrade() {

    }

    public static boolean inventoryContainsUpgrade(IItemHandler inventory,Upgrade upgrade){
        for (int i = 0; i < inventory.getSlots(); i++){
            if (inventory.getStackInSlot(i).getItem() instanceof UpgradeItem upg){
                if (upg.getUpgrade() == upgrade) return true;
            }
        }
        return false;
    }

    public static int getFirstInventoryIndexWithUpgrade(IItemHandler inventory, Upgrade upgrade) {
        for(int i = 0; i < inventory.getSlots(); ++i) {
            ItemStack currentStack = inventory.getStackInSlot(i);
            if (!currentStack.isEmpty() && currentStack.getItem() instanceof UpgradeItem u&& u.getUpgrade() == upgrade) {
                return i;
            }
        }

        return -1;
    }

    public String getName(){
        return ModsUtils.getUpperName(name(),"_");
    }

    public MutableComponent getDescription(){
        return new TranslatableComponent("tooltip."+MODID+ "."+name().toLowerCase()+"_upgrade");
    }

}
