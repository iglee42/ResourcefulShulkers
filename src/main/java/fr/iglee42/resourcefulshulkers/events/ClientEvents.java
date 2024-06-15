package fr.iglee42.resourcefulshulkers.events;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.client.blockentites.GeneratingBoxRenderer;
import fr.iglee42.resourcefulshulkers.client.screen.GeneratingBoxScreen;
import fr.iglee42.resourcefulshulkers.init.ModBlockEntities;
import fr.iglee42.resourcefulshulkers.init.ModEntities;
import fr.iglee42.resourcefulshulkers.client.blockentites.ShulkerPedestalRenderer;
import fr.iglee42.resourcefulshulkers.client.entites.CustomShulkerBulletRenderer;
import fr.iglee42.resourcefulshulkers.client.entites.CustomShulkerRenderer;
import fr.iglee42.resourcefulshulkers.init.ModItems;
import fr.iglee42.resourcefulshulkers.utils.ShulkersManager;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.menu.slot.BoxShellSlot;
import fr.iglee42.resourcefulshulkers.menu.slot.BoxUpgradeSlot;
import fr.iglee42.resourcefulshulkers.aura.AuraOverlay;
import fr.iglee42.resourcefulshulkers.utils.SkullTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.GuiLayerManager;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.function.Supplier;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

@EventBusSubscriber(modid = MODID,bus = EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientEvents {

    private static final ModelLayerLocation shulkerLayer = new ModelLayerLocation(new ResourceLocation(MODID,"shulker_head"), "main");

    @SubscribeEvent
    public static void onTextureStitch(TextureAtlasStitchedEvent event)
    {
        if(event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS))
        {
            //event.addSprite(BoxShellSlot.EMPTY_SHELL_SLOT);
            //event.addSprite(BoxUpgradeSlot.EMPTY_UPGRADE_SLOT);
        }
    }
    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event){
        ShulkersManager.TYPES.forEach(r-> event.getItemColors().register((p_92672_, p_92673_) -> r.getShellColor(), ModItems.getShellById(r.id())));
    }

    @SubscribeEvent
    static void registerSkullModels(EntityRenderersEvent.CreateSkullModels event) {
        EntityModelSet modelSet = event.getEntityModelSet();
        event.registerSkullModel(SkullTypes.SHULKER,new SkullModel(modelSet.bakeLayer(shulkerLayer)));
    }
    @SubscribeEvent
    public static <T extends Entity> void clientSetup(FMLClientSetupEvent event) {
        //MenuScreens.register(ModBlockEntities.GENERATING_BOX_MENU.get(), GeneratingBoxScreen::new);
        EntityRenderers.register(ModEntities.OVERWORLD_SHULKER.get(), CustomShulkerRenderer::new);
        EntityRenderers.register(ModEntities.SKY_SHULKER.get(), CustomShulkerRenderer::new);
        EntityRenderers.register(ModEntities.NETHER_SHULKER.get(), CustomShulkerRenderer::new);
        EntityRenderers.register(ModEntities.END_SHULKER.get(), CustomShulkerRenderer::new);
        ShulkersManager.TYPES.forEach(r->{
            EntityRenderers.register(ShulkersManager.ENTITY_TYPES.get(r.id()).get(), CustomShulkerRenderer::new);
            EntityRenderers.register(ShulkersManager.BULLET_TYPES.get(r.id()).get(), CustomShulkerBulletRenderer::new);
        });
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SHULKER_INFUSER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SHULKER_PEDESTAL.get(), RenderType.cutout());
        BlockEntityRenderers.register(ModBlockEntities.SHULKER_PEDESTAL_BLOCK_ENTITY.get(), ShulkerPedestalRenderer::new);

        event.enqueueWork(()->{
            SkullBlockRenderer.SKIN_BY_TYPE.put(SkullTypes.SHULKER,new ResourceLocation("textures/entity/shulker/shulker.png"));
        });

    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiLayersEvent event){
        event.registerAbove(VanillaGuiLayers.HOTBAR,new ResourceLocation(MODID,"aura"),AuraOverlay.HUD_AURA);
    }

    @SubscribeEvent
    public static void registerMenu(RegisterMenuScreensEvent event){
        event.register(ModBlockEntities.GENERATING_BOX_MENU.get(),GeneratingBoxScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.GENERATING_BOX_BLOCK_ENTITY.get(), GeneratingBoxRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayersDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDef = meshDefinition.getRoot();
        partDef.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 52).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.ZERO);

        Supplier<LayerDefinition> shulkerHead = Lazy.of(()->LayerDefinition.create(meshDefinition, 64, 64));

        event.registerLayerDefinition(shulkerLayer, shulkerHead);
    }
}
