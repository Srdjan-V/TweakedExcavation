package io.github.srdjanv.tweakedexcavation.api.mixins;

public interface IMineralMixSetters {

    void setName(String name);

    void setFailChance(float failChance);

    void setOres(String[] ores);

    void setChances(float[] chances);

    void setRecalculatedChances(float[] recalculatedChances);

    void setDimensionWhitelist(int[] dimensionWhitelist);

    void setDimensionBlacklist(int[] dimensionBlacklist);

    void setYield(int yield);

    void setPowerTier(int powerTier);
}
