package srki2k.tweakedexcavation.util.groovy;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import net.minecraft.item.ItemStack;
import srki2k.tweakedexcavation.api.ihelpers.IMineralMix;
import srki2k.tweakedexcavation.api.ihelpers.IMineralMixGetters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroovyMineralWrapper implements IMineralMixGetters {

    private final IMineralMix mineralMix;

    private final int weight;

    @GroovyBlacklist
    public GroovyMineralWrapper(IMineralMix mineralMix, int weight) {
        this.mineralMix = mineralMix;
        this.weight = weight;
    }

    @GroovyBlacklist
    public GroovyMineralWrapper(ExcavatorHandler.MineralMix mineralMix, int weight) {
        this.mineralMix = (IMineralMix) mineralMix;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public ExcavatorHandler.MineralMix getMineralMix() {
        return (ExcavatorHandler.MineralMix) mineralMix;
    }

    @Override
    public String getName() {
        return mineralMix.getName();
    }

    @Override
    public float getFailChance() {
        return mineralMix.getFailChance();
    }

    @Override
    public String[] getOres() {
        return mineralMix.getOres();
    }

    @Override
    public float[] getChances() {
        return mineralMix.getChances().clone();
    }

    @Override
    public List<ItemStack> getOreOutput() {
        return new ArrayList<>(mineralMix.getOreOutput());
    }

    @Override
    public float[] getRecalculatedChances() {
        return mineralMix.getRecalculatedChances().clone();
    }

    @Override
    public int[] getDimensionWhitelist() {
        return mineralMix.getDimensionWhitelist().clone();
    }

    @Override
    public int[] getDimensionBlacklist() {
        return mineralMix.getDimensionBlacklist().clone();
    }

    @Override
    public int getYield() {
        return mineralMix.getYield();
    }

    @Override
    public int getPowerTier() {
        return mineralMix.getPowerTier();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroovyMineralWrapper that = (GroovyMineralWrapper) o;
        return mineralMix.getName().equals(that.mineralMix.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(mineralMix.getName());
    }

}
