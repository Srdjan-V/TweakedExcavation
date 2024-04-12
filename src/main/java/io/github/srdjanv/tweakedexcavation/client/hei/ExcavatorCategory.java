package io.github.srdjanv.tweakedexcavation.client.hei;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.item.ItemStack;
import io.github.srdjanv.tweakedexcavation.TweakedExcavation;
import io.github.srdjanv.tweakedlib.api.hei.BaseHEIUtil;

import java.util.List;

@SuppressWarnings("NullableProblems")
public class ExcavatorCategory implements IRecipeCategory<ExcavatorWrapper> {
    public static final String UID = TweakedExcavation.MODID + ".excavator";

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return BaseHEIUtil.translateToLocal("tile.immersiveengineering.metal_multiblock.excavator.name");
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
        IGuiItemStackGroup iGuiItemStackGroup = recipeLayout.getItemStacks();
        iGuiItemStackGroup.addTooltipCallback(recipeWrapper);
        List<ItemStack> list = recipeWrapper.getMinerals();

        int x = 8, y = 31;
        for (int i = 0; i < list.size(); i++, x += 21) {
            if (x == 155) {
                x = 8;
                y = 52;
            }

            iGuiItemStackGroup.init(i, false, x, y);
            iGuiItemStackGroup.set(i, list.get(i));

        }

    }

}

