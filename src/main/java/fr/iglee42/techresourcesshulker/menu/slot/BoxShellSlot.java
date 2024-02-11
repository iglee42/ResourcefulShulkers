package fr.iglee42.techresourcesshulker.menu.slot;

import com.mojang.datafixers.util.Pair;
import fr.iglee42.techresourcesshulker.init.ModEntities;
import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BoxShellSlot extends SlotItemHandler {
    public static final ResourceLocation EMPTY_SHELL_SLOT = new ResourceLocation(TechResourcesShulker.MODID, "item/empty_shell");
    private final ResourceLocation resourceId;

    public BoxShellSlot(IItemHandler h, int id, int x, int y,ResourceLocation resourceId) {
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
