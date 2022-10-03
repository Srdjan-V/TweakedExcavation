package srki2k.tweakedexcavation.api.crafting;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import net.minecraft.world.World;
import srki2k.tweakedexcavation.api.ihelpers.IMineralMix;
import srki2k.tweakedexcavation.api.ihelpers.IMineralWorldInfo;
import srki2k.tweakedlib.api.logging.errorlogginglib.ErrorLoggingLib;
import srki2k.tweakedlib.api.powertier.PowerTier;
import srki2k.tweakedlib.api.powertier.PowerTierHandler;

import static blusunrize.immersiveengineering.api.tool.ExcavatorHandler.*;

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

        if (PowerTierHandler.getPowerTier(tweakedMineralMix.getPowerTier()) == null) {
            ErrorLoggingLib.runtimeErrorLogging();
        }

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