package com.jverbruggen.jrides.config.coaster.objects.section;

import org.bukkit.configuration.ConfigurationSection;

public class StationSpecConfig {
    private final String identifier;
    private final double engage;
    private final int minimumWaitIntervalSeconds;
    private final int maximumWaitIntervalSeconds;

    private StationSpecConfig(String identifier, double engage, int minimumWaitIntervalSeconds, int maximumWaitIntervalSeconds) {
        this.identifier = identifier;
        this.engage = engage;
        this.minimumWaitIntervalSeconds = minimumWaitIntervalSeconds;
        this.maximumWaitIntervalSeconds = maximumWaitIntervalSeconds;
    }

    public String getIdentifier() {
        return identifier;
    }

    public double getEngage() {
        return engage;
    }

    public int getMinimumWaitIntervalSeconds() {
        return minimumWaitIntervalSeconds;
    }

    public int getMaximumWaitIntervalSeconds() {
        return maximumWaitIntervalSeconds;
    }

    public static StationSpecConfig fromConfigurationSection(ConfigurationSection configurationSection, String sectionIdentifier) {
        double engage = configurationSection.getDouble("engage");
        int minimumWaitIntervalSeconds = configurationSection.getInt("minimumWaitIntervalSeconds");
        int maximumWaitIntervalSeconds = configurationSection.getInt("maximumWaitIntervalSeconds");

        return new StationSpecConfig(sectionIdentifier, engage, minimumWaitIntervalSeconds, maximumWaitIntervalSeconds);
    }
}
