package fr.iglee42.resourcefulshulkers.jei.recipes;

import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Unmodifiable;

public class InputOutputRecipe implements IJeiInputOutputRecipe {
    private final Ingredient input;
    private final Ingredient output;
    @Override
    public @Unmodifiable Ingredient getInput() {
        return input;
    }

    @Override
    public @Unmodifiable Ingredient getOutput() {
        return output;
    }

    public InputOutputRecipe(Ingredient input, Ingredient output) {
        this.input = input;
        this.output = output;
    }
}