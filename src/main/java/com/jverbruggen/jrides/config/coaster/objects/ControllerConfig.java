package com.jverbruggen.jrides.config.coaster.objects;

import com.jverbruggen.jrides.config.coaster.objects.controller.AlternateControllerSpecConfig;
import org.bukkit.configuration.ConfigurationSection;

public class ControllerConfig extends BaseConfig {
    private String type;
    private AlternateControllerSpecConfig alternateControllerSpecConfig;

    public ControllerConfig(String type, AlternateControllerSpecConfig alternateControllerSpecConfig) {
        this.alternateControllerSpecConfig = alternateControllerSpecConfig;
    }

    public String getType() {
        return type;
    }

    public AlternateControllerSpecConfig getAlternateControllerSpecConfig() {
        return alternateControllerSpecConfig;
    }

    public static ControllerConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        if(configurationSection == null) return null;

        String type = getString(configurationSection, "type");
        AlternateControllerSpecConfig alternateControllerSpecConfig = null;

        if(type.equalsIgnoreCase("alternate"))
            alternateControllerSpecConfig = AlternateControllerSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("spec"));

        return new ControllerConfig(type, alternateControllerSpecConfig);
    }
}

