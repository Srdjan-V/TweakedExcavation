package srki2k.tweakedexcavation.util;

import srki2k.tweakedexcavation.TweakedExcavation;
import srki2k.tweakedexcavation.api.ihelpers.IMineralMix;
import srki2k.tweakedlib.api.logging.errorlogginglib.ErrorLoggingLib;
import srki2k.tweakedlib.api.logging.errorlogginglib.ICustomLogger;
import srki2k.tweakedlib.api.powertier.PowerTierHandler;

import java.util.ArrayList;
import java.util.List;

import static blusunrize.immersiveengineering.api.tool.ExcavatorHandler.mineralList;
import static srki2k.tweakedexcavation.common.Configs.TPConfig.Logging.*;

public final class TweakedExcavationErrorLogging implements ICustomLogger {

    private static ICustomLogger customLogger;

    public static void register() {
        if (customLogger == null) {
            customLogger = new TweakedExcavationErrorLogging();
            ErrorLoggingLib.addCustomLogger(customLogger);
        }
    }

    private TweakedExcavationErrorLogging() {
    }

    List<String> errors = new ArrayList<>();

    @Override
    public boolean doCustomCheck() {
        if (logMissingPowerTier) {
            missingPowerTiers();
            return true;
        }

        return false;
    }

    @Override
    public boolean handleRuntimeErrors() {
        //missingPowerTiersLog()
        missingPowerTiers();
        return !errors.isEmpty();
    }

    @Override
    public boolean discardLoggerAfterStartup() {
        return false;
    }

    @Override
    public boolean logErrorToUsersInGameWithCT() {
        return logToPlayers;
    }

    private void missingPowerTiers() {
        mineralList.keySet().
                forEach(mineralMix -> {
                    if (PowerTierHandler.getPowerTier(((IMineralMix) mineralMix).getPowerTier()) == null) {
                        errors.add("Mineral with the ID (name) " + mineralMix.name + " has no valid Power tier");
                    }
                });
    }

    @Override
    public String getMODID() {
        return TweakedExcavation.MODID;
    }

    @Override
    public String[] getConfigs() {
        String[] strings = new String[2];

        strings[0] = "Log missing reservoirs to players: " + logToPlayers;
        strings[1] = "Log Missing PowerTiers for on startup: " + logMissingPowerTier;

        return strings;
    }

    @Override
    public List<String> getErrors() {
        return errors;
    }

    @Override
    public void clean() {
        errors.clear();
    }
}
