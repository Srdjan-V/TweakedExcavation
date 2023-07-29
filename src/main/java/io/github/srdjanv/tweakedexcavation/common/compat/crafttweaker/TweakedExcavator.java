package io.github.srdjanv.tweakedexcavation.common.compat.crafttweaker;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import io.github.srdjanv.tweakedexcavation.api.crafting.TweakedExcavatorHandler;
import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralMix;
import stanhebben.zenscript.annotations.*;

@SuppressWarnings("unused")
@ZenClass("mods.TweakedExcavation.TweakedExcavator")
@ZenRegister
public class TweakedExcavator {

    @ZenMethod
    public static void addMineral(String name, int mineralWeight, float failChance, String[] ores, float[] chances, int powerTier,
                                  @Optional int[] dimBlacklist, @Optional int[] dimWhitelist) {
        commonMineral(name, mineralWeight, failChance, ores, chances, powerTier, 0, dimBlacklist, dimWhitelist);
    }

    @ZenMethod
    public static void addMineralWithCustomYield(String name, int mineralWeight, float failChance, String[] ores, float[] chances, int powerTier, int oreYield,
                                                 @Optional int[] dimBlacklist, @Optional int[] dimWhitelist) {
        commonMineral(name, mineralWeight, failChance, ores, chances, powerTier, oreYield, dimBlacklist, dimWhitelist);
    }


    private static void commonMineral(String name, int mineralWeight, float failChance, String[] ores, float[] chances, int powerTier, int oreYield,
                                      int[] dimBlacklist, int[] dimWhitelist) {

        if (name.isEmpty()) {
            CraftTweakerAPI.logError("Reservoir name can not be a empty string!");
        }
        if (ores.length != chances.length) {
            CraftTweakerAPI.logError("Mineral(" + name + ") is missing ores or chances in the Arrays");
        }
        if (ores.length > 14) {
            CraftTweakerAPI.logError("Mineral(" + name + ") Should not have more then 14 ores");
        }

        ExcavatorHandler.MineralMix mineral = TweakedExcavatorHandler.addTweakedMineral(name, mineralWeight, failChance, ores, chances, powerTier);

        if (dimWhitelist != null && dimWhitelist.length > 0) {
            mineral.dimensionWhitelist = dimWhitelist;
        }
        if (dimBlacklist != null && dimBlacklist.length > 0) {
            mineral.dimensionBlacklist = dimBlacklist;
        }
        if (oreYield != 0) {
            ((IMineralMix) mineral).setYield(oreYield);
        }

        CraftTweakerAPI.logInfo("Adding MineralMix: " + name + " with weight " + mineralWeight);

    }

    @ZenMethod
    public static TweakedMineral getTweakedMineral(String name) {
        for (ExcavatorHandler.MineralMix mix : ExcavatorHandler.mineralList.keySet()) {
            if (mix.name.equalsIgnoreCase(name)) {
                return new TweakedMineral(mix);
            }
        }
        CraftTweakerAPI.logError("No Mineral with name: " + name + " was found");
        return null;
    }

    @ZenClass("mods.TweakedExcavation.TweakedMineral")
    @ZenRegister
    public static class TweakedMineral {
        IMineralMix mix;

        public TweakedMineral(ExcavatorHandler.MineralMix mix) {
            this.mix = (IMineralMix) mix;
        }

        @ZenGetter("powerTier")
        public int getPowerTier() {
            return mix.getPowerTier();
        }

        @ZenSetter("powerTier")
        public void setPowerTier(int id) {
            mix.setPowerTier(id);
        }

        @ZenGetter("yield")
        public int getYield() {
            return mix.getYield();
        }

        @ZenSetter("yield")
        public void setYield(int yield) {
            mix.setYield(yield);
        }

    }

}