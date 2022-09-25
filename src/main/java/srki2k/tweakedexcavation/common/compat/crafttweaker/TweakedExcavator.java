package srki2k.tweakedexcavation.common.compat.crafttweaker;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import srki2k.tweakedexcavation.api.crafting.TweakedExcavatorHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.TweakedExcavation.TweakedExcavator")
@ZenRegister
public class TweakedExcavator {

    @ZenMethod
    public static void addMineral(String name, int mineralWeight, float failChance, String[] ores, float[] chances, int powerTier, int[] dimBlacklist, int[] dimWhitelist) {


        if (name.isEmpty()) {
            CraftTweakerAPI.logError("Reservoir name can not be a empty string!");
        }
        if (ores.length != chances.length) {
            CraftTweakerAPI.logError("You are missing ores or chances in the Arrays");
        }

        ExcavatorHandler.MineralMix mineral = TweakedExcavatorHandler.addTweakedMineral(name, mineralWeight, failChance, ores, chances, powerTier);

        if (dimWhitelist.length > 0) {
            mineral.dimensionWhitelist = dimWhitelist;
        }

        if (dimBlacklist.length > 0) {
            mineral.dimensionBlacklist = dimBlacklist;
        }

        CraftTweakerAPI.logInfo("Adding MineralMix: " + name + " with weight " + mineralWeight);
    }

}