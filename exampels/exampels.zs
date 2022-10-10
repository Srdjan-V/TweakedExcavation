import mods.TweakedLib.TweakedPowerTier;
import mods.TweakedExcavation.TweakedExcavator;
import mods.TweakedExcavation.TweakedMineral;

/*
    TweakedExcavator.addMineral(String name, int mineralWeight, float failChance, String[] ores, float[] chances, int powerTier,
                                @Optional int[] dimBlacklist, @Optional int[] dimWhitelist)

    ores Syntax:
    "OreDict Item" - this will used the default IE method of searching
    "ModID:OreDict Item" - This will search for a oredict item/block of a specific mod
    "ModID:Mod Item" - this will search for items/blocks from a mod
    "ModID:Mod Item:Metadata" this will search for items/blocks from a mod with metadata

    "minecraft:oreIron" - will get minecraft's oredict version of iron
    "minecraft:woolYellow" - will get minecraft's oredict version of yellow wool
    "minecraft:wool:14" - will get minecraft's wool with metadata 13 (red wool)
*/

TweakedExcavator.addMineral("Test1", 2000000, 0.0,["minecraft:oreIron", "minecraft:wool:14", "immersiveengineering:connector:0"] ,[1.0,1.0,1.0], 1);

/*
TweakedExcavator.addMineralWithCustomYield(String name, int mineralWeight, float failChance, String[] ores, float[] chances, int powerTier, int oreYield,
                                           @Optional int[] dimBlacklist, @Optional int[] dimWhitelist)

    oreYield - is the size of the mineral vein

*/

TweakedExcavator.addMineralWithCustomYield("Test2", 2000000, 0.0,["minecraft:oreIron", "minecraft:woolYellow", "immersiveengineering:connector:0"] ,[1.0,1.0,1.0], 1, 1000000);

/*
mods.TweakedExcavation.TweakedMineral is used for modifying default IE minerals

        available fields:
        -powerTier
        -yield

        A TweakedMineral object can be obtained with this method:
        TweakedExcavator.getTweakedMineral(String name);
*/



var mineral1 = TweakedExcavator.getTweakedMineral("Test1");
mineral1.yield = 180;

/*
 By default TweakedLib is registering a powerTier(default id: 1) if it detects that IE is loaded(and if its configuration is enabled see the configs for TweakedLib)
*/

TweakedPowerTier.registerPowerTier(60, 160000, 10240);
var mineral2 = TweakedExcavator.getTweakedMineral("Test2");
mineral1.powerTier = 60;
