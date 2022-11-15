package srki2k.tweakedexcavation.api.ihelpers;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IMineralMixGetters {

    String getName();

    float getFailChance();

    String[] getOres();

    float[] getChances();

    List<ItemStack> getOreOutput();

    float[] getRecalculatedChances();

    int[] getDimensionWhitelist();

    int[] getDimensionBlacklist();

    int getYield();

    int getPowerTier();

}
