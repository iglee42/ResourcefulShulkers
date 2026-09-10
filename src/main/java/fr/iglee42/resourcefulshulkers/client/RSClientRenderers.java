package fr.iglee42.resourcefulshulkers.client;

import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.client.blockentites.EndCityRenderer;
import fr.iglee42.resourcefulshulkers.client.blockentites.GeneratingBoxRenderer;
import fr.iglee42.resourcefulshulkers.client.blockentites.ShulkerPedestalRenderer;
import fr.iglee42.resourcefulshulkers.client.blockentites.TrainerRenderer;
import fr.iglee42.resourcefulshulkers.client.entites.CustomShulkerRenderer;
import fr.iglee42.resourcefulshulkers.client.entites.ResourceShulkerBulletRenderer;
import fr.iglee42.resourcefulshulkers.client.items.GeneratingBoxItemExtension;
import fr.iglee42.resourcefulshulkers.registries.RSBlockEntities;
import fr.iglee42.resourcefulshulkers.registries.RSSkullTypes;
import fr.iglee42.resourcefulshulkers.shulkers.ShulkersManager;
import fr.iglee42.resourcefulshulkers.types.TypesManager;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static fr.iglee42.resourcefulshulkers.RSIds.MODID;

@Mod.EventBusSubscriber(modid = RSIds.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RSClientRenderers {

    private static final ModelLayerLocation SHULKER_LAYER = new ModelLayerLocation(RSIds.id("shulker_head"), "main");

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event){
        event.enqueueWork(()->{
            SkullBlockRenderer.SKIN_BY_TYPE.put(RSSkullTypes.SHULKER,ResourceLocation.withDefaultNamespace("textures/entity/shulker/shulker.png"));
        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        ShulkersManager.forEachShulker(shulker->{
            if (shulker.bulletType() != null)
                event.registerEntityRenderer(shulker.bulletType().get(), ResourceShulkerBulletRenderer::new);
            if (shulker.entityType() != null)
                event.registerEntityRenderer(shulker.entityType().get(), CustomShulkerRenderer::new);
        });

        TypesManager.forEachType(type->{
            if (type.entityType() != null)
                event.registerEntityRenderer(type.entityType().get(), CustomShulkerRenderer::new);
        });

        event.registerBlockEntityRenderer(RSBlockEntities.SHULKER_PEDESTAL.get(), ShulkerPedestalRenderer::new);
        event.registerBlockEntityRenderer(RSBlockEntities.GENERATING_BOX.get(), GeneratingBoxRenderer::new);
        event.registerBlockEntityRenderer(RSBlockEntities.END_CITY.get(), EndCityRenderer::new);
        event.registerBlockEntityRenderer(RSBlockEntities.TRAINER.get(), TrainerRenderer::new);
    }

    @SubscribeEvent
    static void registerSkullModels(EntityRenderersEvent.CreateSkullModels event) {
        EntityModelSet modelSet = event.getEntityModelSet();
        event.registerSkullModel(RSSkullTypes.SHULKER,new SkullModel(modelSet.bakeLayer(SHULKER_LAYER)));
    }

    @SubscribeEvent
    public static void registerLayersDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDef = meshDefinition.getRoot();
        partDef.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 52).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.ZERO);

        Supplier<LayerDefinition> shulkerHead = (()->LayerDefinition.create(meshDefinition, 64, 64));

        event.registerLayerDefinition(SHULKER_LAYER, shulkerHead);
    }
}
