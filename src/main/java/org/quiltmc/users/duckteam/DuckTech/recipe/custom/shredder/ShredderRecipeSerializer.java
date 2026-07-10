package org.quiltmc.users.duckteam.DuckTech.recipe.custom.shredder;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import org.quiltmc.users.duckteam.DuckTech.api.recipes.InputOutputRecipeSerializer;

public class ShredderRecipeSerializer extends InputOutputRecipeSerializer<ShredderRecipe> {
    public static final ShredderRecipeSerializer INSTANCE = new ShredderRecipeSerializer();

    public ShredderRecipeSerializer() {
        super(data -> new ShredderRecipe(data.inputs, data.outputs, data.id), 2, 3);
    }

    //粉碎机配方不需要处理时间，使用默认值20
    @Override
    protected int readProcessingTimeFromJson(JsonObject jsonObject) {
        return 20;
    }

    @Override
    protected int readProcessingTimeFromNetwork(FriendlyByteBuf buffer) {
        return 20;
    }
}
