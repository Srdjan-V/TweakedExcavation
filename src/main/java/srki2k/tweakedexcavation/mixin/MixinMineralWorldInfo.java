package srki2k.tweakedexcavation.mixin;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import srki2k.tweakedexcavation.api.ihelpers.IMineralWorldInfo;

@Mixin(value = ExcavatorHandler.MineralWorldInfo.class, remap = false)
public class MixinMineralWorldInfo implements IMineralWorldInfo {

    @Shadow
    public ExcavatorHandler.MineralMix mineral;
    @Shadow
    public ExcavatorHandler.MineralMix mineralOverride;

    @Shadow
    public int depletion;

    @Override
    public ExcavatorHandler.MineralMix getType() {
        return this.mineralOverride == null ? this.mineral : this.mineralOverride;
    }

    @Override
    public int getDepletion() {
        return depletion;
    }
}
