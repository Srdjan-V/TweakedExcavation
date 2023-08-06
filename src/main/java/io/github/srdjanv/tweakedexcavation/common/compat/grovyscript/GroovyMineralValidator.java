package io.github.srdjanv.tweakedexcavation.common.compat.grovyscript;

import com.cleanroommc.groovyscript.api.GroovyLog;
import io.github.srdjanv.tweakedlib.api.powertier.PowerTierHandler;

import java.math.BigDecimal;
import java.util.List;

public class GroovyMineralValidator {
    public static void validateGroovyMineral(GroovyLog.Msg msg, String name, float failChance, List<String> ores, List<BigDecimal> chances, int weight, int powerTier, int oreYield,
                                             List<Integer> dimBlacklist, List<Integer> dimWhitelist) {

        validateName(msg, name);
        validateFailChance(msg, name, failChance);

        validateOres(msg, name, ores);
        validateChances(msg, name, chances);
        validateOresAndChances(msg, name, ores, chances);

        validateWeight(msg, name, weight);
        validatePowerTier(msg, name, powerTier);
        validateOreYield(msg, name, oreYield);

        validateDimBlacklist(msg, name, dimBlacklist);
        validateDimWhitelist(msg, name, dimWhitelist);

    }

    public static void validateName(GroovyLog.Msg msg, String name) {
        msg.add(name == null || name.isEmpty(),
                () -> "Mineral name can not be null or a empty string!");
    }

    public static void validateFailChance(GroovyLog.Msg msg, String name, float failChance) {
        msg.add(failChance < 0,
                "Mineral({}): failChance can not be smaller then 0!", name);
    }

    public static void validateOres(GroovyLog.Msg msg, String name, List<String> ores) {
        if (ores != null) {
            ores.forEach(ore -> {
                msg.add(ore == null,
                        "Mineral({}): Dim id: '{}' in dimWhitelist is Null", name, ore);
            });
        }
    }

    public static void validateChances(GroovyLog.Msg msg, String name, List<BigDecimal> chances) {
        if (chances != null) {
            chances.forEach(chance -> {
                msg.add(chance == null,
                        "Mineral({}): Dim id: '{}' in dimWhitelist is Null", name, chance);
            });
        }
    }

    public static void validateOresAndChances(GroovyLog.Msg msg, String name, List<String> ores, List<BigDecimal> chances) {
        if (ores != null && chances != null) {
            validateOresAndChances(msg, name, ores.size(), chances.size());
        }
    }

    public static void validateOresAndChances(GroovyLog.Msg msg, String name, int numberOfOres, int numberOfChances) {
        msg.add(numberOfOres != numberOfChances,
                "Mineral({}): is missing ores or chances in the Arrays", name);

        msg.add(numberOfOres > 14,
                "Mineral({}): Can not have more then 14 ores", name);
    }

    public static void validateWeight(GroovyLog.Msg msg, String name, int weight) {
        msg.add(weight < 1,
                "Mineral({}): weight has to be greater than or equal to 1!", name);
    }

    public static void validatePowerTier(GroovyLog.Msg msg, String name, int powerTier) {
        msg.add(!PowerTierHandler.powerTierExists(powerTier),
                "Mineral({}): supplied powerTier is not valid", name);
    }

    public static void validateOreYield(GroovyLog.Msg msg, String name, int oreYield) {
        msg.add(oreYield < 0,
                "Mineral({}): oreYield can not be smaller then 0!", name);
    }

    public static void validateDimBlacklist(GroovyLog.Msg msg, String name, List<Integer> dimBlacklist) {
        if (dimBlacklist != null) {
            dimBlacklist.forEach(id -> {
                msg.add(id == null,
                        "Mineral({}): Dim id: '{}' in dimBlacklist is Null", name, id);
            });
        }
    }

    public static void validateDimWhitelist(GroovyLog.Msg msg, String name, List<Integer> dimWhitelist) {
        if (dimWhitelist != null) {
            dimWhitelist.forEach(id -> {
                msg.add(id == null,
                        "Mineral({}): Dim id: '{}' in dimWhitelist is Null", name, id);
            });
        }
    }
}
