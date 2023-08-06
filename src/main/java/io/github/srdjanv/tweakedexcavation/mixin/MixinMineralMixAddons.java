package io.github.srdjanv.tweakedexcavation.mixin;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralMix;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;


@Mixin(value = ExcavatorHandler.MineralMix.class, remap = false)
public class MixinMineralMixAddons implements IMineralMix {

    @Shadow
    public String name;
    @Shadow
    public String[] ores;
    @Shadow
    public float[] chances;
    @Shadow
    public NonNullList<ItemStack> oreOutput;
    @Shadow
    public float[] recalculatedChances;
    @Shadow
    public int[] dimensionWhitelist;
    @Shadow
    public int[] dimensionBlacklist;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Shadow
    public float failChance;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getFailChance() {
        return failChance;
    }

    @Override
    public String[] getOres() {
        return ores;
    }

    @Override
    public float[] getChances() {
        return chances;
    }

    @Override
    public List<ItemStack> getOreOutput() {
        return oreOutput;
    }

    @Override
    public float[] getRecalculatedChances() {
        return recalculatedChances;
    }

    @Override
    public int[] getDimensionWhitelist() {
        return dimensionWhitelist;
    }

    @Override
    public int[] getDimensionBlacklist() {
        return dimensionBlacklist;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setFailChance(float failChance) {
        this.failChance = failChance;
    }

    @Override
    public void setOres(String[] ores) {
        this.ores = ores;
    }

    @Override
    public void setChances(float[] chances) {
        this.chances = chances;
    }

    @Override
    public void setRecalculatedChances(float[] recalculatedChances) {
        this.recalculatedChances = recalculatedChances;
    }

    @Override
    public void setDimensionWhitelist(int[] dimensionWhitelist) {
        this.dimensionWhitelist = dimensionWhitelist;
    }

    @Override
    public void setDimensionBlacklist(int[] dimensionBlacklist) {
        this.dimensionBlacklist = dimensionBlacklist;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Unique
    int tweakedExcavation$powerTier;

    @Unique
    @Override
    public int getPowerTier() {
        return tweakedExcavation$powerTier;
    }

    @Unique
    @Override
    public void setPowerTier(int powerTier) {
        this.tweakedExcavation$powerTier = powerTier;
    }

    @Unique
    private int tweakedExcavation$yield = ExcavatorHandler.mineralVeinCapacity;

    @Unique
    @Override
    public int getYield() {
        return tweakedExcavation$yield;
    }

    @Unique
    @Override
    public void setYield(int yield) {
        this.tweakedExcavation$yield = yield;
    }


    @Inject(method = "readFromNBT", at = @At("RETURN"))
    private static void onReadFromNBT(NBTTagCompound nbt, CallbackInfoReturnable<ExcavatorHandler.MineralMix> cir) {
        IMineralMix mineral = (IMineralMix) cir.getReturnValue();

        mineral.setPowerTier(nbt.getInteger("powerTier"));
        mineral.setYield(nbt.getInteger("yield"));
    }

    @Inject(method = "writeToNBT", at = @At("RETURN"))
    public void onWriteToNBT(CallbackInfoReturnable<NBTTagCompound> cir) {
        NBTTagCompound tag = cir.getReturnValue();
        tag.setInteger("powerTier", this.tweakedExcavation$powerTier);
        tag.setInteger("yield", this.tweakedExcavation$yield);
    }

    //Equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IMineralMix that = (IMineralMix) o;
        return name.equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
