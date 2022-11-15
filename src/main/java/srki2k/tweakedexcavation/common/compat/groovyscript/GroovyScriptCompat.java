package srki2k.tweakedexcavation.common.compat.groovyscript;

import com.cleanroommc.groovyscript.compat.mods.ModPropertyContainer;
import com.cleanroommc.groovyscript.compat.mods.ModSupport;
import srki2k.tweakedexcavation.TweakedExcavation;
import srki2k.tweakedlib.util.groovyscript.GroovyScriptModSupportContainerWrapper;

public final class GroovyScriptCompat extends ModPropertyContainer {

    private static ModSupport.Container<GroovyScriptCompat> modSupportContainer;

    public static void init() {
        modSupportContainer = GroovyScriptModSupportContainerWrapper.registerGroovyContainer(TweakedExcavation.MODID, "TweakedExcavation", GroovyScriptCompat::new);
    }

    private GroovyScriptCompat() {
        addRegistry(TweakedGroovyExcavator.init());
    }
}
