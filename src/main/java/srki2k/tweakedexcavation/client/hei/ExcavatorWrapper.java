package srki2k.tweakedexcavation.client.hei;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;

import java.util.List;

public class ExcavatorWrapper implements IRecipeWrapper {
    private final ExcavatorHandler.MineralMix mineralMix;

    public ExcavatorWrapper(ExcavatorHandler.MineralMix mineralMix) {
        this.mineralMix = mineralMix;
    }


    @Override
    public void getIngredients(IIngredients ingredients) {
        //ingredients.setOutputs(VanillaTypes.ITEM, Lists.newArrayList());
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return null;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }

}
