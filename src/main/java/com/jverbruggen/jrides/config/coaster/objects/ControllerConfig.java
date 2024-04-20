package com.jverbruggen.jrides.config.coaster.objects;

import com.jverbruggen.jrides.config.coaster.objects.controller.DualControllerSpecConfig;
import org.bukkit.configuration.ConfigurationSection;

public class ControllerConfig extends BaseConfig {
    public static final String CONTROLLER_DEFAULT = "default";
    public static final String CONTROLLER_ALTERNATE = "alternate";
    public static final String CONTROLLER_SIMULTANEOUS = "simultaneous";

    private String type;
    private DualControllerSpecConfig dualControllerSpecConfig;

    public ControllerConfig(String type, DualControllerSpecConfig dualControllerSpecConfig) {
        this.type = type;
        this.dualControllerSpecConfig = dualControllerSpecConfig;
    }

    public String getType() {
        return type;
    }

    public DualControllerSpecConfig getAlternateControllerSpecConfig() {
        return dualControllerSpecConfig;
    }

    public static ControllerConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        if(configurationSection == null) return null;

        String type = getString(configurationSection, "type", "default");
        DualControllerSpecConfig dualControllerSpecConfig = null;

        if(type.equalsIgnoreCase(CONTROLLER_ALTERNATE))
            dualControllerSpecConfig = DualControllerSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("spec"));
        else if(type.equalsIgnoreCase(CONTROLLER_SIMULTANEOUS))
            dualControllerSpecConfig = DualControllerSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("spec"));

        return new ControllerConfig(type, dualControllerSpecConfig);
    }
}

