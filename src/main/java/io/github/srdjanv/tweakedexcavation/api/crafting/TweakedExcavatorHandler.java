package io.github.srdjanv.tweakedexcavation.api.crafting;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralMix;
import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralWorldInfo;
import io.github.srdjanv.tweakedlib.api.powertier.PowerTier;
import io.github.srdjanv.tweakedlib.api.powertier.PowerTierHandler;
import net.minecraft.world.World;

import static blusunrize.immersiveengineering.api.tool.ExcavatorHandler.mineralList;

public class TweakedExcavatorHandler {

    /**
     * Gets the PowerTier object associated with the mineral of a given chunk
     *
     * @param world  World whose ore chunk to drain
     * @param chunkX Chunk x
     * @param chunkZ Chunk z
     * @return Returns PowerTier
     */
    public static PowerTier getPowerTier(World world, int chunkX, int chunkZ) {
        IMineralWorldInfo info = (IMineralWorldInfo) ExcavatorHandler.getMineralWorldInfo(world, chunkX, chunkZ);

        if (info == null || info.getType() == null) {
            return PowerTierHandler.getFallbackPowerTier();
        }

        IMineralMix tweakedMineralMix = (IMineralMix) info.getType();
        return PowerTierHandler.getPowerTier(tweakedMineralMix.getPowerTier());
    }

    /**
     * Adds a tweaked MineralMix to the pool of valid mineral
     *
     * @param name          The name of the MineralMix
     * @param mineralWeight The weight for this Mineral to spawn
     * @param powerTier     The tier of power usage
     * @return The created MineralMix
     */
    public static ExcavatorHandler.MineralMix addTweakedMineral(String name, int mineralWeight, float failChance, String[] ores, float[] chances, int powerTier) {
        ExcavatorHandler.MineralMix mix = new ExcavatorHandler.MineralMix(name, failChance, ores, chances);
        mineralList.put(mix, mineralWeight);

        IMineralMix iMix = (IMineralMix) mix;
        iMix.setPowerTier(powerTier);

        return mix;
    }
}