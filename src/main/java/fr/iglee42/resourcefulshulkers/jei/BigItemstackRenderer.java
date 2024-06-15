/*package fr.iglee42.resourcefulshulkers.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BigItemstackRenderer implements IIngredientRenderer<ItemStack> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void render(PoseStack poseStack, @Nullable ItemStack ingredient) {
        if (ingredient != null) {
            PoseStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushPose();
            {
                modelViewStack.mulPoseMatrix(poseStack.last().pose());

                RenderSystem.enableDepthTest();

                Minecraft minecraft = Minecraft.getInstance();
                Font font = getFontRenderer(minecraft, ingredient);
                ItemRenderer itemRenderer = minecraft.getItemRenderer();
                modelViewStack.scale(1.5f,1.5f,1.0f);
                itemRenderer.renderAndDecorateFakeItem(ingredient, 0, 0);
                itemRenderer.renderGuiItemDecorations(font, ingredient, 0, 0);
                RenderSystem.disableBlend();
            }
            modelViewStack.popPose();
            // Restore model-view matrix now that the item has been rendered
            RenderSystem.applyModelViewMatrix();
        }
    }

    @SuppressWarnings("removal")
    @Override
    public void render(PoseStack stack, int xPosition, int yPosition, @Nullable ItemStack ingredient) {
        stack.pushPose();
        {
            stack.translate(xPosition, yPosition, 0);
            render(stack, ingredient);
        }
        stack.popPose();
    }

    @Override
    public List<Component> getTooltip(ItemStack ingredient, TooltipFlag tooltipFlag) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        try {
            return ingredient.getTooltipLines(player, tooltipFlag);
        } catch (RuntimeException | LinkageError e) {
            String itemStackInfo = getItemStackInfo(ingredient);
            LOGGER.error("Failed to get tooltip: {}", itemStackInfo, e);
            List<Component> list = new ArrayList<>();
            TranslatableComponent crash = new TranslatableComponent("jei.tooltip.error.crash");
            list.add(crash.withStyle(ChatFormatting.RED));
            return list;
        }
    }

    @Override
    public Font getFontRenderer(Minecraft minecraft, ItemStack ingredient) {
        return Minecraft.getInstance().font;
    }

    @SuppressWarnings("ConstantConditions")
    public static String getItemStackInfo(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return "null";
        }
        Item item = itemStack.getItem();
        final String itemName;
        IForgeRegistry<Item> itemRegistry = ForgeRegistries.ITEMS;
        ResourceLocation registryName = itemRegistry.getKey(item);
        if (registryName != null) {
            itemName = registryName.toString();
        } else if (item instanceof BlockItem) {
            final String blockName;
            Block block = ((BlockItem) item).getBlock();
            if (block == null) {
                blockName = "null";
            } else {
                IForgeRegistry<Block> blockRegistry =ForgeRegistries.BLOCKS;
                ResourceLocation blockRegistryName = blockRegistry.getKey(block);
                if (blockRegistryName != null) {
                    blockName = blockRegistryName.toString();
                } else {
                    blockName = block.getClass().getName();
                }
            }
            itemName = "BlockItem(" + blockName + ")";
        } else {
            itemName = item.getClass().getName();
        }

        CompoundTag nbt = itemStack.getTag();
        if (nbt != null) {
            return itemStack + " " + itemName + " nbt:" + nbt;
        }
        return itemStack + " " + itemName;
    }

    @Override
    public int getWidth() {
        return 24;
    }

    @Override
    public int getHeight() {
        return 24;
    }
}*/
