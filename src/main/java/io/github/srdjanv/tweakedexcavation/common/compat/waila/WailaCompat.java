package io.github.srdjanv.tweakedexcavation.common.compat.waila;

import blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxReceiver;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityBucketWheel;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityExcavator;
import io.github.srdjanv.tweakedexcavation.TweakedExcavation;
import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralMix;
import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralWorldInfo;
import io.github.srdjanv.tweakedexcavation.util.TweakedExcavationInitializer;
import io.github.srdjanv.tweakedlib.api.hei.BaseHEIUtil;
import io.github.srdjanv.tweakedlib.api.powertier.PowerTierHandler;
import io.github.srdjanv.tweakedlib.api.waila.DuplicateFilter;
import io.github.srdjanv.tweakedlib.api.waila.FEFilter;
import io.github.srdjanv.tweakedlib.api.waila.IEWallaOverwriteManager;
import io.github.srdjanv.tweakedlib.api.waila.Styling;
import io.github.srdjanv.tweakedlib.common.Constants;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mcjty.theoneprobe.api.TextStyleClass;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.List;

import static mcjty.theoneprobe.api.IProbeInfo.ENDLOC;
import static mcjty.theoneprobe.api.IProbeInfo.STARTLOC;

public class WailaCompat implements TweakedExcavationInitializer {
    private static final String NBT_TAG = "tweakedExTag";

    @Override public String getModID() {
        return TweakedExcavation.MODID;
    }

    @Override public boolean shouldRun() {
        return Constants.isWailaLoaded();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        IEWallaOverwriteManager manager = IEWallaOverwriteManager.getInstance();
        manager.registerIEBodyOverwrite(TileEntityExcavator.class, WailaCompat::getWailaBody);
        manager.registerIENBTDataOverwrite(TileEntityExcavator.class, WailaCompat::getNBTData);
    }

    private static List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getNBTData().hasKey(NBT_TAG)) {
            final var toolTip = new ObjectArrayList<String>();
            final var nbtList = (NBTTagList) accessor.getNBTData().getTag(NBT_TAG);

            for (NBTBase nbtBase : nbtList) {
                if (nbtBase instanceof NBTTagString string) {
                    toolTip.add(Styling.stylifyString(string.getString()));
                }
            }
            DuplicateFilter.add(currenttip, toolTip);
        }

        FEFilter.filter(currenttip);
        return currenttip;
    }

    private static NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        TileEntityExcavator master = ((TileEntityExcavator) te).master();
        if (master == null) return tag;
        IMineralWorldInfo info = (IMineralWorldInfo) ExcavatorHandler.getMineralWorldInfo(master.getWorld(), master.getPos().getX() >> 4, master.getPos().getZ() >> 4);
        List<String> tips = new ObjectArrayList<>();
        localBreak:
        {
            if (info == null) {
                tips.add(STARTLOC + "tweakedexcavation.hud.mineral.empty" + ENDLOC);
                break localBreak;
            }

            IMineralMix iMineralMix = (IMineralMix) info.getType();
            if (iMineralMix == null) {
                tips.add(STARTLOC + "tweakedexcavation.hud.mineral.empty" + ENDLOC);
                break localBreak;
            } else {
                if (master.active && (info.getDepletion() == iMineralMix.getYield())) {
                    tips.add(STARTLOC + "tweakedexcavation.hud.mineral.unknown" + ENDLOC);
                } else if (!master.active) {
                    BlockPos wheelPos = master.getBlockPosForPos(31);
                    TileEntity tileWheel = master.getWorld().getTileEntity(wheelPos);
                    if (!(tileWheel instanceof TileEntityBucketWheel)) {
                        tips.add(STARTLOC + "tweakedexcavation.hud.mineral.missing_wheel" + ENDLOC);
                    } else tips.add(STARTLOC + "tweakedexcavation.hud.mineral.req_power" + ENDLOC);
                }
            }

            if (master.active && (info.getDepletion() != iMineralMix.getYield())) {
                tips.add(STARTLOC + "tweakedexcavation.hud.mineral.name" + ENDLOC + " " + BaseHEIUtil.formatString(((ExcavatorHandler.MineralMix) iMineralMix).name));
                if (iMineralMix.getYield() < 0) {
                    tips.add(STARTLOC + "tweakedexcavation.jei.mineral.average.Infinite" + ENDLOC);
                } else {
                    tips.add(STARTLOC + "tweakedexcavation.hud.mineral.yield" + ENDLOC + String.format("§7 %s / %s",
                            Math.min(1 + (iMineralMix.getYield() - info.getDepletion()),
                                    iMineralMix.getYield()), iMineralMix.getYield()));
                }
            }

            var powerTier = PowerTierHandler.getPowerTier(iMineralMix.getPowerTier());
            tips.add(STARTLOC + "tweakedlib.jei.power_tier" + ENDLOC + " " +
                    BaseHEIUtil.numberFormat.format(PowerTierHandler.getTierOfSpecifiedPowerTier(iMineralMix.getPowerTier())));
            tips.add(STARTLOC + "tweakedlib.jei.power_usage" + ENDLOC + " " +
                    BaseHEIUtil.numberFormat.format(powerTier.getRft()) + " IF/t");

            tips.add(String.format("§7 %s / %s IF",
                    BaseHEIUtil.numberFormat.format(((IFluxReceiver) te).getEnergyStored(null)),
                    BaseHEIUtil.numberFormat.format(powerTier.getCapacity())));
        }
        if (!tips.isEmpty()) {
            var tweakedPetrTag = new NBTTagList();
            for (String tip : tips) tweakedPetrTag.appendTag(new NBTTagString(tip));
            tag.setTag(NBT_TAG, tweakedPetrTag);
        }
        return tag;


    }
}
