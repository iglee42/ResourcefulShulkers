package fr.iglee42.techresourcesshulker;

import fr.iglee42.techresourcesshulker.customize.Types;
import fr.iglee42.techresourcesshulker.menu.slot.BoxShellSlot;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;

public class ClientEvents {

    public static void onTextureStitch(TextureStitchEvent.Pre event)
    {
        if(event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS))
        {
            event.addSprite(BoxShellSlot.EMPTY_SHELL_SLOT);
        }
    }
    public static void registerItemColors(ColorHandlerEvent.Item event){
        Types.TYPES.forEach(r-> event.getItemColors().register((p_92672_, p_92673_) -> r.shellItemColor(),ModContent.getShellById(r.id())));
    }

}
