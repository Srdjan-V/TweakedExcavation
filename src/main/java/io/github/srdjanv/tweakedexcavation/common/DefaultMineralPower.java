package io.github.srdjanv.tweakedexcavation.common;

import io.github.srdjanv.tweakedlib.api.powertier.PowerTierHandler;

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
                Configs.TEConfig.DefaultExcavatorPowerTiers.capacity,
                Configs.TEConfig.DefaultExcavatorPowerTiers.rft
        );
    }

    private final int powerTier;

    public int getPowerTier() {
        return powerTier;
    }

}
