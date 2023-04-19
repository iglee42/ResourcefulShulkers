package fr.iglee42.techresourcesshulker.utils;

import fr.iglee42.igleelib.api.utils.ModsUtils;
import fr.iglee42.techresourcesshulker.item.UpgradeItem;
import net.minecraftforge.items.IItemHandler;

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
                if (upg.getUpgrade() == upgrade && inventory.getStackInSlot(i).getCount() == MAX) return true;
            }
        }
        return false;
    }

    public String getName(){
        return ModsUtils.getUpperName(name(),"_");
    }

}
