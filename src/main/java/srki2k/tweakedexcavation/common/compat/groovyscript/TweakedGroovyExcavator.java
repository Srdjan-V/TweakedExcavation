package srki2k.tweakedexcavation.common.compat.groovyscript;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.helper.recipe.IRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import srki2k.tweakedexcavation.api.ihelpers.IMineralMix;
import srki2k.tweakedexcavation.util.groovy.GroovyMineralValidator;
import srki2k.tweakedexcavation.util.groovy.GroovyMineralWrapper;
import srki2k.tweakedlib.api.powertier.PowerTier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class TweakedGroovyExcavator extends VirtualizedRegistry<GroovyMineralWrapper> {

    protected static TweakedGroovyExcavator instance;

    @GroovyBlacklist
    static TweakedGroovyExcavator init() {
        return instance = new TweakedGroovyExcavator();
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

        if (removedMineral == null) {
            GroovyLog.msg("Error removing custom mineral with name: {}", name).error()
                    .add("No minerals exist").post();
            return;
        }

        ExcavatorHandler.mineralList.remove(removedMineral);
    }

    public void remove(GroovyMineralWrapper mineral) {
        if (ExcavatorHandler.mineralList.containsKey(mineral.getMineralMix())) {
            ExcavatorHandler.mineralList.remove(mineral.getMineralMix());
            addBackup(mineral);
            GroovyLog.msg("Removed custom mineral with name: {}", mineral.getName()).info().post();
            return;
        }
        GroovyLog.msg("Error removing custom mineral with name: {}", mineral.getName()).error()
                .add("That mineral dos not exist").post();
    }

    public List<GroovyMineralWrapper> getAll() {
        List<GroovyMineralWrapper> minerals = ExcavatorHandler.mineralList.entrySet()
                .stream().collect(ArrayList::new,
                        (list, map) -> list.add(new GroovyMineralWrapper(map.getKey(), map.getValue())),
                        ArrayList::addAll);

        if (minerals.isEmpty()) {
            GroovyLog.msg("Error getting all custom mineral").error()
                    .add("No minerals exist").post();
        }

        return minerals;
    }

    public GroovyMineralWrapper get(String name) {
        for (Map.Entry<ExcavatorHandler.MineralMix, Integer> mineral : ExcavatorHandler.mineralList.entrySet()) {
            if (mineral.getKey().name.equalsIgnoreCase(name)) {
                return new GroovyMineralWrapper(mineral.getKey(), mineral.getValue());
            }
        }

        GroovyLog.msg("Error getting custom mineral with name: {}", name).error().post();
        return null;
    }

    public void add(GroovyMineralWrapper mineral) {
        if (mineral != null) {
            if (scripted.add(mineral)) {
                ExcavatorHandler.mineralList.put(mineral.getMineralMix(), mineral.getWeight());
                return;
            }
            GroovyLog.msg("Registered custom mineral with name: {} is already registered", mineral.getName()).error().post();
        }
    }

    public MineralBuilder recipeBuilder() {
        return new MineralBuilder();
    }

    public static class MineralBuilder implements IRecipeBuilder<GroovyMineralWrapper> {

        protected String name;
        protected int weight;
        protected Float failChance;
        protected int powerTier;
        protected int oreYield;
        protected List<Integer> dimBlacklist;
        protected List<Integer> dimWhitelist;
        protected List<String> ores;
        protected List<Float> chances;


        public MineralBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MineralBuilder weight(int weight) {
            this.weight = weight;
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
            if (validate()) {
                double[] doubleChances = chances.stream().mapToDouble(f -> f).toArray();
                float[] floatChances = new float[doubleChances.length];
                for (int i = 0; i < doubleChances.length; i++) {
                    floatChances[i] = (float) doubleChances[i];
                }

                IMineralMix mix = (IMineralMix) new ExcavatorHandler.MineralMix(name, failChance, ores.toArray(new String[0]), floatChances);
                mix.setPowerTier(powerTier);

                if (oreYield != 0) {
                    mix.setYield(oreYield);
                }

                if (dimBlacklist != null) {
                    mix.setDimensionBlacklist(dimBlacklist.stream().mapToInt(i -> i).toArray());
                } else {
                    mix.setDimensionBlacklist(new int[0]);
                }

                if (dimWhitelist != null) {
                    mix.setDimensionWhitelist(dimWhitelist.stream().mapToInt(i -> i).toArray());
                } else {
                    mix.setDimensionWhitelist(new int[0]);
                }

                GroovyMineralWrapper groovyMineralWrapper = new GroovyMineralWrapper(mix, weight);
                instance.add(groovyMineralWrapper);
                return groovyMineralWrapper;
            }

            return null;
        }

    }
}
