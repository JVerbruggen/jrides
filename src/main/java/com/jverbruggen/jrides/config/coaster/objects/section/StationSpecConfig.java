package com.jverbruggen.jrides.config.coaster.objects.section;

import org.bukkit.configuration.ConfigurationSection;

public class StationSpecConfig {
    private final String identifier;
    private final double engage;
    private final int minimumWaitIntervalSeconds;
    private final int maximumWaitIntervalSeconds;
    private final StationEffectsConfig stationEffectsConfig;

    private StationSpecConfig(String identifier, double engage, int minimumWaitIntervalSeconds, int maximumWaitIntervalSeconds, StationEffectsConfig stationEffectsConfig) {
        this.identifier = identifier;
        this.engage = engage;
        this.minimumWaitIntervalSeconds = minimumWaitIntervalSeconds;
        this.maximumWaitIntervalSeconds = maximumWaitIntervalSeconds;
        this.stationEffectsConfig = stationEffectsConfig;
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

    public StationEffectsConfig getStationEffectsConfig() {
        return stationEffectsConfig;
    }

    public static StationSpecConfig fromConfigurationSection(ConfigurationSection configurationSection, String sectionIdentifier) {
        double engage = configurationSection.getDouble("engage");
        int minimumWaitIntervalSeconds = configurationSection.getInt("minimumWaitIntervalSeconds");
        int maximumWaitIntervalSeconds = configurationSection.getInt("maximumWaitIntervalSeconds");
        StationEffectsConfig effects = StationEffectsConfig.fromConfigurationSection(configurationSection.getConfigurationSection("effects"));

        return new StationSpecConfig(sectionIdentifier, engage, minimumWaitIntervalSeconds, maximumWaitIntervalSeconds, effects);
    }
}
