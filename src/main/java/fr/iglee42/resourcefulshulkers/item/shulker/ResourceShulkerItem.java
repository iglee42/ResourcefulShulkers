package fr.iglee42.resourcefulshulkers.item.shulker;

import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.item.RSTooltipHandler;
import fr.iglee42.resourcefulshulkers.types.TypesManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ResourceShulkerItem extends ShulkerItem{

    private final IShulkerDefinition definition;

    public ResourceShulkerItem(Properties props, IShulkerDefinition definition) {
        super(props);
        this.definition = definition;
    }

    @Override
    public @NotNull EntityType<? extends Shulker> entityType() {
        if (definition.registration().entityType() == null) {
            throw new IllegalStateException("Entity type is not registered for shulker definition: " + definition.id());
        }
        return definition.registration().entityType().get();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> tooltips, TooltipFlag p_41424_) {
        if (definition != null) {
            RSTooltipHandler.tooltip(stack, tooltips).type(definition.type()).apply();
        }
        super.appendHoverText(stack, p_41422_, tooltips, p_41424_);
    }

    public IShulkerDefinition definition() {
        return definition;
    }
}
