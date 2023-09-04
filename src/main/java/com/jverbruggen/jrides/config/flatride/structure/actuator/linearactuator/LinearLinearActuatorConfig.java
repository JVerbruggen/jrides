package com.jverbruggen.jrides.config.flatride.structure.actuator.linearactuator;

import com.jverbruggen.jrides.animator.flatride.linearactuator.mode.ActuatorMode;
import com.jverbruggen.jrides.animator.flatride.linearactuator.mode.LinearMode;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class LinearLinearActuatorConfig extends BaseConfig implements LinearActuatorTypeConfig {
    private final Double lowerBound;
    private final Double upperBound;

    public LinearLinearActuatorConfig(Double lowerBound, Double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public ActuatorMode createActuatorMode() {
        return new LinearMode(lowerBound, upperBound);
    }

    public static LinearLinearActuatorConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        Double lowerBound = getDoubleObj(configurationSection, "lowerBound", null);
        Double upperBound = getDoubleObj(configurationSection, "upperBound", null);
        return new LinearLinearActuatorConfig(lowerBound, upperBound);
    }
}
