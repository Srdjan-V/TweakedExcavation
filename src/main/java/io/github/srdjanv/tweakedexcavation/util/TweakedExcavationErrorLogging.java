package io.github.srdjanv.tweakedexcavation.util;

import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralMix;
import io.github.srdjanv.tweakedexcavation.common.Configs;
import org.apache.logging.log4j.Logger;
import io.github.srdjanv.tweakedexcavation.TweakedExcavation;
import io.github.srdjanv.tweakedlib.api.logging.errorlogginglib.ErrorLoggingLib;
import io.github.srdjanv.tweakedlib.api.logging.errorlogginglib.ICustomLogger;
import io.github.srdjanv.tweakedlib.api.powertier.PowerTierHandler;

import java.util.ArrayList;
import java.util.List;

import static blusunrize.immersiveengineering.api.tool.ExcavatorHandler.mineralList;

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
        String[] strings = new String[3];

        strings[0] = "Default Excavator Power Tiers:";
        strings[1] = "Default capacity: " + Configs.TPConfig.DefaultExcavatorPowerTiers.capacity;
        strings[2] = "Default consumption: " + Configs.TPConfig.DefaultExcavatorPowerTiers.rft;

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
