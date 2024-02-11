package fr.iglee42.techresourcesshulker.utils;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.SkullBlock;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.Supplier;

public enum SkullTypes implements SkullBlock.Type, StringRepresentable {

    SHULKER(EntityType.SHULKER.delegate)
    ;

    private final Supplier<EntityType<?>> type;

    SkullTypes(Supplier<EntityType<?>> type) {
        this.type = type;
    }

    public EntityType<?> getType() {
        return type.get();
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }


    @Nullable
    public static SkullTypes fromEntityType(EntityType<?> type) {
        for (SkullTypes headType : values()) {
            if (headType.getType() == type) {
                return headType;
            }
        }
        return null;
    }
}
