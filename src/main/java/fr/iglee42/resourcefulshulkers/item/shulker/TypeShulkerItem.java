package fr.iglee42.resourcefulshulkers.item.shulker;

import fr.iglee42.resourcefulshulkers.api.types.ITypeDefinition;
import fr.iglee42.resourcefulshulkers.item.RSTooltipHandler;
import fr.iglee42.resourcefulshulkers.types.TypesManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TypeShulkerItem extends ShulkerItem{

    private final ITypeDefinition definition;

    public TypeShulkerItem(Properties props, ITypeDefinition definition) {
        super(props);
        if (!definition.createShulker()) throw new IllegalArgumentException("Cannot create a shulker item for a type that does not create shulkers: " + definition.id());
        this.definition = definition;
    }

    @Override
    public @NotNull EntityType<? extends Shulker> entityType() {
        if (definition.registration().entityType() == null)
            throw new IllegalStateException("Entity type supplier is not set for " + definition.id());
        return definition.registration().entityType().get();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext p_41422_, List<Component> tooltips, TooltipFlag p_41424_) {
        RSTooltipHandler.tooltip(stack, tooltips).type(TypesManager.getElementalType()).apply();
        super.appendHoverText(stack, p_41422_, tooltips, p_41424_);
    }
}
