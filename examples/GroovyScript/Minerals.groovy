//Register an power tier
var power = mods.tweakedMods.PowerTier.recipeBuilder()
        .capacity(5555555)
        .rft(5)
        .register()

//Register an mineral deposit
mods.tweakedMods.Excavator.recipeBuilder()
        .name("GroovyMineral")
        .weight(80000)
        .failChance(0.05)
        .powerTier(power)
        .ore("minecraft:oreGold", 0.5)
        .ores(["minecraft:oreIron": 1.0,
               "minecraft:wool:14": 1.0])
        .register()

//Remove the default IE iron mineral
mods.tweakedMods.Excavator.remove("iron")

//Modify the default IE iron mineral
var IEGoldMineral = mods.tweakedMods.Mineral.get("gold").toBuilder()
IEGoldMineral.oreYield(15000).weight(5500).register()

/*
All methods can be seen in 'io.github.srdjanv.tweakedexcavation.common.compat.grovyscript'

Also see https://cleanroommc.com/groovy-script/
*/


