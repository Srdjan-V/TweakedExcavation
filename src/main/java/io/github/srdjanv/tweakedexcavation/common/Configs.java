package io.github.srdjanv.tweakedexcavation.common;

import net.minecraftforge.common.config.Config;
import io.github.srdjanv.tweakedexcavation.TweakedExcavation;

public class Configs {

    @Config(modid = TweakedExcavation.MODID)
    public static class TEConfig {

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

        @Config.Name("JEI Config")
        public static HEIConfig heiConfig;

        public static class HEIConfig {

            @Config.Name("Draw PowerTier")
            public static boolean drawPowerTier = true;

            @Config.Name("Draw SpawnWeight")
            public static boolean drawSpawnWeight = true;

        }

    }

}