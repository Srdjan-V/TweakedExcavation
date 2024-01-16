package io.github.srdjanv.tweakedexcavation.common.compat.grovyscript;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.helper.SimpleObjectStream;
import com.cleanroommc.groovyscript.helper.recipe.IRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralMix;
import io.github.srdjanv.tweakedexcavation.api.mixins.IMineralMixGetters;
import io.github.srdjanv.tweakedexcavation.common.CustomMineralBlocks;
import io.github.srdjanv.tweakedlib.api.powertier.PowerTier;
import net.minecraft.item.ItemStack;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class TweakedGroovyExcavator extends VirtualizedRegistry<TweakedGroovyExcavator.GroovyMineralWrapper> {

    @GroovyBlacklist
    public TweakedGroovyExcavator() {
        super("Excavator", "excavator", "Mineral", "mineral");
    }

    @Override
    @GroovyBlacklist
    public void onReload() {
        removeScripted().forEach((recipe) -> ExcavatorHandler.mineralList.remove(recipe.getMineralMix()));
        restoreFromBackup().forEach((recipe) -> ExcavatorHandler.mineralList.put(recipe.getMineralMix(), recipe.getWeight()));
    }

    @Override
    @GroovyBlacklist
    protected void initBackup() {
        this.backup = new HashSet<>();
    }

    @Override
    @GroovyBlacklist
    protected void initScripted() {
        this.scripted = new HashSet<>();
    }

    @Override
    @GroovyBlacklist
    public void addBackup(GroovyMineralWrapper recipe) {
        if (scripted.contains(recipe) || backup.contains(recipe)) return;
        backup.add(recipe);
    }

    @Override
    @GroovyBlacklist
    protected boolean compareRecipe(GroovyMineralWrapper recipe, GroovyMineralWrapper recipe2) {
        return recipe.equals(recipe2);
    }

    @Override
    @GroovyBlacklist
    public void afterScriptLoad() {
        CustomMineralBlocks.cleanCache();
        ExcavatorHandler.mineralList.forEach((mineralMix, integer) -> mineralMix.recalculateChances());
    }

    public void removeAll() {
        ExcavatorHandler.mineralList.forEach((reservoirType, integer) -> addBackup(new GroovyMineralWrapper(reservoirType, integer)));
        ExcavatorHandler.mineralList.clear();
    }

    public void remove(String name) {
        ExcavatorHandler.MineralMix removedMineral = null;
        for (Map.Entry<ExcavatorHandler.MineralMix, Integer> mineral : ExcavatorHandler.mineralList.entrySet()) {
            if (mineral.getKey().name.equalsIgnoreCase(name)) {
                addBackup(new GroovyMineralWrapper(mineral.getKey(), mineral.getValue()));
                removedMineral = mineral.getKey();
                break;
            }
        }

        if (removedMineral != null) {
            ExcavatorHandler.mineralList.remove(removedMineral);
        } else GroovyLog.msg("Error removing custom mineral with name: {}", name)
                .add("No minerals with that name exist").error().post();
    }

    public boolean remove(GroovyMineralWrapper mineral) {
        if (ExcavatorHandler.mineralList.containsKey(mineral.getMineralMix())) {
            ExcavatorHandler.mineralList.remove(mineral.getMineralMix());
            addBackup(mineral);
            GroovyLog.msg("Removed custom mineral with name: {}", mineral.getName()).info().post();
            return true;
        } else GroovyLog.msg("Error removing custom mineral with name: {}", mineral.getName())
                .add("That mineral does not exist").error().post();
        return false;
    }

    public SimpleObjectStream<GroovyMineralWrapper> getAll() {
        List<GroovyMineralWrapper> minerals = ExcavatorHandler.mineralList.entrySet()
                .stream().collect(ArrayList::new,
                        (list, map) -> list.add(new GroovyMineralWrapper(map.getKey(), map.getValue())),
                        ArrayList::addAll);
        var simpleMinerals = new SimpleObjectStream<>(minerals);
        simpleMinerals.setRemover(this::remove);
        return simpleMinerals;
    }

    public GroovyMineralWrapper get(String name) {
        for (Map.Entry<ExcavatorHandler.MineralMix, Integer> mineral : ExcavatorHandler.mineralList.entrySet()) {
            if (mineral.getKey().name.equalsIgnoreCase(name))
                return new GroovyMineralWrapper(mineral.getKey(), mineral.getValue());
        }

        GroovyLog.msg("Error getting custom mineral with name: {}", name).error().post();
        return null;
    }

    public void add(GroovyMineralWrapper mineral) {
        if (mineral != null) {
            if (ExcavatorHandler.mineralList.containsKey(mineral.getMineralMix())) {
                addBackup(get(mineral.getName()));
                ExcavatorHandler.mineralList.remove(mineral.getMineralMix());
                GroovyLog.msg("Updating mineral with name: {}", mineral.getName()).info().post();
            }
            ExcavatorHandler.mineralList.put(mineral.getMineralMix(), mineral.getWeight());
            scripted.add(mineral);
        } else GroovyLog.msg("Mineral to add is null").error().post();
    }

    public MineralBuilder recipeBuilder() {
        return new MineralBuilder();
    }

    public class MineralBuilder implements IRecipeBuilder<GroovyMineralWrapper> {

        protected String name;
        protected int weight;
        protected Float failChance;
        protected int powerTier;
        protected int oreYield;
        protected List<Integer> dimBlacklist;
        protected List<Integer> dimWhitelist;
        protected List<String> ores;
        protected List<BigDecimal> chances;

        public MineralBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MineralBuilder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public MineralBuilder ores(List<String> ores) {
            this.ores = ores;
            return this;
        }

        public MineralBuilder chances(List<BigDecimal> chances) {
            this.chances = chances;
            return this;
        }

        public MineralBuilder failChance(Float failChance) {
            this.failChance = failChance;
            return this;
        }

        public MineralBuilder powerTier(int powerTier) {
            this.powerTier = powerTier;
            return this;
        }

        public MineralBuilder powerTier(PowerTier powerTier) {
            this.powerTier = powerTier.hashCode();
            return this;
        }

        public MineralBuilder oreYield(int oreYield) {
            this.oreYield = oreYield;
            return this;
        }

        public MineralBuilder dimBlacklist(List<Integer> dimBlacklist) {
            this.dimBlacklist = dimBlacklist;
            return this;
        }

        public MineralBuilder dimWhitelist(List<Integer> dimWhitelist) {
            this.dimWhitelist = dimWhitelist;
            return this;
        }

        @Override
        public boolean validate() {
            GroovyLog.Msg msg = GroovyLog.msg("Error adding custom Mineral deposit").error();
            GroovyMineralValidator.validateGroovyMineral(msg, name, failChance, ores, chances, weight, powerTier, oreYield,
                    dimBlacklist, dimWhitelist);

            return !msg.postIfNotEmpty();
        }

        @Override
        public GroovyMineralWrapper register() {
            if (!validate()) return null;

            double[] doubleChances = chances.stream().mapToDouble(BigDecimal::doubleValue).toArray();
            float[] floatChances = new float[doubleChances.length];
            for (int i = 0; i < doubleChances.length; i++) floatChances[i] = (float) doubleChances[i];

            IMineralMix mix = (IMineralMix) new ExcavatorHandler.MineralMix(name, failChance, ores.toArray(new String[0]), floatChances);
            mix.setPowerTier(powerTier);

            if (oreYield != 0) mix.setYield(oreYield);

            if (dimBlacklist != null) {
                mix.setDimensionBlacklist(dimBlacklist.stream().mapToInt(i -> i).toArray());
            } else mix.setDimensionBlacklist(new int[0]);

            if (dimWhitelist != null) {
                mix.setDimensionWhitelist(dimWhitelist.stream().mapToInt(i -> i).toArray());
            } else mix.setDimensionWhitelist(new int[0]);

            GroovyMineralWrapper groovyMineralWrapper = new GroovyMineralWrapper(mix, weight);
            add(groovyMineralWrapper);
            return groovyMineralWrapper;
        }
    }

    public class GroovyMineralWrapper implements IMineralMixGetters {
        private final IMineralMix mineralMix;
        private final int weight;

        public int getWeight() {
            return weight;
        }

        public ExcavatorHandler.MineralMix getMineralMix() {
            return (ExcavatorHandler.MineralMix) mineralMix;
        }

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
            return mineralMix.getOres().clone();
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

        public MineralBuilder toBuilder() {
            var builder = recipeBuilder();
            builder.name(mineralMix.getName());
            builder.weight(weight);
            builder.failChance(mineralMix.getFailChance());
            builder.ores(Arrays.asList(mineralMix.getOres()));
            List<BigDecimal> chances = new ArrayList<>();
            for (float chance : mineralMix.getChances()) chances.add(new BigDecimal(chance));
            builder.chances(chances);
            builder.dimWhitelist(Arrays.stream(mineralMix.getDimensionWhitelist()).boxed().collect(Collectors.toList()));
            builder.dimBlacklist(Arrays.stream(mineralMix.getDimensionBlacklist()).boxed().collect(Collectors.toList()));
            builder.oreYield(mineralMix.getYield());
            builder.powerTier(mineralMix.getPowerTier());
            return builder;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GroovyMineralWrapper that = (GroovyMineralWrapper) o;
            return Objects.equals(mineralMix, that.mineralMix);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mineralMix);
        }
    }
}
