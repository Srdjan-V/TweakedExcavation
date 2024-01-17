package io.github.srdjanv.tweakedexcavation.common.compat.top;

import blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxReceiver;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityExcavator;
import io.github.srdjanv.tweakedexcavation.TweakedExcavation;
import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralMix;
import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralWorldInfo;
import io.github.srdjanv.tweakedlib.api.hei.BaseHEIUtil;
import io.github.srdjanv.tweakedlib.api.powertier.PowerTierHandler;
import io.github.srdjanv.tweakedlib.api.top.TopOverwriteManager;
import io.github.srdjanv.tweakedlib.common.Constants;
import io.github.srdjanv.tweakedlib.api.integration.IInitializer;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import static io.github.srdjanv.tweakedlib.api.hei.BaseHEIUtil.translateToLocal;
import static io.github.srdjanv.tweakedlib.api.hei.BaseHEIUtil.translateToLocalFormatted;

public class TopCompat implements IInitializer {

    @Override public String getModID() {
        return TweakedExcavation.MODID;
    }

    @Override public boolean shouldRun() {
        return Constants.isTheOneProbeLoaded();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        TopOverwriteManager manager = TopOverwriteManager.getInstance();
        manager.registerOverwrite(TileEntityExcavator.class, TopCompat::addProbeInfo);
    }

    private static void addProbeInfo(TileEntity te, ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntityExcavator master = ((TileEntityExcavator) te).master();
        if (master == null) return;
        var info = (IMineralWorldInfo) ExcavatorHandler.getMineralWorldInfo(master.getWorld(), master.getPos().getX() >> 4, master.getPos().getZ() >> 4);
        if (info == null) {
            probeInfo.text(translateToLocalFormatted("tweakedexcavation.hud.empty"));
            return;
        }

        IMineralMix iMineralMix = (IMineralMix) info.getType();
        if (info.getDepletion() == iMineralMix.getYield()) {
            probeInfo.text(translateToLocalFormatted("tweakedexcavation.hud.unknown"));
            return;
        }

        probeInfo.text(translateToLocalFormatted("tweakedexcavation.hud.name", BaseHEIUtil.formatString(((ExcavatorHandler.MineralMix) iMineralMix).name)));
        if (iMineralMix.getYield() < 0) {
            probeInfo.text(translateToLocal("tweakedexcavation.jei.mineral.average.Infinite"));
            return;
        } else
            probeInfo.text(" §7" + translateToLocalFormatted("tweakedexcavation.hud.yield", String.valueOf(1 + iMineralMix.getYield() - info.getDepletion())));
        probeInfo.text("");

        var powerTier = PowerTierHandler.getPowerTier(iMineralMix.getPowerTier());
        probeInfo.text(translateToLocalFormatted("tweakedlib.jei.power_tier", BaseHEIUtil.numberFormat.format(PowerTierHandler.getTierOfSpecifiedPowerTier(iMineralMix.getPowerTier()))));
        probeInfo.progress(((IFluxReceiver) te).getEnergyStored(null), powerTier.getCapacity(),
                new ProgressStyle().numberFormat(NumberFormat.COMMAS));
    }
}
