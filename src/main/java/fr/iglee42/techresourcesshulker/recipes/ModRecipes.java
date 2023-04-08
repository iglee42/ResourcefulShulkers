package fr.iglee42.techresourcesshulker.recipes;

import fr.iglee42.techresourcesshulker.TechResourcesShulker;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TechResourcesShulker.MODID);


    public static RegistryObject<RecipeSerializer<ShulkerEnvironnementRecipe>> SHULKER_ENVIRONNEMENT_INFUSE_SERIALIZER = SERIALIZER.register("shulker_environnement_infuse", ShulkerEnvironnementRecipe.Serializer::new);


}
