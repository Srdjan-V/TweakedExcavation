package srki2k.tweakedexcavation.common.compat.crafttweaker;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import srki2k.tweakedexcavation.api.crafting.TweakedExcavatorHandler;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.TweakedExcavation.TweakedExcavator")
@ZenRegister
public class TweakedExcavator {

    @ZenMethod
    public static void addMineral(String name, int mineralWeight, double failChance, String[] ores, double[] chances, @Optional int[] dimension, @Optional boolean blacklist , int powerTier) {
        float[] fChances = new float[chances.length];
        for (int i = 0; i < chances.length; i++) {
            fChances[i] = (float) chances[i];
        }


        ExcavatorHandler.MineralMix mineral = TweakedExcavatorHandler.addTweakedMineral(name, mineralWeight, (float) failChance, ores, fChances, powerTier);
        if (dimension != null)
            if (blacklist) {
                mineral.dimensionBlacklist = dimension;
            } else {
                mineral.dimensionWhitelist = dimension;
            }

        CraftTweakerAPI.logInfo("Adding MineralMix: "+name+" with weight "+mineralWeight);
    }

}