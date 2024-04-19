package io.github.srdjanv.tweakedexcavation.util;

import io.github.srdjanv.tweakedexcavation.TweakedExcavation;
import io.github.srdjanv.tweakedlib.api.integration.IInitializer;

public interface TweakedExcavationInitializer extends IInitializer {
    @Override default String getModID() {
        return TweakedExcavation.MODID;
    }
}
