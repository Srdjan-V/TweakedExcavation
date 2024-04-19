package io.github.srdjanv.tweakedexcavation.mixin;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralWorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
