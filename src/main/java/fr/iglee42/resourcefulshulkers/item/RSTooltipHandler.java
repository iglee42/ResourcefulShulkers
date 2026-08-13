package fr.iglee42.resourcefulshulkers.item;

import fr.iglee42.resourcefulshulkers.api.types.ITypeDefinition;
import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;


public final class RSTooltipHandler {

    public static final String TOOLTIP_PREFIX = "tooltip.resourcefulshulkers.";
    public static final String PRESS_SHIFT_KEY = TOOLTIP_PREFIX + "press_shift";
    public static final String TYPE_KEY = TOOLTIP_PREFIX + "type";
    public static final int MAX_TOOLTIP_LINES = 5;

    private static final Object[] NO_ARGS = new Object[0];

    private RSTooltipHandler() {}

    public static Builder tooltip(ItemStack stack, List<Component> tooltips) {
        return new Builder(stack, tooltips);
    }

    public static void addTooltip(ItemStack stack, List<Component> tooltips, boolean isShiftDown) {
        tooltip(stack, tooltips).shift(isShiftDown).apply();
    }

    public static List<Component> getTooltips(ItemStack stack, Map<Integer, Object[]> lineArgs) {
        List<Component> tooltips = new ArrayList<>();
        String descriptionId = stack.getItem().getDescriptionId();
        String[] parts = descriptionId.split("\\.");
        if (parts.length < 3) return tooltips;
        String base = "tooltip." + parts[1] + "." + parts[2];
        for (int i = 0; i < MAX_TOOLTIP_LINES; i++) {
            String key = base + (i == 0 ? "" : "." + i);
            if (!Language.getInstance().has(key)) break;
            tooltips.add(format(key, lineArgs.getOrDefault(i, NO_ARGS)));
        }
        return tooltips;
    }

    public static Component formatted(String key, Object... args) {
        return format(key.startsWith("tooltip.") ? key : TOOLTIP_PREFIX + key, args);
    }

    private static Component format(String key, Object... args) {
        return format(Component.translatable(key, args));
    }

    public static Component format(Component component) {
        MutableComponent output = Component.empty().withStyle(ChatFormatting.DARK_PURPLE);
        boolean currentlyHighlighted = false;
        String text = component.getString();
        for (String chunk : text.split("_", -1)) {
            output.append(Component.literal(chunk).withStyle(currentlyHighlighted ? ChatFormatting.LIGHT_PURPLE : ChatFormatting.DARK_PURPLE));
            currentlyHighlighted = !currentlyHighlighted;
        }
        return output;
    }

    public static final class Builder {

        private final ItemStack stack;
        private final List<Component> target;
        private final List<Component> headers = new ArrayList<>();
        private final Map<Integer, Object[]> lineArgs = new HashMap<>();
        private final NavigableMap<Integer, List<Component>> inserts = new TreeMap<>();
        private final List<Component> trailing = new ArrayList<>();
        private boolean shift = true;
        private boolean pressShiftHint = false;

        private Builder(ItemStack stack, List<Component> target) {
            this.stack = stack;
            this.target = target;
        }


        public Builder shift(boolean shift) {
            this.shift = shift;
            this.pressShiftHint = true;
            return this;
        }


        public Builder header(Component... lines) {
            headers.addAll(List.of(lines));
            return this;
        }


        public Builder type(ITypeDefinition type) {
            return header(Component.translatable(TYPE_KEY, type.name()));
        }

        public Builder args(int line, Object... args) {
            lineArgs.put(line, args);
            return this;
        }

        public Builder before(int line, Component... lines) {
            return insert(line, lines);
        }


        public Builder before(int line, String key, Object... args) {
            return insert(line, formatted(key, args));
        }

        public Builder after(int line, Component... lines) {
            return insert(line + 1, lines);
        }

        public Builder after(int line, String key, Object... args) {
            return insert(line + 1, formatted(key, args));
        }

        public Builder append(Component... lines) {
            trailing.addAll(List.of(lines));
            return this;
        }

        public Builder append(String key, Object... args) {
            return append(formatted(key, args));
        }

        public Builder appendAll(String... keys) {
            for (String key : keys) append(key);
            return this;
        }

        public List<Component> build() {
            List<Component> output = new ArrayList<>(headers.stream().map(h->h.copy().withStyle(ChatFormatting.GRAY)).toList());
            if (!shift) {
                if (pressShiftHint) output.add(format(PRESS_SHIFT_KEY));
                return output;
            }
            NavigableMap<Integer, List<Component>> remaining = new TreeMap<>(inserts);
            List<Component> lines = getTooltips(stack, lineArgs);
            for (int i = 0; i < lines.size(); i++) {
                List<Component> inserted = remaining.remove(i);
                if (inserted != null) output.addAll(inserted);
                output.add(lines.get(i));
            }
            remaining.values().forEach(output::addAll);
            output.addAll(trailing);
            return output;
        }


        public void apply() {
            target.addAll(build());
        }

        private Builder insert(int position, Component... lines) {
            inserts.computeIfAbsent(Math.max(position, 0), i -> new ArrayList<>()).addAll(List.of(lines));
            return this;
        }
    }
}
