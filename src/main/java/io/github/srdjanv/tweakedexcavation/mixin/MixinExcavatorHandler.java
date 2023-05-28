package io.github.srdjanv.tweakedexcavation.mixin;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import io.github.srdjanv.tweakedexcavation.api.ihelpers.IMineralMix;
import io.github.srdjanv.tweakedexcavation.api.ihelpers.IMineralWorldInfo;
import io.github.srdjanv.tweakedexcavation.common.DefaultMineralPower;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static blusunrize.immersiveengineering.api.tool.ExcavatorHandler.getMineralWorldInfo;

@Mixin(value = ExcavatorHandler.class, remap = false)
public class MixinExcavatorHandler {


    /**
     * @author Srki_2K
     * @reason Added the ability to specify the Mineral size for each mineral
     */
    @Overwrite
    public static ExcavatorHandler.MineralMix getRandomMineral(World world, int chunkX, int chunkZ) {
        if (world.isRemote) {
            return null;
        }

        ExcavatorHandler.MineralWorldInfo info = getMineralWorldInfo(world, chunkX, chunkZ);
        if (info == null || info.mineral == null && info.mineralOverride == null) {
            return null;
        }

        IMineralMix mineralMix = (IMineralMix) ((IMineralWorldInfo) info).getType();

        if (mineralMix.getYield() >= 0 && info.depletion > mineralMix.getYield()) {
            return null;
        }

        return (ExcavatorHandler.MineralMix) mineralMix;

    }

    @Inject(method = "addMineral", at = @At("RETURN"))
    private static void addMineral(String name, int mineralWeight, float failChance, String[] ores, float[] chances, CallbackInfoReturnable<ExcavatorHandler.MineralMix> cir) {
        ((IMineralMix) cir.getReturnValue()).setPowerTier(DefaultMineralPower.getInstance().getPowerTier());
    }
}
