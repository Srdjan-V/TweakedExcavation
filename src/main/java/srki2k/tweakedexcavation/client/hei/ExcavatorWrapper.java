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

    private int getStringWidth() {
        return Math.min(118, Minecraft.getMinecraft().fontRenderer.getStringWidth(mineralMix.name) + 8);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        if (mouseY > 6 && mouseX > 6 && mouseY < 17 && mouseX < getStringWidth()) {
            List<String> list = new ArrayList<>();
            list.add(BaseHEIUtil.formatString(mineralMix.name));

            return list;
        }

        if (mouseY > 8 && mouseY < 22) {
            if (mouseX > 124 && mouseX < 135) {
                List<String> list = new ArrayList<>();
                BaseHEIUtil.powerTierListData(list, ((IMineralMix) mineralMix).getPowerTier());

                return list;
            }

            if (mouseX > 138 && mouseX < 151) {
                List<String> list = new ArrayList<>();
                BaseHEIUtil.dimensionListData(list, mineralMix.dimensionWhitelist, mineralMix.dimensionBlacklist);

                return list;
            }

        }

        return null;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (getStringWidth() > 115) {
            minecraft.fontRenderer.drawString(minecraft.fontRenderer.trimStringToWidth(mineralMix.name, 109).concat("..."), 8, 9, 15658734);
            return;
        }
        minecraft.fontRenderer.drawString(mineralMix.name, 8, 9, 15658734);
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
        tooltip.clear();
        tooltip.add(ingredient.getDisplayName());
        tooltip.add(BaseHEIUtil.translateToLocalFormatted("tweakedexcavation.jei.chance", BaseHEIUtil.percentFormat.format(mineralMix.recalculatedChances[slotIndex] * 100) + "%"));
    }

}
