package srki2k.tweakedexcavation.client.hei;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import srki2k.tweakedexcavation.api.ihelpers.IMineralMix;
import srki2k.tweakedlib.api.hei.BaseHEIUtil;

import java.util.ArrayList;
import java.util.List;

public class ExcavatorWrapper implements IRecipeWrapper, ITooltipCallback<ItemStack> {
    private final ExcavatorHandler.MineralMix mineralMix;

    public ExcavatorWrapper(ExcavatorHandler.MineralMix mineralMix) {
        this.mineralMix = mineralMix;
    }


    public List<ItemStack> getMinerals() {
        return mineralMix.oreOutput;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.ITEM, mineralMix.oreOutput);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        if (mouseX > 6 && mouseX < 120 && mouseY > 6 && mouseY < 18) {
            List<String> list = new ArrayList<>();
            list.add(BaseHEIUtil.formatString(mineralMix.name));

            return list;
        }

        if (mouseX > 124 && mouseX < 135 && mouseY > 8 && mouseY < 22) {
            List<String> list = new ArrayList<>();
            BaseHEIUtil.powerTierListData(list, ((IMineralMix) mineralMix).getPowerTier());

            return list;
        }

        if (mouseX > 138 && mouseX < 151 && mouseY > 8 && mouseY < 22) {
            List<String> list = new ArrayList<>();
            BaseHEIUtil.dimensionListData(list, mineralMix.dimensionWhitelist, mineralMix.dimensionBlacklist);

            return list;
        }

        return null;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (mineralMix.name.length() > 17) {
            minecraft.fontRenderer.drawString(mineralMix.name.substring(0, 17).concat("..."), 8, 9, 15658734);
            return;
        }
        minecraft.fontRenderer.drawString(mineralMix.name, 8, 9, 15658734);
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
        tooltip.clear();
        tooltip.add(ingredient.getDisplayName());
        tooltip.add("chances: " + BaseHEIUtil.percentFormat.format(mineralMix.recalculatedChances[slotIndex] * 100));
    }

}
