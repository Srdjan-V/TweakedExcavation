package io.github.srdjanv.tweakedexcavation.util;

import com.cleanroommc.groovyscript.api.GroovyLog;
import crafttweaker.CraftTweakerAPI;
import io.github.srdjanv.tweakedlib.api.powertier.PowerTierHandler;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.ints.IntList;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MineralValidator {
    public static boolean validateGroovyMineral(GroovyLog.Msg msg, String name, float failChance,
                                                Collection<String> ores, DoubleCollection chances,
                                                int weight, int powerTier, int oreYield) {
        var builder = new MessageBuilder(msg::add);
        return validateCommon(builder, name, failChance, ores, chances, weight, powerTier, oreYield);
    }

    public static boolean validateCTMineral(String name, float failChance,
                                            String[] ores, float[] chances,
                                            int weight, int powerTier, int oreYield) {
        var builder = new MessageBuilder(CraftTweakerAPI::logError);
        var dChances = IntStream.range(0, chances.length)
                .mapToDouble(i -> chances[i])
                .toArray();

        return validateCommon(builder, name, failChance,
                Arrays.stream(ores).collect(Collectors.toList()),
                new DoubleArrayList(dChances),
                weight, powerTier, oreYield);
    }

    public static boolean validateCommon(MessageBuilder builder, String name, float failChance,
                                         Collection<String> ores, DoubleCollection chances,
                                         int weight, int powerTier, int oreYield) {
        validateName(builder, name);
        if (!builder.isValid()) name = "INVALID_NAME";
        validateFailChance(builder, name, failChance);

        validateOres(builder, name, ores, chances);
        validateChances(builder, name, chances);
        validateOresAndChances(builder, name, ores, chances);

        validateWeight(builder, name, weight);
        validatePowerTier(builder, name, powerTier);
        validateOreYield(builder, name, oreYield);
        return builder.isValid();
    }


    public static void validateName(MessageBuilder builder, String name) {
        builder.check(name == null || name.isEmpty(),
                "Mineral name can not be null or a empty string!");
    }

    public static void validateFailChance(MessageBuilder builder, String name, float failChance) {
        builder.check(failChance < 0,
                "Mineral({}): failChance can not be smaller then 0!", name);

        builder.check(failChance > 1,
                "Mineral({}): failChance can not be greater then 1!", name);
    }

    public static void validateOres(MessageBuilder builder, String name, Collection<String> ores, DoubleCollection chances) {
        if (ores == null) return;
        ores.forEach(ore -> {
            builder.check(ore == null,
                    "Mineral({}): Dim id: '{}' in dimWhitelist is Null", name, ore);
        });

        builder.check(ores.size() != chances.size(),
                "Mineral({}): is missing ores or chances in the Arrays", name);
        builder.check(ores.size() > 14,
                "Mineral({}): Should not have more then 14 ores, it will not be displayed correctly in jei", name);
    }

    public static void validateChances(MessageBuilder builder, String name, DoubleCollection chances) {
        if (chances == null) return;
        chances.forEach(chance -> {
            builder.check(chance == null,
                    "Mineral({}): Dim id: '{}' in dimWhitelist is Null", name, chance);
        });
    }

    public static void validateOresAndChances(MessageBuilder builder, String name, Collection<String> ores, DoubleCollection chances) {
        if (ores == null || chances == null) return;
        validateOresAndChances(builder, name, ores.size(), chances.size());
    }

    public static void validateOresAndChances(MessageBuilder builder, String name, int numberOfOres, int numberOfChances) {
        builder.check(numberOfOres != numberOfChances,
                "Mineral({}): is missing ores or chances in the Arrays", name);

        builder.check(numberOfOres > 14,
                "Mineral({}): Can not have more then 14 ores", name);
    }

    public static void validateWeight(MessageBuilder builder, String name, int weight) {
        builder.check(weight < 1,
                "Mineral({}): weight has to be greater than or equal to 1!", name);
    }

    public static void validatePowerTier(MessageBuilder builder, String name, int powerTier) {
        builder.check(!PowerTierHandler.powerTierExists(powerTier),
                "Mineral({}): supplied powerTier is not valid", name);
    }

    public static void validateOreYield(MessageBuilder builder, String name, int oreYield) {
        builder.check(oreYield < 0,
                "Mineral({}): oreYield can not be smaller then 0!", name);
    }


    public static class MessageBuilder {
        private final Consumer<String> errorConsumer;
        private boolean isValid = true;

        public MessageBuilder(Consumer<String> errorConsumer) {
            this.errorConsumer = errorConsumer;
        }

        public void check(boolean condition, String msg, Object... args) {
            if (!condition) return;
            isValid = false;
            errorConsumer.accept(args.length == 0 ? msg : new ParameterizedMessage(msg, args).getFormattedMessage());
        }

        public boolean isValid() {
            return isValid;
        }
    }

}
