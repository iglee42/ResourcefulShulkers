package fr.iglee42.techresourcesshulker.events;

import fr.iglee42.techresourcesshulker.ModContent;
import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import fr.iglee42.techresourcesshulker.client.blockentites.ShulkerPedestalRenderer;
import fr.iglee42.techresourcesshulker.client.entites.CustomShulkerRenderer;
import fr.iglee42.techresourcesshulker.customize.Types;
import fr.iglee42.techresourcesshulker.init.ModBlocks;
import fr.iglee42.techresourcesshulker.menu.slot.BoxShellSlot;
import fr.iglee42.techresourcesshulker.menu.slot.BoxUpgradeSlot;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TechResourcesShulker.MODID,bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientEvents {

    public static void onTextureStitch(TextureStitchEvent.Pre event)
    {
        if(event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS))
        {
            event.addSprite(BoxShellSlot.EMPTY_SHELL_SLOT);
            event.addSprite(BoxUpgradeSlot.EMPTY_UPGRADE_SLOT);
        }
    }
    public static void registerItemColors(ColorHandlerEvent.Item event){
        Types.TYPES.forEach(r-> event.getItemColors().register((p_92672_, p_92673_) -> r.shellItemColor(), ModContent.getShellById(r.id())));
    }
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModContent.OVERWORLD_SHULKER.get(), CustomShulkerRenderer::new);
        EntityRenderers.register(ModContent.SKY_SHULKER.get(), CustomShulkerRenderer::new);
        EntityRenderers.register(ModContent.NETHER_SHULKER.get(), CustomShulkerRenderer::new);
        EntityRenderers.register(ModContent.END_SHULKER.get(), CustomShulkerRenderer::new);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SHULKER_INFUSER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ENERGY_INSERTER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SHULKER_PEDESTAL.get(), RenderType.cutout());
        BlockEntityRenderers.register(ModContent.SHULKER_PEDESTAL_BLOCK_ENTITY.get(), ShulkerPedestalRenderer::new);
    }

}
