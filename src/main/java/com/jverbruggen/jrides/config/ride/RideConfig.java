package com.jverbruggen.jrides.config.ride;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.stream.Collectors;

public class RideConfig {
    private List<RideConfigObject> rides;

    public RideConfig(List<RideConfigObject> rides) {
        this.rides = rides;
    }

    public List<RideConfigObject> getRides() {
        return rides;
    }

    public static RideConfig fromConfigurationSection(ConfigurationSection configurationSection){
        List<RideConfigObject> rides = configurationSection.getKeys(false)
                .stream()
                .map(i -> RideConfigObject.fromConfigurationSection(configurationSection.getConfigurationSection(i)))
                .collect(Collectors.toList());

        return new RideConfig(rides);
    }
}
