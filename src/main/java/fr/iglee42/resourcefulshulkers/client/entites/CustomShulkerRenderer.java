package fr.iglee42.resourcefulshulkers.client.entites;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;

import fr.iglee42.resourcefulshulkers.entity.TypeShulker;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.entity.CustomShulker;
import fr.iglee42.resourcefulshulkers.entity.ResourceShulker;
import fr.iglee42.resourcefulshulkers.utils.ShulkerType;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomShulkerRenderer extends MobRenderer<CustomShulker, CustomShulkerModel<CustomShulker>> {
   private static final ResourceLocation DEFAULT_TEXTURE_LOCATION = new ResourceLocation("textures/" + Sheets.DEFAULT_SHULKER_TEXTURE_LOCATION.texture().getPath() + ".png");
   public static final ResourceLocation[] TEXTURE_LOCATION = Sheets.SHULKER_TEXTURE_LOCATION.stream().map((p_115919_) -> {
      return new ResourceLocation("textures/" + p_115919_.texture().getPath() + ".png");
   }).toArray((p_115877_) -> {
      return new ResourceLocation[p_115877_];
   });

   public CustomShulkerRenderer(EntityRendererProvider.Context p_174370_) {
      super(p_174370_, new CustomShulkerModel<>(p_174370_.bakeLayer(ModelLayers.SHULKER)), 0.0F);
      this.addLayer(new CustomShulkerHeadLayer(this));
   }

   public Vec3 getRenderOffset(CustomShulker p_115904_, float p_115905_) {
      return p_115904_.getRenderPosition(p_115905_).orElse(super.getRenderOffset(p_115904_, p_115905_));
   }

   public boolean shouldRender(CustomShulker p_115913_, Frustum p_115914_, double p_115915_, double p_115916_, double p_115917_) {
      return super.shouldRender(p_115913_, p_115914_, p_115915_, p_115916_, p_115917_) ? true : p_115913_.getRenderPosition(0.0F).filter((p_174374_) -> {
         EntityType<?> entitytype = p_115913_.getType();
         float f = entitytype.getHeight() / 2.0F;
         float f1 = entitytype.getWidth() / 2.0F;
         Vec3 vec3 = Vec3.atBottomCenterOf(p_115913_.blockPosition());
         return p_115914_.isVisible((new AABB(p_174374_.x, p_174374_.y + (double)f, p_174374_.z, vec3.x, vec3.y + (double)f, vec3.z)).inflate((double)f1, (double)f, (double)f1));
      }).isPresent();
   }

   @Override
   public ResourceLocation getTextureLocation(CustomShulker shulker) {
      return getShulkerTexture(shulker);
   }

   public static ResourceLocation getShulkerTexture(CustomShulker shulker) {

      if (shulker.hasCustomName() && shulker.getCustomName().getString().equalsIgnoreCase("MLDEG")) return getTypeShulkerTexture("mldeg");
      if (shulker instanceof TypeShulker) return getTypeShulkerTexture(shulker.getTypeId().getPath());
      //if (shulker.getType() == ModEntities.OVERWORLD_SHULKER.get()) return getBaseShulkerTexture("overworld");
      //if (shulker.getType() == ModEntities.SKY_SHULKER.get()) return getBaseShulkerTexture("sky");
      //if (shulker.getType() == ModEntities.NETHER_SHULKER.get()) return getBaseShulkerTexture("nether");
      return shulker instanceof ResourceShulker ? getResourceTextureLocation(ShulkerType.getById(shulker.getTypeId())) : getTextureLocation(shulker.getColor());
   }

   private static ResourceLocation getTypeShulkerTexture(String essence){
      return new ResourceLocation(ResourcefulShulkers.MODID,"textures/entity/types/"+essence+".png");
   }

   public static ResourceLocation getResourceTextureLocation(ShulkerType r) {
      return new ResourceLocation(ResourcefulShulkers.MODID,"textures/"+r.getTexture().getPath());
   }
   public static ResourceLocation getTextureLocation(@Nullable DyeColor p_174376_) {
      return p_174376_ == null ? DEFAULT_TEXTURE_LOCATION : TEXTURE_LOCATION[p_174376_.getId()];
   }

   protected void setupRotations(CustomShulker p_115907_, PoseStack p_115908_, float p_115909_, float p_115910_, float p_115911_) {
      super.setupRotations(p_115907_, p_115908_, p_115909_, p_115910_ + 180.0F, p_115911_);
      p_115908_.translate(0.0D, 0.5D, 0.0D);
      p_115908_.mulPose(p_115907_.getAttachFace().getOpposite().getRotation());
      p_115908_.translate(0.0D, -0.5D, 0.0D);
   }
}