package com.jverbruggen.jrides.config.flatride.structure.actuator.linearactuator;

import com.jverbruggen.jrides.animator.flatride.linearactuator.mode.ActuatorMode;
import com.jverbruggen.jrides.animator.flatride.linearactuator.mode.ContinuousMode;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class ContinuousLinearActuatorConfig extends BaseConfig implements LinearActuatorTypeConfig {
    private final Double lowerBound;
    private final Double upperBound;

    public ContinuousLinearActuatorConfig(Double lowerBound, Double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public ActuatorMode createActuatorMode() {
        return new ContinuousMode(lowerBound, upperBound);
    }

    public static ContinuousLinearActuatorConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        Double lowerBound = getDoubleObj(configurationSection, "lowerBound", null);
        Double upperBound = getDoubleObj(configurationSection, "upperBound", null);
        return new ContinuousLinearActuatorConfig(lowerBound, upperBound);
    }
}
