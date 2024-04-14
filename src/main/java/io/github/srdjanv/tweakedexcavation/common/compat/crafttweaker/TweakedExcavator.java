package io.github.srdjanv.tweakedexcavation.common.compat.crafttweaker;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import io.github.srdjanv.tweakedexcavation.api.crafting.TweakedExcavatorHandler;
import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralMix;
import io.github.srdjanv.tweakedexcavation.util.MineralValidator;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
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
        MineralValidator.validateCTMineral(name, failChance, ores, chances, mineralWeight, powerTier, oreYield);
        ExcavatorHandler.MineralMix mineral = TweakedExcavatorHandler.addTweakedMineral(name, mineralWeight, failChance, ores, chances, powerTier);

        if (dimWhitelist != null && dimWhitelist.length > 0) mineral.dimensionWhitelist = dimWhitelist;
        if (dimBlacklist != null && dimBlacklist.length > 0) mineral.dimensionBlacklist = dimBlacklist;
        if (oreYield != 0) ((IMineralMix) mineral).setYield(oreYield);

        CraftTweakerAPI.logInfo("Adding MineralMix: '" + name + "' with weight " + mineralWeight);
    }

    @ZenMethod
    public static boolean removeTweakedMineral(String name) {
        var minInt = ExcavatorHandler.mineralList.keySet().iterator();
        boolean modified = false;

        while (minInt.hasNext()) {
            var min = minInt.next();
            if (name.equalsIgnoreCase(min.name)) {
                minInt.remove();
                modified = true;
            }
        }
        return modified;
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

        @ZenGetter("failChance")
        public double getFailChance() {
            return this.mix.getFailChance();
        }

        @ZenSetter("failChance")
        public void setFailChance(double chance) {
            mix.setFailChance((float) chance);
        }

        @ZenMethod
        public void addOre(String ore, double chance) {
            String[] newOres = new String[mix.getOres().length + 1];
            float[] newChances = new float[newOres.length];
            System.arraycopy(mix.getOres(), 0, newOres, 0, mix.getOres().length);
            System.arraycopy(mix.getChances(), 0, newChances, 0, mix.getChances().length);
            newOres[mix.getOres().length] = ore;
            newChances[mix.getOres().length] = (float)chance;
            this.mix.setOres(newOres);
            this.mix.setChances(newChances);
        }

        @ZenMethod
        public void removeOre(String ore) {
            Object2FloatMap<String> map = new Object2FloatOpenHashMap<>();

            int i;
            for(i = 0; i < mix.getOres().length; ++i)
                map.put(mix.getOres()[i], mix.getChances()[i]);

            map.remove(ore);
            mix.setOres(new String[map.size()]);
            mix.setChances(new float[map.size()]);
            i = 0;

            for (var e : map.object2FloatEntrySet()) {
                mix.getOres()[i] = e.getKey();
                mix.getChances()[i] = e.getFloatValue();
            }
        }
    }

}