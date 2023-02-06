package com.jverbruggen.jrides.config.coaster.objects.section;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import org.bukkit.configuration.ConfigurationSection;

public class StationSpecConfig extends BaseConfig {
    private final double engage;
    private final double driveSpeed;
    private final int minimumWaitIntervalSeconds;
    private final int maximumWaitIntervalSeconds;
    private final StationEffectsConfig stationEffectsConfig;
    private final PlayerLocation ejectLocation;

    private StationSpecConfig(double engage, double driveSpeed, int minimumWaitIntervalSeconds, int maximumWaitIntervalSeconds, StationEffectsConfig stationEffectsConfig, PlayerLocation ejectLocation) {
        this.engage = engage;
        this.driveSpeed = driveSpeed;
        this.minimumWaitIntervalSeconds = minimumWaitIntervalSeconds;
        this.maximumWaitIntervalSeconds = maximumWaitIntervalSeconds;
        this.stationEffectsConfig = stationEffectsConfig;
        this.ejectLocation = ejectLocation;
    }

    public double getEngage() {
        return engage;
    }

    public double getDriveSpeed() {
        return driveSpeed;
    }

    public int getMinimumWaitIntervalSeconds() {
        return minimumWaitIntervalSeconds;
    }

    public int getMaximumWaitIntervalSeconds() {
        return maximumWaitIntervalSeconds;
    }

    public PlayerLocation getEjectLocation() {
        return ejectLocation;
    }

    public StationEffectsConfig getStationEffectsConfig() {
        return stationEffectsConfig;
    }

    public static StationSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double engage = getDouble(configurationSection, "engage");
        double driveSpeed = getDouble(configurationSection, "driveSpeed");
        int minimumWaitIntervalSeconds = getInt(configurationSection, "minimumWaitIntervalSeconds");
        int maximumWaitIntervalSeconds = getInt(configurationSection, "maximumWaitIntervalSeconds");
        StationEffectsConfig effects = StationEffectsConfig.fromConfigurationSection(configurationSection.getConfigurationSection("effects"));
        PlayerLocation ejectLocation = PlayerLocation.fromDoubleList(configurationSection.getDoubleList("ejectLocation"));

        return new StationSpecConfig(engage, driveSpeed, minimumWaitIntervalSeconds, maximumWaitIntervalSeconds, effects, ejectLocation);
    }
}
