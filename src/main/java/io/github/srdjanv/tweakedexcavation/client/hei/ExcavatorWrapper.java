package io.github.srdjanv.tweakedexcavation.client.hei;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import io.github.srdjanv.tweakedexcavation.api.ihelpers.IMineralMix;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import io.github.srdjanv.tweakedlib.api.hei.BaseHEIUtil;

import java.util.ArrayList;
import java.util.List;

import static blusunrize.immersiveengineering.api.tool.ExcavatorHandler.mineralList;

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

                list.add(BaseHEIUtil.translateToLocalFormatted("tweakedexcavation.jei.mineral.weight", mineralList.get(mineralMix)));
                list.add("");

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
            minecraft.fontRenderer.drawString(minecraft.fontRenderer.trimStringToWidth(
                    BaseHEIUtil.formatString(mineralMix.name), 109).concat("..."), 8, 9, 15658734);
            return;
        }
        minecraft.fontRenderer.drawString(mineralMix.name, 8, 9, 15658734);
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
        tooltip.clear();
        tooltip.add(ingredient.getDisplayName());
        tooltip.add(BaseHEIUtil.translateToLocalFormatted("tweakedexcavation.jei.mineral.chance",
                BaseHEIUtil.percentFormat.format(mineralMix.recalculatedChances[slotIndex] * 100) + "%"));
        depletion(tooltip, slotIndex);
    }

    private void depletion(List<String> tooltip, int slotIndex) {
        if (((IMineralMix) mineralMix).getYield() < 0) {
            tooltip.add(BaseHEIUtil.translateToLocal("tweakedexcavation.jei.mineral.average.Infinite"));
            return;
        }
        tooltip.add(BaseHEIUtil.translateToLocalFormatted("tweakedexcavation.jei.mineral.average",
                BaseHEIUtil.numberFormat.format((int) (((IMineralMix) mineralMix).getYield() * mineralMix.recalculatedChances[slotIndex]))));
    }

}
