package io.github.srdjanv.tweakedexcavation.common.compat.grovyscript;

import io.github.srdjanv.tweakedexcavation.TweakedExcavation;
import io.github.srdjanv.tweakedexcavation.util.TweakedExcavationInitializer;
import io.github.srdjanv.tweakedlib.common.Constants;
import io.github.srdjanv.tweakedlib.common.compat.groovyscript.GroovyScriptRegistry;
import io.github.srdjanv.tweakedlib.api.integration.IInitializer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SuppressWarnings("unused")
public class GroovyCompatInitializer implements TweakedExcavationInitializer {
    @Override public boolean shouldRun() {
        return Constants.isGroovyScriptLoaded();
    }

    @Override public void preInit(FMLPreInitializationEvent event) {
        GroovyScriptRegistry.getRegistry().addRegistry(new TweakedGroovyExcavator());
    }
}
