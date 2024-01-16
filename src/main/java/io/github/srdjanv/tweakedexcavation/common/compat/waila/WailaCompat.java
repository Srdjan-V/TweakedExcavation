package io.github.srdjanv.tweakedexcavation.common.compat.waila;

import blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxReceiver;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityExcavator;
import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralMix;
import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralWorldInfo;
import io.github.srdjanv.tweakedlib.api.hei.BaseHEIUtil;
import io.github.srdjanv.tweakedlib.api.powertier.PowerTierHandler;
import io.github.srdjanv.tweakedlib.api.waila.WallaOverwriteManager;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static io.github.srdjanv.tweakedlib.api.hei.BaseHEIUtil.translateToLocalFormatted;

public class WailaCompat {
    private static final String nbtTag = "tweakedExTag";

    private static final String statusKey = "status";
    private static final String statusUnknown = "unknown";
    private static final String statusInf = "infinite";
    private static final String statusEmpty = "empty";

    private static final String NAME = "name";
    private static final String YIELD = "yield";
    private static final String POWER_TIER = "power_tier";
    private static final String POWER_CAPACITY = "power_capacity";
    private static final String CURRENT_RFPOWER = "current_rfpower";

    public static void init() {
        WallaOverwriteManager manager = WallaOverwriteManager.getInstance();
        manager.registerBodyOverwrite(TileEntityExcavator.class, WailaCompat::getWailaBody);
        manager.registerNBTDataOverwrite(TileEntityExcavator.class, WailaCompat::getNBTData);
    }

    private static List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getNBTData().hasKey(nbtTag)) {
            var toolTip = new ArrayList<String>();
            var tweakedExTag = (NBTTagCompound) accessor.getNBTData().getTag(nbtTag);
            switch (tweakedExTag.getString(statusKey)) {
                case statusUnknown -> {
                    toolTip.add(translateToLocalFormatted("tweakedexcavation.hud.unknown"));
                }
                case statusEmpty -> {
                    toolTip.add(translateToLocalFormatted("tweakedexcavation.hud.empty"));
                }
                case statusInf -> {
                    BaseHEIUtil.translateToLocal("tweakedexcavation.jei.mineral.average.Infinite");
                }
                default -> {
                    toolTip.add(translateToLocalFormatted("tweakedexcavation.hud.name", tweakedExTag.getString(NAME)));
                    toolTip.add(" §7" + translateToLocalFormatted("tweakedexcavation.hud.yield", tweakedExTag.getInteger(YIELD)));
                    toolTip.add(translateToLocalFormatted("tweakedlib.hud.power_tier", BaseHEIUtil.numberFormat.format(tweakedExTag.getInteger(POWER_TIER))));
                    toolTip.add("§7" + BaseHEIUtil.numberFormat.format(tweakedExTag.getInteger(CURRENT_RFPOWER)) +
                            "/" + BaseHEIUtil.numberFormat.format(tweakedExTag.getInteger(POWER_CAPACITY)) + " RF");
                }
            }

            //The tooltip function can get called 2 times in one tick
            if (!new HashSet<>(currenttip).containsAll(toolTip)) {
                currenttip.addAll(toolTip);
            }
        }
        return currenttip;
    }

    private static NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        TileEntityExcavator master = ((TileEntityExcavator) te).master();
        if (master == null) {
            return tag;
        }
        IMineralWorldInfo info = (IMineralWorldInfo) ExcavatorHandler.getMineralWorldInfo(master.getWorld(), master.getPos().getX() >> 4, master.getPos().getZ() >> 4);
        var tweakedExTag = new NBTTagCompound();
        tag.setTag(nbtTag, tweakedExTag);

        if (info == null) {
            tweakedExTag.setString(statusKey, statusEmpty);
            return tag;
        }

        IMineralMix iMineralMix = (IMineralMix) info.getType();
        if (iMineralMix == null) {
            if (master.energyStorage.getEnergyStored() != 0) {
                tweakedExTag.setString(statusKey, statusEmpty);
            } else tweakedExTag.setString(statusKey, statusUnknown);
            return tag;
        }

        if (info.getDepletion() == iMineralMix.getYield()) {
            tweakedExTag.setString(statusKey, statusUnknown);
            return tag;
        }

        var orgMineral = info.getType();
        IMineralMix mineral = (IMineralMix) orgMineral;
        tweakedExTag.setString(NAME, orgMineral.name);
        tweakedExTag.setInteger(YIELD, 1 + mineral.getYield() - info.getDepletion());

        tweakedExTag.setInteger(POWER_TIER, PowerTierHandler.getTierOfSpecifiedPowerTier(mineral.getPowerTier()));
        tweakedExTag.setInteger(POWER_CAPACITY, master.energyStorage.getMaxEnergyStored());
        tweakedExTag.setInteger(CURRENT_RFPOWER, master.energyStorage.getEnergyStored());

        return tag;
    }
}
