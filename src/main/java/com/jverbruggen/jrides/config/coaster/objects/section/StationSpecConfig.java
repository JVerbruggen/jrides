package com.jverbruggen.jrides.config.coaster.objects.section;

import org.bukkit.configuration.ConfigurationSection;

public class StationSpecConfig {
    private final double engage;
    private final String identifier;

    private StationSpecConfig(String identifier, double engage) {
        this.identifier = identifier;
        this.engage = engage;
    }

    public String getIdentifier() {
        return identifier;
    }

    public double getEngage() {
        return engage;
    }

    public static StationSpecConfig fromConfigurationSection(ConfigurationSection configurationSection, String sectionIdentifier) {
        double engage = configurationSection.getDouble("engage");

        return new StationSpecConfig(sectionIdentifier, engage);
    }
}
