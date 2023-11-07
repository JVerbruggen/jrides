package com.jverbruggen.jrides.config.ride;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.stream.Collectors;

public class RideCounterMapConfigs extends BaseConfig {
    private final Map<String, RideCounterMapConfig> rideCounterMapConfigs;

    public RideCounterMapConfigs(Map<String, RideCounterMapConfig> rideCounterMapConfigs) {
        this.rideCounterMapConfigs = rideCounterMapConfigs;
    }

    public RideCounterMapConfigs() {
        this(null);
    }

    public Map<String, RideCounterMapConfig> getRideCounterMapConfigs() {
        return rideCounterMapConfigs;
    }

    public static RideCounterMapConfigs fromConfigurationSection(String rideIdentifier, ConfigurationSection configurationSection) {
        if(configurationSection == null) return new RideCounterMapConfigs();

        Map<String, RideCounterMapConfig> rideCounterMapConfigs = configurationSection.getKeys(false).stream().collect(
                Collectors.toMap(
                        key -> key,
                        key -> RideCounterMapConfig.fromConfigurationSection(rideIdentifier, configurationSection.getConfigurationSection(key))
                )
        );

        return new RideCounterMapConfigs(rideCounterMapConfigs);
    }


}