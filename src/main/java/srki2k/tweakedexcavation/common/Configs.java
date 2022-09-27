package srki2k.tweakedexcavation.common;

import net.minecraftforge.common.config.Config;
import srki2k.tweakedexcavation.TweakedExcavation;

public class Configs {

    @Config(modid = TweakedExcavation.MODID)
    public static class TPConfig {

        @Config.Name("Default Power Tiers")
        public static PowerTiers powerTiers;

        public static class PowerTiers {
            @Config.Comment({"This will set the power tier of the default IE Minerals, default=1"})
            @Config.Name("Default Minerals PowerTier")
            @Config.RangeInt(min = 0)
            @Config.RequiresMcRestart
            public static int defaultPowerTier = 1;
        }

    }

}