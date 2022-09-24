package srki2k.tweakedexcavation.common;

import net.minecraftforge.common.config.Config;
import srki2k.tweakedexcavation.TweakedExcavation;

public class Configs {

    @Config(modid = TweakedExcavation.MODID)
    public static class TPConfig {

        @Config.Name("Logging")
        public static Logging logging;

        public static class Logging {
            @Config.Comment({"This will log missing power tiers on startup",
                    "it will still crash if you try to use a non existent power tier and generate a report, even if this setting is enabled",
                    "recommend while developing a pack but not in production, default=false"})
            @Config.Name("Log Missing PowerTiers on startup")
            @Config.RequiresMcRestart
            public static boolean logMissingPowerTier = false;

            @Config.Comment({"Log errors to the player once he joins the game, default=true"})
            @Config.Name("Log errors to players")
            @Config.RequiresMcRestart
            public static boolean logToPlayers = true;

        }

        @Config.Name("Default Power Tiers")
        public static DefaultPowerTiers defaultPowerTiers;

        public static class DefaultPowerTiers {
            @Config.Comment({"This will set the power tier of the default IE Minerals, default=0"})
            @Config.Name("Default Minerals PowerTier")
            @Config.RangeInt(min = 0)
            @Config.RequiresMcRestart
            public static int defaultPowerTier = 0;
        }

    }

}