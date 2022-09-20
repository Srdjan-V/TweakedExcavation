package srki2k.tweakedexcavation.mixin;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import srki2k.tweakedexcavation.api.ihelpers.IMineralMix;

@Mixin(value = ExcavatorHandler.MineralMix.class, remap = false)
public class MixinMineralMix implements IMineralMix {



    @Unique
    int powerTier;

    @Override
    public int getPowerTier() {
        return powerTier;
    }

    @Override
    public void setPowerTier(int i) {
        powerTier = i;
    }

}
