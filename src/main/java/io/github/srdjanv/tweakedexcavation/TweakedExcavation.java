package io.github.srdjanv.tweakedexcavation;

import io.github.srdjanv.tweakedexcavation.common.CustomMineralBlocks;
import io.github.srdjanv.tweakedexcavation.util.TweakedExcavationErrorLogging;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(modid = TweakedExcavation.MODID,
        version = TweakedExcavation.VERSION,
        name = "Tweaked Excavation",
        dependencies = "required-after:crafttweaker;" +
                "required-after:tweakedlib@[" + Tags.TWEAKED_LIB_VERSION + ",)")
public class TweakedExcavation {

    public static final String MODID = "tweakedexcavation";
    public static final String VERSION = Tags.VERSION;

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MODID)) {
            ConfigManager.sync(MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        TweakedExcavationErrorLogging.register();
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        CustomMineralBlocks.cleanCache();
    }

}