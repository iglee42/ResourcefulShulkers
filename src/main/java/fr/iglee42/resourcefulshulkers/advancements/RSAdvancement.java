package fr.iglee42.resourcefulshulkers.advancements;

import fr.iglee42.resourcefulshulkers.RSIds;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class RSAdvancement {

    public static final String CODE_CRITERION = "shulker";

    private final ResourceLocation id;
    private final @Nullable RSAdvancement parent;
    private final Supplier<ItemStack> icon;
    private final String title;
    private final String description;
    private final @Nullable ResourceLocation background;
    private final AdvancementType frame;
    private final boolean showToast;
    private final boolean announceToChat;
    private final boolean hidden;
    private final Map<String, Criterion<?>> criteria;

    private RSAdvancement(Builder builder) {
        this.id = builder.id;
        this.parent = builder.parent;
        this.icon = builder.icon;
        this.title = builder.title;
        this.description = builder.description;
        this.background = builder.background;
        this.frame = builder.frame;
        this.showToast = builder.showToast;
        this.announceToChat = builder.announceToChat;
        this.hidden = builder.hidden;
        this.criteria = builder.criteria.isEmpty()
                ? Map.of(CODE_CRITERION, CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                : Map.copyOf(builder.criteria);
    }

    public static Builder builder(String path) {
        return new Builder(RSIds.id(path));
    }

    public @NotNull ResourceLocation id() {
        return id;
    }

    public @Nullable RSAdvancement parent() {
        return parent;
    }

    public @NotNull String title() {
        return title;
    }

    public @NotNull String description() {
        return description;
    }

    public @NotNull String titleKey() {
        return baseKey() + ".title";
    }

    public @NotNull String descriptionKey() {
        return baseKey() + ".description";
    }

    public void awardTo(@Nullable Player player) {
        if (player instanceof ServerPlayer serverPlayer) awardTo(serverPlayer);
    }

    public void awardTo(@NotNull ServerPlayer player) {
        AdvancementHolder holder = resolve(player.getServer());
        if (holder == null) return;
        PlayerAdvancements advancements = player.getAdvancements();
        AdvancementProgress progress = advancements.getOrStartProgress(holder);
        if (progress.isDone()) return;
        for (String criterion : progress.getRemainingCriteria()) {
            advancements.award(holder, criterion);
        }
    }

    public void revokeFrom(@NotNull ServerPlayer player) {
        AdvancementHolder holder = resolve(player.getServer());
        if (holder == null) return;
        PlayerAdvancements advancements = player.getAdvancements();
        for (String criterion : advancements.getOrStartProgress(holder).getCompletedCriteria()) {
            advancements.revoke(holder, criterion);
        }
    }

    public boolean isDone(@NotNull ServerPlayer player) {
        AdvancementHolder holder = resolve(player.getServer());
        return holder != null && player.getAdvancements().getOrStartProgress(holder).isDone();
    }

    public @Nullable AdvancementHolder resolve(@Nullable MinecraftServer server) {
        return server == null ? null : server.getAdvancements().get(id);
    }

    public @NotNull Advancement.Builder toVanillaBuilder(@Nullable AdvancementHolder parentHolder) {
        Advancement.Builder builder = Advancement.Builder.recipeAdvancement();
        if (parentHolder != null) builder.parent(parentHolder);
        builder.display(icon.get(),
                Component.translatable(titleKey()),
                Component.translatable(descriptionKey()),
                background,
                frame,
                showToast,
                announceToChat,
                hidden);
        criteria.forEach(builder::addCriterion);
        return builder;
    }

    private String baseKey() {
        return "advancements." + id.getNamespace() + "." + id.getPath().replace('/', '.');
    }

    public static final class Builder {

        private final ResourceLocation id;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
        private @Nullable RSAdvancement parent;
        private Supplier<ItemStack> icon = () -> new ItemStack(Items.BARRIER);
        private String title = "";
        private String description = "";
        private @Nullable ResourceLocation background;
        private AdvancementType frame = AdvancementType.TASK;
        private boolean showToast = true;
        private boolean announceToChat = true;
        private boolean hidden = false;

        private Builder(ResourceLocation id) {
            this.id = id;
        }

        public Builder parent(RSAdvancement parent) {
            this.parent = parent;
            return this;
        }

        public Builder icon(ItemLike item) {
            return iconStack(() -> new ItemStack(item));
        }

        public Builder icon(Supplier<? extends ItemLike> item) {
            return iconStack(() -> new ItemStack(item.get()));
        }

        public Builder icon(ResourceLocation itemId, Supplier<? extends ItemLike> fallback) {
            return iconStack(() -> {
                Item item = BuiltInRegistries.ITEM.get(itemId);
                return new ItemStack(item == Items.AIR ? fallback.get().asItem() : item);
            });
        }

        public Builder iconStack(Supplier<ItemStack> icon) {
            this.icon = icon;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder background(ResourceLocation background) {
            this.background = background;
            return this;
        }

        public Builder frame(AdvancementType frame) {
            this.frame = frame;
            return this;
        }

        public Builder toast(boolean showToast) {
            this.showToast = showToast;
            return this;
        }

        public Builder announce(boolean announceToChat) {
            this.announceToChat = announceToChat;
            return this;
        }

        public Builder hidden(boolean hidden) {
            this.hidden = hidden;
            return this;
        }

        public Builder criterion(String name, Criterion<?> criterion) {
            this.criteria.put(name, criterion);
            return this;
        }

        public RSAdvancement build() {
            return new RSAdvancement(this);
        }
    }
}
