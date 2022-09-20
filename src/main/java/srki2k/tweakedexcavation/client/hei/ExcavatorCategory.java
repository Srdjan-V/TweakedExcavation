package srki2k.tweakedexcavation.client.hei;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.util.Translator;
import srki2k.tweakedexcavation.TweakedExcavation;
import srki2k.tweakedlib.api.hei.BaseHEIUtil;

@SuppressWarnings("NullableProblems")
public class ExcavatorCategory implements IRecipeCategory<ExcavatorWrapper> {
    public static final String UID = TweakedExcavation.MODID + ".excavator";

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return Translator.translateToLocal("tile.immersiveengineering.metal_multiblock.metal_press.excavator.name");
    }

    @Override
    public String getModName() {
        return TweakedExcavation.MODID;
    }

    @Override
    public IDrawable getBackground() {
        return BaseHEIUtil.getExcavatorBackground();
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ExcavatorWrapper recipeWrapper, IIngredients ingredients) {

    }

}

