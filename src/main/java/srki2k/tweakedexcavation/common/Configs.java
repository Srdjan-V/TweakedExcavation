package srki2k.tweakedexcavation.common;

import net.minecraftforge.common.config.Config;
import srki2k.tweakedexcavation.TweakedExcavation;

public class Configs {

    @Config(modid = TweakedExcavation.MODID)
    public static class TPConfig {

            @Config.Name("Default Excavator Power Tiers")
            public static DefaultExcavatorPowerTiers defaultExcavatorPowerTiers;

            public static class DefaultExcavatorPowerTiers {

                @Config.Comment({"This will set the capacity of the excavator, default=64000"})
                @Config.Name("Default capacity")
                @Config.RangeInt(min = 1)
                @Config.RequiresMcRestart
                public static int capacity = 64000;

                @Config.Comment({"This will set the power consumption of the excavator, default=4096"})
                @Config.Name("Default consumption")
                @Config.RangeInt(min = 1)
                @Config.RequiresMcRestart
                public static int rft = 4096;

            }

    }

}