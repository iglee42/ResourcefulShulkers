package fr.iglee42.resourcefulshulkers.registries;

import fr.iglee42.igleelib.api.utils.ITickableRecipe;
import fr.iglee42.resourcefulshulkers.RSIds;
import fr.iglee42.resourcefulshulkers.blocks.entites.ShulkerInfuserBlockEntity;
import fr.iglee42.resourcefulshulkers.recipes.ShulkerInfuserInput;
import fr.iglee42.resourcefulshulkers.recipes.ItemInfusionRecipe;
import fr.iglee42.resourcefulshulkers.recipes.EnvironmentInfusionRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;


public class RSRecipes {

    public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(Registries.RECIPE_TYPE, RSIds.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, RSIds.MODID);

    public static RegistryObject<RecipeType<EnvironmentInfusionRecipe>> SHULKER_ENVIRONNEMENT_INFUSE = RECIPES.register(EnvironmentInfusionRecipe.Type.ID, ()-> EnvironmentInfusionRecipe.Type.INSTANCE);
    public static RegistryObject<RecipeType<ItemInfusionRecipe>> SHULKER_ITEM_INFUSE = RECIPES.register(ItemInfusionRecipe.Type.ID, ()-> ItemInfusionRecipe.Type.INSTANCE);


    public static RegistryObject<RecipeSerializer<EnvironmentInfusionRecipe>> SHULKER_ENVIRONNEMENT_INFUSE_SERIALIZER = SERIALIZER.register(EnvironmentInfusionRecipe.Type.ID, EnvironmentInfusionRecipe.Serializer::new);
    public static RegistryObject<RecipeSerializer<ItemInfusionRecipe>> SHULKER_ITEM_INFUSE_SERIALIZER = SERIALIZER.register(ItemInfusionRecipe.Type.ID, ItemInfusionRecipe.Serializer::new);


    public static <R extends Recipe<ShulkerInfuserInput> & ITickableRecipe<ShulkerInfuserBlockEntity>> Optional<ITickableRecipe<ShulkerInfuserBlockEntity>> findShulkerInfuserRecipe(Level level, ShulkerInfuserBlockEntity be){
        Optional<ItemInfusionRecipe> itemInfusionRecipe = level.getRecipeManager().getRecipeFor(ItemInfusionRecipe.Type.INSTANCE,
                new ShulkerInfuserInput(be), level);
        if (itemInfusionRecipe.isPresent()) return Optional.of((R) itemInfusionRecipe.get());
        Optional<EnvironmentInfusionRecipe> environmentRecipe = level.getRecipeManager()
                .getRecipeFor(EnvironmentInfusionRecipe.Type.INSTANCE, new ShulkerInfuserInput(be), level);
        return environmentRecipe.map(recipe -> (R) recipe);
    }
}
