package srki2k.tweakedexcavation.util.groovy;

import com.cleanroommc.groovyscript.api.GroovyLog;
import srki2k.tweakedlib.api.powertier.PowerTierHandler;

import java.util.List;

public class GroovyMineralValidator {


    public static void validateGroovyMineral(GroovyLog.Msg msg, String name, float failChance, List<String> ores, List<Float> chances, int weight, int powerTier, int oreYield,
                                             List<Integer> dimBlacklist, List<Integer> dimWhitelist) {


        msg.add(name == null || name.isEmpty(),
                () -> "Mineral name can not be null or a empty string!");

        msg.add(failChance < 0,
                "Mineral({}): failChance can not be smaller then 0!", name);

        if (ores != null && chances != null) {
            msg.add(ores.size() != chances.size(),
                    "Mineral({}): is missing ores or chances in the Arrays", name);

            msg.add(ores.size() > 14,
                    "Mineral({}): Can not have more then 14 ores", name);

        } else {
            msg.add(ores == null,
                    "Mineral({}): ores can not be null!", name);

            msg.add(chances == null,
                    "Mineral({}): chances can not be null!", name);
        }

        msg.add(oreYield < 0,
                "Mineral({}): oreYield can not be smaller then 0!", name);

        msg.add(weight < 1,
                "Mineral({}): weight has to be greater than or equal to 1!", name);

        msg.add(!PowerTierHandler.powerTierExists(powerTier),
                "Mineral({}): supplied powerTier is not valid", name);


        if (dimBlacklist != null) {
            dimBlacklist.forEach(id -> {
                msg.add(id == null,
                        "Mineral({}): Dim id: '{}' in dimBlacklist is Null", name, id);
            });
        }

        if (dimWhitelist != null) {
            dimWhitelist.forEach(id -> {
                msg.add(id == null,
                        "Mineral({}): Dim id: '{}' in dimWhitelist is Null", name, id);
            });
        }

    }

}
