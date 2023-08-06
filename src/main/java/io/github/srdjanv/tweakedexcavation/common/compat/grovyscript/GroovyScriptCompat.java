package io.github.srdjanv.tweakedexcavation.common.compat.grovyscript;

import io.github.srdjanv.tweakedlib.common.compat.groovyscript.GroovyScriptRegistry;

public class GroovyScriptCompat {
    public static void init() {
        GroovyScriptRegistry.getRegistry().addRegistry(new TweakedGroovyExcavator());
    }
}
