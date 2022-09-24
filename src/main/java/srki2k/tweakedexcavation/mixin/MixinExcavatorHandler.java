package srki2k.tweakedexcavation.mixin;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srki2k.tweakedexcavation.api.ihelpers.IMineralMix;
import srki2k.tweakedexcavation.common.Configs;

@Mixin(value = ExcavatorHandler.class, remap = false)
public class MixinExcavatorHandler {

    @Inject(method = "addMineral", at = @At("RETURN"))
    private static void addMineral(String name, int mineralWeight, float failChance, String[] ores, float[] chances, CallbackInfoReturnable<ExcavatorHandler.MineralMix> cir) {
        ((IMineralMix) cir.getReturnValue()).setPowerTier(Configs.TPConfig.DefaultPowerTiers.defaultPowerTier);
    }
}
