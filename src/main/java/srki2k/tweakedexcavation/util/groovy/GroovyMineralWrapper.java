package srki2k.tweakedexcavation.util.groovy;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import net.minecraft.item.ItemStack;
import srki2k.tweakedexcavation.api.ihelpers.IMineralMix;
import srki2k.tweakedexcavation.api.ihelpers.IMineralMixGetters;
import srki2k.tweakedexcavation.common.compat.groovyscript.TweakedGroovyExcavator;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class GroovyMineralWrapper implements IMineralMixGetters {

    private InnerWrapper innerMineralWrapper;
    private InnerWrapper mutableInnerMineralWrapper;
    private GroovyLog.Msg groovyLogMsg;

    @GroovyBlacklist
    public GroovyMineralWrapper(IMineralMix mineralMix, int weight) {
        innerMineralWrapper = new InnerWrapper(mineralMix, weight);
        groovyLogMsg = GroovyLog.msg("Error editing Mineral").
                add("This Mineral is not editable!!!").error();
    }

    @GroovyBlacklist
    public GroovyMineralWrapper(ExcavatorHandler.MineralMix mineralMix, int weight) {
        innerMineralWrapper = new InnerWrapper(mineralMix, weight);
        groovyLogMsg = GroovyLog.msg("Error editing Mineral").error();
    }

    @GroovyBlacklist
    public InnerWrapper getInnerMineralWrapper() {
        return innerMineralWrapper;
    }

    @Override
    public String getName() {
        return innerMineralWrapper.mineralMix.getName();
    }

    @Override
    public float getFailChance() {
        return innerMineralWrapper.mineralMix.getFailChance();
    }

    @Override
    public String[] getOres() {
        return innerMineralWrapper.mineralMix.getOres().clone();
    }

    @Override
    public float[] getChances() {
        return innerMineralWrapper.mineralMix.getChances().clone();
    }

    @Override
    public List<ItemStack> getOreOutput() {
        return new ArrayList<>(innerMineralWrapper.mineralMix.getOreOutput());
    }

    @Override
    public float[] getRecalculatedChances() {
        return innerMineralWrapper.mineralMix.getRecalculatedChances().clone();
    }

    @Override
    public int[] getDimensionWhitelist() {
        return innerMineralWrapper.mineralMix.getDimensionWhitelist().clone();
    }

    @Override
    public int[] getDimensionBlacklist() {
        return innerMineralWrapper.mineralMix.getDimensionBlacklist().clone();
    }

    @Override
    public int getYield() {
        return innerMineralWrapper.mineralMix.getYield();
    }

    @Override
    public int getPowerTier() {
        return innerMineralWrapper.mineralMix.getPowerTier();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void cleanInstance() {
        innerMineralWrapper = null;
        mutableInnerMineralWrapper = null;
        groovyLogMsg = null;
    }

    private InnerWrapper getMutableObject() {
        if (mutableInnerMineralWrapper == null) {
            mutableInnerMineralWrapper = innerMineralWrapper.cloneWrapper();
        }
        return mutableInnerMineralWrapper;
    }

    public GroovyMineralWrapper setName(String name) {
        GroovyMineralValidator.validateName(groovyLogMsg, name);
        if (groovyLogMsg.hasSubMessages()) {
            return this;
        }

        getMutableObject().mineralMix.setName(name);
        return this;
    }

    public GroovyMineralWrapper setWeight(int weight) {
        GroovyMineralValidator.validateWeight(groovyLogMsg, innerMineralWrapper.mineralMix.getName(), weight);
        if (groovyLogMsg.hasSubMessages()) {
            return this;
        }

        getMutableObject().weight = weight;
        return this;
    }

    public GroovyMineralWrapper setFailChance(float failChance) {
        GroovyMineralValidator.validateFailChance(groovyLogMsg, innerMineralWrapper.mineralMix.getName(), failChance);
        if (groovyLogMsg.hasSubMessages()) {
            return this;
        }

        getMutableObject().mineralMix.setFailChance(failChance);
        return this;
    }

    public GroovyMineralWrapper setOres(List<String> ores) {
        GroovyMineralValidator.validateOres(groovyLogMsg, innerMineralWrapper.mineralMix.getName(), ores);
        if (groovyLogMsg.hasSubMessages()) {
            return this;
        }

        getMutableObject().mineralMix.setOres(ores.toArray(new String[0]));
        return this;
    }

    public GroovyMineralWrapper setChances(List<BigDecimal> chances) {
        GroovyMineralValidator.validateChances(groovyLogMsg, innerMineralWrapper.mineralMix.getName(), chances);
        if (groovyLogMsg.hasSubMessages()) {
            return this;
        }

        double[] doubleChances = chances.stream().mapToDouble(BigDecimal::doubleValue).toArray();
        float[] floatChances = new float[doubleChances.length];
        for (int i = 0; i < doubleChances.length; i++) {
            floatChances[i] = (float) doubleChances[i];
        }

        getMutableObject().mineralMix.setChances(floatChances);
        return this;
    }

    public GroovyMineralWrapper setDimensionWhitelist(int[] dimensionWhitelist) {
        GroovyMineralValidator.validateDimWhitelist(groovyLogMsg, innerMineralWrapper.mineralMix.getName(), Arrays.stream(dimensionWhitelist).boxed().collect(Collectors.toList()));
        if (groovyLogMsg.hasSubMessages()) {
            return this;
        }

        getMutableObject().mineralMix.setDimensionWhitelist(dimensionWhitelist);
        return this;
    }

    public GroovyMineralWrapper setDimensionBlacklist(int[] dimensionBlacklist) {
        GroovyMineralValidator.validateDimBlacklist(groovyLogMsg, innerMineralWrapper.mineralMix.getName(), Arrays.stream(dimensionBlacklist).boxed().collect(Collectors.toList()));
        if (groovyLogMsg.hasSubMessages()) {
            return this;
        }

        getMutableObject().mineralMix.setDimensionBlacklist(dimensionBlacklist);
        return this;
    }

    public GroovyMineralWrapper setYield(int yield) {
        GroovyMineralValidator.validateOreYield(groovyLogMsg, innerMineralWrapper.mineralMix.getName(), yield);
        if (groovyLogMsg.hasSubMessages()) {
            return this;
        }

        getMutableObject().mineralMix.setYield(yield);
        return this;
    }

    public GroovyMineralWrapper setPowerTier(int powerTier) {
        GroovyMineralValidator.validatePowerTier(groovyLogMsg, innerMineralWrapper.mineralMix.getName(), powerTier);
        if (groovyLogMsg.hasSubMessages()) {
            return this;
        }

        getMutableObject().mineralMix.setPowerTier(powerTier);
        return this;
    }

    public void apply() {
        if (mutableInnerMineralWrapper == null) {
            groovyLogMsg.add("No values have need changed before calling apply()").post();
        } else {
            GroovyMineralValidator.validateOresAndChances(groovyLogMsg, innerMineralWrapper.mineralMix.getName(),
                    mutableInnerMineralWrapper.mineralMix.getOres().length, mutableInnerMineralWrapper.mineralMix.getChances().length);
        }

        if (groovyLogMsg.hasSubMessages()) {
            groovyLogMsg.post();
            cleanInstance();
            return;
        }

        TweakedGroovyExcavator.getInstance().remove(innerMineralWrapper);
        TweakedGroovyExcavator.getInstance().add(mutableInnerMineralWrapper);
        cleanInstance();
    }

    public static class InnerWrapper {
        private final IMineralMix mineralMix;
        private int weight;

        public InnerWrapper(IMineralMix mineralMix, int weight) {
            this.mineralMix = mineralMix;
            this.weight = weight;
        }

        public InnerWrapper(ExcavatorHandler.MineralMix mineralMix, int weight) {
            this.mineralMix = (IMineralMix) mineralMix;
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }

        public ExcavatorHandler.MineralMix getMineralMix() {
            return (ExcavatorHandler.MineralMix) mineralMix;
        }

        private InnerWrapper cloneWrapper() {
            IMineralMix cloneMineralMix = (IMineralMix) new ExcavatorHandler.MineralMix(
                    mineralMix.getName(), mineralMix.getFailChance(),
                    mineralMix.getOres(), mineralMix.getChances());

            cloneMineralMix.setYield(mineralMix.getYield());
            cloneMineralMix.setPowerTier(mineralMix.getPowerTier());

            cloneMineralMix.setDimensionWhitelist(mineralMix.getDimensionWhitelist());
            cloneMineralMix.setDimensionBlacklist(mineralMix.getDimensionBlacklist());

            return new InnerWrapper(cloneMineralMix, weight);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GroovyMineralWrapper.InnerWrapper that = (GroovyMineralWrapper.InnerWrapper) o;
            return mineralMix.getName().equals(that.mineralMix.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(mineralMix.getName());
        }

    }

}
