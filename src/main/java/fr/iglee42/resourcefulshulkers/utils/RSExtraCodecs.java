// Some methods are extracted from the newer version of DataFixerUpper
// See https://github.com/Mojang/DataFixerUpper/blob/master/src/main/java/com/mojang/serialization/Codec.java

package fr.iglee42.resourcefulshulkers.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class RSExtraCodecs {

    public static final Codec<Integer> COLOR = withAlternative(
            Codec.INT,
            Codec.STRING.flatXmap(s->{
                try {
                    String formatted = s.replace("#", "");
                    return DataResult.success(Integer.parseInt(formatted, 16));
                } catch (NumberFormatException e) {
                    return DataResult.error(()->"Invalid color format: " + s,0);
                }
            }, i-> DataResult.success(String.format("#%06X", i)))
    );

    public static <A> Codec<A> withAlternative(Codec<A> primary, Codec<? extends A> alternative){
        return Codec.either(primary, alternative)
                .xmap(e->e.map(Function.identity(), Function.identity()), Either::left);
    }

    public static <A> Codec<A> validate(Codec<A> primary, final Function<A, DataResult<A>> checker){
        return primary.flatXmap(checker, checker);
    }


    public static <T> HolderSet<T> decodeHolderSet(
            FriendlyByteBuf buf,
            Registry<T> registry
    ) {
        int size = buf.readVarInt() - 1;

        if (size == -1) {
            ResourceLocation location = buf.readResourceLocation();
            TagKey<T> tagKey = TagKey.create(registry.key(), location);

            return registry.getTag(tagKey).orElseThrow();
        }

        List<Holder<T>> holders = new ArrayList<>(Math.min(size, 65536));

        for (int i = 0; i < size; i++) {
            T value = buf.readById(registry);
            if (value == null) continue;
            holders.add(Holder.direct(value));
        }

        return HolderSet.direct(holders);
    }

    public static <T> HolderSet<T> decodeHolderSet(
            FriendlyByteBuf buf,
            IForgeRegistry<T> registry
    ) {
        int size = buf.readVarInt() - 1;

        if (size == -1) {
            ResourceLocation location = buf.readResourceLocation();
            TagKey<T> tagKey = TagKey.create(registry.getRegistryKey(), location);

            return HolderSet.direct(registry.tags().getTag(tagKey).stream().map(Holder::direct).toList());
        }

        List<Holder<T>> holders = new ArrayList<>(Math.min(size, 65536));

        for (int i = 0; i < size; i++) {
            T value = buf.readRegistryIdUnsafe(registry);
            if (value == null) continue;
            holders.add(Holder.direct(value));
        }

        return HolderSet.direct(holders);
    }


    public static <T> void encodeHolderSet(
            FriendlyByteBuf buf,
            HolderSet<T> holderSet,
            Registry<T> registry
    ) {
        Optional<TagKey<T>> tag = holderSet.unwrapKey();

        // HolderSet basé sur un tag
        if (tag.isPresent()) {
            buf.writeVarInt(0);
            buf.writeResourceLocation(tag.get().location());
            return;
        }

        buf.writeVarInt(holderSet.size() + 1);

        for (Holder<T> holder : holderSet) {
            buf.writeId(registry, holder.value());
        }
    }


    public static <T> void encodeHolderSet(
            FriendlyByteBuf buf,
            HolderSet<T> holderSet,
            IForgeRegistry<T> registry
    ) {
        Optional<TagKey<T>> tag = holderSet.unwrapKey();

        // HolderSet basé sur un tag
        if (tag.isPresent()) {
            buf.writeVarInt(0);
            buf.writeResourceLocation(tag.get().location());
            return;
        }

        buf.writeVarInt(holderSet.size() + 1);

        for (Holder<T> holder : holderSet) {
            buf.writeRegistryIdUnsafe(registry, holder.value());
        }
    }

    public static List<String> readHolderSetValues(JsonElement elt){
        if (elt.isJsonPrimitive() && elt.getAsJsonPrimitive().isString()){
            return new ArrayList<>(List.of(elt.getAsString()));
        }
        if (!elt.isJsonArray()) throw new JsonParseException("Not a json array or string : " + elt);
        JsonArray array = elt.getAsJsonArray();
        List<String> values = new ArrayList<>();
        array.forEach(e -> values.add(e.getAsString()));
        return values;
    }

    public static <T> HolderSet<T> resolveHolderSet(IForgeRegistry<T> registry, List<String> values, HolderLookup.Provider registries){
        if (values.size() == 1){
            return parseHolderSet(registry, new JsonPrimitive(values.get(0)),registries);
        }
        JsonArray array = new JsonArray();
        values.forEach(array::add);
        return parseHolderSet(registry, array, registries);
    }

    private static <T> HolderSet<T> parseHolderSet(IForgeRegistry<T> registry, JsonElement elt, HolderLookup.Provider registries){

        if (!(elt.isJsonPrimitive() && elt.getAsJsonPrimitive().isString()) && !elt.isJsonArray()) throw new JsonParseException("Invalid element : " + elt);

        if (elt.isJsonPrimitive() && elt.getAsJsonPrimitive().isString()){
            String value = elt.getAsString();
            if (!value.startsWith("#")) {
                ResourceLocation id = ResourceLocation.parse(value);
                if (!registry.containsKey(id)) throw new JsonParseException("Invalid ID : " + elt);
                return HolderSet.direct(Holder.direct(registry.getValue(id)));
            }
        }
        return RegistryCodecs.homogeneousList(registry.getRegistryKey()).parse(RegistryOps.create(JsonOps.INSTANCE, registries), elt).resultOrPartial(msg -> {
                throw new JsonParseException(msg);
            }).orElseThrow();
    }

}
