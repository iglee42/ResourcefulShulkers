package fr.iglee42.resourcefulshulkers.menu.slot;

import com.mojang.datafixers.util.Pair;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BoxShellSlot extends SlotItemHandler {
    public static final ResourceLocation EMPTY_SHELL_SLOT = new ResourceLocation(ResourcefulShulkers.MODID, "item/empty_shell");
    private final ResourceLocation resourceId;

    public BoxShellSlot(IItemHandler h, int id, int x, int y, ResourceLocation resourceId) {
        super(h,id,x,y);
        this.resourceId = resourceId;
    }

    @Nullable
    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS,EMPTY_SHELL_SLOT);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem() == ModItems.getShellById(resourceId);
    }
}
