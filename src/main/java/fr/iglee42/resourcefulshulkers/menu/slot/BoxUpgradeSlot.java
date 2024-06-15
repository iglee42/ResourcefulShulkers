package fr.iglee42.resourcefulshulkers.menu.slot;

import com.mojang.datafixers.util.Pair;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.item.UpgradeItem;
import fr.iglee42.resourcefulshulkers.utils.Upgrade;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BoxUpgradeSlot extends SlotItemHandler {
    public static final ResourceLocation EMPTY_UPGRADE_SLOT = new ResourceLocation(ResourcefulShulkers.MODID, "item/empty_upgrade");

    public BoxUpgradeSlot(IItemHandler h, int id, int x, int y) {
        super(h,id,x,y);
    }

    @Nullable
    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS,EMPTY_UPGRADE_SLOT);
    }

    @Override
    public int getMaxStackSize() {
        return Upgrade.MAX;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem() instanceof UpgradeItem upg && !Upgrade.inventoryContainsUpgrade(this.getItemHandler(),upg.getUpgrade());
    }
}
