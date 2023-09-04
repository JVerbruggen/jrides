package com.jverbruggen.jrides.config.flatride.structure.actuator.linearactuator;

import com.jverbruggen.jrides.animator.flatride.linearactuator.mode.ActuatorMode;
import com.jverbruggen.jrides.animator.flatride.linearactuator.mode.LinearMode;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class LinearLinearActuatorConfig extends BaseConfig implements LinearActuatorTypeConfig {
    @Override
    public ActuatorMode createActuatorMode() {
        return new LinearMode();
    }

    public static LinearLinearActuatorConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        return new LinearLinearActuatorConfig();
    }
}
