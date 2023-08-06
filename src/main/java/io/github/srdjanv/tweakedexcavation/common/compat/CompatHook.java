package io.github.srdjanv.tweakedexcavation.common.compat;

import io.github.srdjanv.tweakedexcavation.common.compat.grovyscript.GroovyScriptCompat;
import io.github.srdjanv.tweakedexcavation.common.compat.top.TopCompat;
import io.github.srdjanv.tweakedexcavation.common.compat.waila.WailaCompat;
import io.github.srdjanv.tweakedlib.common.Constants;

public class CompatHook {
    public static void init() {
        if (Constants.isTheOneProbeLoaded()) TopCompat.init();
        if (Constants.isWailaLoaded()) WailaCompat.init();
        if (Constants.isGroovyScriptLoaded()) GroovyScriptCompat.init();
    }
}
