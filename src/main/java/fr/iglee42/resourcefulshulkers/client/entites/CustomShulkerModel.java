package fr.iglee42.resourcefulshulkers.client.entites;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Shulker;

public class CustomShulkerModel<T extends Shulker> extends ShulkerModel<T> {
    public CustomShulkerModel(ModelPart parent) {
        super(parent);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer consumer, int light, int overlay, int color) {
        super.renderToBuffer(stack, consumer, light, overlay, color);
    }
}
