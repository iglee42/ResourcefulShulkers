package fr.iglee42.resourcefulshulkers.jei.recipes;

import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Unmodifiable;

public interface IJeiInputOutputRecipe {

    @Unmodifiable
    Ingredient getInput();

    @Unmodifiable
    Ingredient getOutput();



}