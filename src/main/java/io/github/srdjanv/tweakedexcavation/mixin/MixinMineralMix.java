package io.github.srdjanv.tweakedexcavation.mixin;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import io.github.srdjanv.tweakedexcavation.common.CustomMineralBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.HashMap;

@Mixin(value = ExcavatorHandler.MineralMix.class, remap = false)
public class MixinMineralMix {

    @Shadow
    public String name;
    @Shadow
    public String[] ores;
    @Shadow
    public float[] chances;
    @Shadow
    public NonNullList<ItemStack> oreOutput;
    @Shadow
    public float[] recalculatedChances;
    @Shadow
    boolean isValid = false;
    @Shadow
    public HashMap<String, String> replacementOres;

    @Shadow
    public int[] dimensionWhitelist;

    @Shadow
    public int[] dimensionBlacklist;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @author Srki_2K
     * @reason Added the ability to extract custom blocks
     */
    @Overwrite
    public void recalculateChances() {
        double chanceSum = 0.0;
        NonNullList<ItemStack> existing = NonNullList.create();
        ArrayList<Double> reChances = new ArrayList<>();

        int i;
        for (i = 0; i < this.ores.length; ++i) {
            String ore = this.ores[i];
            if (this.replacementOres != null && !ApiUtils.isExistingOreName(ore) && this.replacementOres.containsKey(ore)) {
                ore = this.replacementOres.get(ore);
            }

            if (ore != null && !ore.isEmpty()) {
                ItemStack preferredOre = ItemStack.EMPTY;

                if (ApiUtils.isExistingOreName(ore)) {
                    preferredOre = IEApi.getPreferredOreStack(ore);
                } else if (CustomMineralBlocks.getInstance().searchBlock(ore)) {
                    preferredOre = CustomMineralBlocks.getInstance().getBlocksFromCache(ore);
                }

                if (!preferredOre.isEmpty()) {
                    existing.add(preferredOre);
                    reChances.add((double) this.chances[i]);
                    chanceSum += this.chances[i];
                }
            }
        }

        this.isValid = existing.size() > 0;
        this.oreOutput = existing;
        this.recalculatedChances = new float[reChances.size()];

        for (i = 0; i < reChances.size(); ++i) {
            this.recalculatedChances[i] = (float) (reChances.get(i) / chanceSum);
        }

    }

    /**
     * @author Srki_2K
     * @reason Added the ability to use a white list and a black list
     */
    @Overwrite
    public boolean validDimension(int dim) {

        if (dimensionBlacklist != null && dimensionBlacklist.length > 0) {
            for (int black : dimensionBlacklist) {
                if (dim == black) {
                    return false;
                }
            }
        }

        if (dimensionWhitelist != null && dimensionWhitelist.length > 0) {
            for (int white : dimensionWhitelist) {
                if (dim == white) {
                    return true;
                }
            }
            return false;
        }

        return true;
    }

}
