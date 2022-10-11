package srki2k.tweakedexcavation.common;

import srki2k.tweakedlib.api.powertier.PowerTierHandler;

public class DefaultMineralPower {

    public static DefaultMineralPower instance;

    public static DefaultMineralPower getInstance() {
        if (instance == null) {
            instance = new DefaultMineralPower();
        }
        return instance;
    }
    private DefaultMineralPower() {
        powerTier = PowerTierHandler.registerPowerTier(
                Configs.TPConfig.DefaultExcavatorPowerTiers.capacity,
                Configs.TPConfig.DefaultExcavatorPowerTiers.rft
        );
    }

    private final int powerTier;

    public int getPowerTier() {
        return powerTier;
    }

}
