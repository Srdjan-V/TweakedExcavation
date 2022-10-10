package srki2k.tweakedexcavation.util;

import org.apache.logging.log4j.Logger;
import srki2k.tweakedexcavation.TweakedExcavation;
import srki2k.tweakedexcavation.api.ihelpers.IMineralMix;
import srki2k.tweakedlib.api.logging.errorlogginglib.ErrorLoggingLib;
import srki2k.tweakedlib.api.logging.errorlogginglib.ICustomLogger;
import srki2k.tweakedlib.api.powertier.PowerTierHandler;

import java.util.ArrayList;
import java.util.List;

import static blusunrize.immersiveengineering.api.tool.ExcavatorHandler.mineralList;
import static srki2k.tweakedexcavation.common.Configs.TPConfig.PowerTiers.defaultPowerTier;

public final class TweakedExcavationErrorLogging implements ICustomLogger {
    public static void register() {
        ErrorLoggingLib.addCustomLogger(new TweakedExcavationErrorLogging());
    }

    private TweakedExcavationErrorLogging() {
    }

    List<String> errors = new ArrayList<>();

    @Override
    public boolean startupChecks() {
        return false;
    }

    @Override
    public boolean runtimeChecks() {
        mineralList.keySet().
                forEach(mineralMix -> {
                    if (!PowerTierHandler.powerTierExists(((IMineralMix) mineralMix).getPowerTier())) {
                        errors.add("Mineral with the ID (name) " + mineralMix.name + " has no valid Power tier");
                    }
                });

        return !errors.isEmpty();
    }

    @Override
    public boolean discardLoggerAfterStartup() {
        return false;
    }

    @Override
    public Logger getModLogger() {
        return TweakedExcavation.LOGGER;
    }

    @Override
    public String[] getConfigs() {
        String[] strings = new String[1];

        strings[0] = "Default Minerals PowerTier: " + defaultPowerTier;

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
