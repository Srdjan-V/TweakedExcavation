package io.github.srdjanv.tweakedexcavation.api.mixins;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;

public interface IMineralWorldInfo {
    ExcavatorHandler.MineralMix getType();

    int getDepletion();
}
