package io.github.srdjanv.tweakedexcavation.common.compat.top;

import blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxReceiver;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityBucketWheel;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityExcavator;
import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralMix;
import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralWorldInfo;
import io.github.srdjanv.tweakedexcavation.util.TweakedExcavationInitializer;
import io.github.srdjanv.tweakedlib.api.hei.BaseHEIUtil;
import io.github.srdjanv.tweakedlib.api.powertier.PowerTierHandler;
import io.github.srdjanv.tweakedlib.api.top.IETopOverwriteManager;
import io.github.srdjanv.tweakedlib.common.Constants;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import static mcjty.theoneprobe.api.IProbeInfo.ENDLOC;
import static mcjty.theoneprobe.api.IProbeInfo.STARTLOC;

public class TopCompat implements TweakedExcavationInitializer {

    @Override public boolean shouldRun() {
        return Constants.isTheOneProbeLoaded();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        IETopOverwriteManager manager = IETopOverwriteManager.getInstance();
        manager.registerEnergyInfoOverwrite(TileEntityExcavator.class, TopCompat::addProbeInfo);
    }

    private static void addProbeInfo(TileEntity te, ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntityExcavator master = ((TileEntityExcavator) te).master();
        if (master == null) return;
        var info = (IMineralWorldInfo) ExcavatorHandler.getMineralWorldInfo(master.getWorld(), master.getPos().getX() >> 4, master.getPos().getZ() >> 4);

        if (info == null) {
            probeInfo.text(TextStyleClass.INFO + STARTLOC + "tweakedexcavation.hud.mineral.empty" + ENDLOC);
            return;
        }

        IMineralMix iMineralMix = (IMineralMix) info.getType();
        if (iMineralMix == null) {
            probeInfo.text(TextStyleClass.INFO + STARTLOC + "tweakedexcavation.hud.mineral.empty" + ENDLOC);
            return;
        } else {
            if (master.active && (info.getDepletion() == iMineralMix.getYield())) {
                probeInfo.text(TextStyleClass.INFO + STARTLOC + "tweakedexcavation.hud.mineral.unknown" + ENDLOC);
            } else if (!master.active) {
                BlockPos wheelPos = master.getBlockPosForPos(31);
                TileEntity tileWheel = master.getWorld().getTileEntity(wheelPos);
                if (!(tileWheel instanceof TileEntityBucketWheel)) {
                    probeInfo.text(TextStyleClass.ERROR + STARTLOC + "tweakedexcavation.hud.mineral.missing_wheel" + ENDLOC);
                } else probeInfo.text(TextStyleClass.ERROR + STARTLOC + "tweakedexcavation.hud.mineral.req_power" + ENDLOC);
            }
        }

        if (master.active && (info.getDepletion() != iMineralMix.getYield())) {
            probeInfo.text(TextStyleClass.INFO + STARTLOC + "tweakedexcavation.hud.mineral.name" + ENDLOC + " " + BaseHEIUtil.formatString(((ExcavatorHandler.MineralMix) iMineralMix).name));
            if (iMineralMix.getYield() < 0) {
                probeInfo.text(TextStyleClass.INFO + STARTLOC + "tweakedexcavation.jei.mineral.average.Infinite" + ENDLOC);
            } else {
                probeInfo.text(TextStyleClass.INFO + STARTLOC + "tweakedexcavation.hud.mineral.yield" + ENDLOC);
                probeInfo.progress(Math.min(1 + (iMineralMix.getYield() - info.getDepletion()), iMineralMix.getYield()), iMineralMix.getYield(),
                        new ProgressStyle().numberFormat(NumberFormat.COMMAS)
                                .filledColor(0xff808080).alternateFilledColor(0xff262626).borderColor(-12705779)
                );
            }
        }

        var powerTier = PowerTierHandler.getPowerTier(iMineralMix.getPowerTier());
        probeInfo.text(TextStyleClass.INFO + STARTLOC + "tweakedlib.jei.power_tier" + ENDLOC + " " +
                BaseHEIUtil.numberFormat.format(PowerTierHandler.getTierOfSpecifiedPowerTier(iMineralMix.getPowerTier())));
        probeInfo.text(TextStyleClass.INFO + STARTLOC + "tweakedlib.jei.power_usage" + ENDLOC + " " +
                BaseHEIUtil.numberFormat.format(powerTier.getRft()) + " IF/t");

        probeInfo.progress(((IFluxReceiver) te).getEnergyStored(null), powerTier.getCapacity(),
                new ProgressStyle().numberFormat(NumberFormat.COMMAS)
                        .filledColor(-557004).alternateFilledColor(-6729952).borderColor(-12705779)
        );
    }
}
