package com.jverbruggen.jrides.config.coaster.objects.section;

import org.bukkit.configuration.ConfigurationSection;

public class StationSpecConfig {
    private final double engage;

    private StationSpecConfig(double engage) {
        this.engage = engage;
    }

    public double getEngage() {
        return engage;
    }

    public static StationSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double engage = configurationSection.getDouble("engage");

        return new StationSpecConfig(engage);
    }
}
