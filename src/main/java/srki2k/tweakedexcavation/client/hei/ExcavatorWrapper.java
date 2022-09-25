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
import srki2k.tweakedlib.api.powertier.PowerTierHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcavatorWrapper implements IRecipeWrapper, ITooltipCallback<ItemStack> {
    private final ExcavatorHandler.MineralMix mineralMix;

    public ExcavatorWrapper(ExcavatorHandler.MineralMix mineralMix) {
        this.mineralMix = mineralMix;
    }


    // TODO: 25/09/2022 This is Temporary
    public ExcavatorHandler.MineralMix getM() {
        return mineralMix;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.ITEM, mineralMix.oreOutput);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        if (mouseX > 124 && mouseX < 135 && mouseY > 8 && mouseY < 22) {
            List<String> list = new ArrayList<>();
            list.add(String.valueOf(((IMineralMix) mineralMix).getPowerTier()));
            list.add(BaseHEIUtil.numberFormat.format(PowerTierHandler.getPowerTier(((IMineralMix) mineralMix).getPowerTier()).getCapacity()));
            list.add(BaseHEIUtil.numberFormat.format(PowerTierHandler.getPowerTier(((IMineralMix) mineralMix).getPowerTier()).getRft()));

            return list;
        }

        if (mouseX > 138 && mouseX < 151 && mouseY > 8 && mouseY < 22) {
            List<String> list = new ArrayList<>();
            list.add("dimensionWhitelist" + Arrays.toString(mineralMix.dimensionWhitelist));
            list.add("dimensionBlacklist" + Arrays.toString(mineralMix.dimensionBlacklist));

            return list;
        }

        return null;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawString(mineralMix.name, 8, 9 , 15658734);
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
        tooltip.clear();
        tooltip.add(ingredient.getDisplayName());
        tooltip.add("chances: " + BaseHEIUtil.percentFormat.format(mineralMix.recalculatedChances[slotIndex] * 100));
    }

}
