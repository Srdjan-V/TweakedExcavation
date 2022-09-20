package srki2k.tweakedexcavation.api.ihelpers;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;

public interface IMineralWorldInfo {
    ExcavatorHandler.MineralMix getType();

    int getDepletion();
}
