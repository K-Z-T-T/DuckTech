package org.quiltmc.users.duckteam.DuckTech.recipe.custom.injection_machine;

import org.quiltmc.users.duckteam.DuckTech.api.recipes.InputOutputRecipeSerializer;

public class InjectionMachineRecipeSerializer extends InputOutputRecipeSerializer<InjectionMachineRecipe> {
    public static final InjectionMachineRecipeSerializer INSTANCE = new InjectionMachineRecipeSerializer();

    public InjectionMachineRecipeSerializer() {
        super(data -> new InjectionMachineRecipe(data.inputs, data.outputs, data.id, data.processingTime), 2, 1);
    }

}
