package com.jverbruggen.jrides.config.flatride.structure.actuator.rotor;

import com.jverbruggen.jrides.animator.flatride.rotor.mode.ContinuousMode;
import com.jverbruggen.jrides.animator.flatride.rotor.mode.RotorActuatorMode;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class ContinuousRotorConfig extends BaseConfig implements RotorTypeConfig {
    private final Double lowerBound;
    private final Double upperBound;

    public ContinuousRotorConfig(Double lowerBound, Double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public RotorActuatorMode createActuatorMode() {
        return new ContinuousMode(lowerBound, upperBound);
    }

    public static ContinuousRotorConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        Double lowerBound = getDoubleObj(configurationSection, "lowerBound", null);
        Double upperBound = getDoubleObj(configurationSection, "upperBound", null);
        return new ContinuousRotorConfig(lowerBound, upperBound);
    }
}
