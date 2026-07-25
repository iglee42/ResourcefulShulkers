package fr.iglee42.resourcefulshulkers.events;

import fr.iglee42.resourcefulshulkers.client.blockentites.GeneratingBoxRenderer;
import fr.iglee42.resourcefulshulkers.client.blockentites.ShulkerPedestalRenderer;
import fr.iglee42.resourcefulshulkers.client.entites.CustomShulkerBulletRenderer;
import fr.iglee42.resourcefulshulkers.client.entites.CustomShulkerRenderer;
import fr.iglee42.resourcefulshulkers.client.screen.GeneratingBoxScreen;
import fr.iglee42.resourcefulshulkers.init.ModBlockEntities;
import fr.iglee42.resourcefulshulkers.init.ModBlocks;
import fr.iglee42.resourcefulshulkers.registries.RSItems;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager;
import fr.iglee42.resourcefulshulkers.registries.RSSkullTypes;
import fr.iglee42.resourcefulshulkers.utils.Type;
import fr.iglee42.resourcefulshulkers.utils.TypesManager;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.awt.*;
import java.util.function.Supplier;

import static fr.iglee42.resourcefulshulkers.ResourcefulShulkers.MODID;

@EventBusSubscriber(modid = MODID,bus = EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientEvents {

    private static final ModelLayerLocation shulkerLayer = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MODID,"shulker_head"), "main");
    
    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event){
        ShulkersManager.forEachShulker(r->
                event.register((stack,index)->
                        index == 0 ? FastColor.ARGB32.opaque(r.definition().color()): 0xffffff, r.shell().get()
                ));
    }

    @SubscribeEvent
    static void registerSkullModels(EntityRenderersEvent.CreateSkullModels event) {
        EntityModelSet modelSet = event.getEntityModelSet();
        event.registerSkullModel(RSSkullTypes.SHULKER,new SkullModel(modelSet.bakeLayer(shulkerLayer)));
    }

    @SubscribeEvent
    public static void registerMenus(RegisterMenuScreensEvent event){
        event.register(ModBlockEntities.GENERATING_BOX_MENU.get(),GeneratingBoxScreen::new);
    }
    @SubscribeEvent
    public static <T extends Entity> void clientSetup(FMLClientSetupEvent event) {
        //EntityRenderers.register(ModEntities.OVERWORLD_SHULKER.get(), CustomShulkerRenderer::new);
        //EntityRenderers.register(ModEntities.SKY_SHULKER.get(), CustomShulkerRenderer::new);
        //EntityRenderers.register(ModEntities.NETHER_SHULKER.get(), CustomShulkerRenderer::new);
        //EntityRenderers.register(ModEntities.END_SHULKER.get(), CustomShulkerRenderer::new);
        TypesManager.TYPES.stream().filter(Type::shouldCreateEntity).forEach(t->{
            EntityRenderers.register(TypesManager.ENTITY_TYPES.get(t.id()).get(),CustomShulkerRenderer::new);
        });
        /*ShulkersManager.TYPES.forEach(r->{
            EntityRenderers.register(ShulkersManager.ENTITY_TYPES.get(r.id()).get(), CustomShulkerRenderer::new);
            EntityRenderers.register(ShulkersManager.BULLET_TYPES.get(r.id()).get(), CustomShulkerBulletRenderer::new);
        });*/
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SHULKER_INFUSER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SHULKER_PEDESTAL.get(), RenderType.cutout());
        BlockEntityRenderers.register(ModBlockEntities.SHULKER_PEDESTAL_BLOCK_ENTITY.get(), ShulkerPedestalRenderer::new);

        event.enqueueWork(()->{
            SkullBlockRenderer.SKIN_BY_TYPE.put(RSSkullTypes.SHULKER,ResourceLocation.withDefaultNamespace("textures/entity/shulker/shulker.png"));
        });

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

        Supplier<LayerDefinition> shulkerHead = (()->LayerDefinition.create(meshDefinition, 64, 64));

        event.registerLayerDefinition(shulkerLayer, shulkerHead);
    }
}
