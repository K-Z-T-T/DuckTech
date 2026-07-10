package org.quiltmc.users.duckteam.DuckTech.recipe.custom.advanceshredder;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import org.quiltmc.users.duckteam.DuckTech.api.recipes.InputOutputRecipeSerializer;

public class AdvanceShredderRecipeSerializer extends InputOutputRecipeSerializer<AdvanceShredderRecipe> {
    public static final AdvanceShredderRecipeSerializer INSTANCE = new AdvanceShredderRecipeSerializer();

    public AdvanceShredderRecipeSerializer() {
        super(data -> new AdvanceShredderRecipe(data.inputs, data.outputs, data.id, data.processingTime), 2, 3);
    }

    //如果需要自定义处理时间默认值，可以重写该方法
    @Override
    protected int readProcessingTimeFromJson(JsonObject jsonObject) {
        return GsonHelper.getAsInt(jsonObject, "processingTime", 20);
    }
}

