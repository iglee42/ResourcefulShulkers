package fr.iglee42.resourcefulshulkers.entity.shulkers;

import fr.iglee42.resourcefulshulkers.api.shulkers.IShulkerDefinition;
import fr.iglee42.resourcefulshulkers.api.types.ITypeDefinition;
import fr.iglee42.resourcefulshulkers.blocks.entites.PurpurTargetBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Objects;

public class TypeShulker extends CustomShulker{

    private final ITypeDefinition type;

    public TypeShulker(EntityType<? extends TypeShulker> type, Level level, ITypeDefinition typeDef) {
        super(type, level);
        this.type = typeDef;
    }

    public TypeShulker(Level level, ITypeDefinition type) {
        this(type.registration().entityType().get(), level,type);
    }

    public ITypeDefinition definition() {
        return type;
    }


    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!player.isCrouching()) return InteractionResult.PASS;
        if (!player.getMainHandItem().isEmpty()) return InteractionResult.PASS;
        if (definition().registration().shulkerItem() == null) return InteractionResult.PASS;
        ItemStack stack = definition().registration().shulkerItem().get().getDefaultInstance();
        if (!player.getInventory().add(stack))
            player.drop(stack, false);
        remove(RemovalReason.KILLED);
        return InteractionResult.SUCCESS;
    }

}
