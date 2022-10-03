package srki2k.tweakedexcavation.mixin;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srki2k.tweakedexcavation.api.ihelpers.IMineralMix;
import srki2k.tweakedexcavation.api.ihelpers.IMineralWorldInfo;
import srki2k.tweakedexcavation.common.Configs;

import static blusunrize.immersiveengineering.api.tool.ExcavatorHandler.getMineralWorldInfo;
import static blusunrize.immersiveengineering.api.tool.ExcavatorHandler.mineralVeinCapacity;

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
        ((IMineralMix) cir.getReturnValue()).setPowerTier(Configs.TPConfig.PowerTiers.defaultPowerTier);
    }
}
