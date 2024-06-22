package com.jverbruggen.jrides.config.ride;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.stream.Collectors;

public class RidesConfig {
    private List<RidesConfigObject> rides;

    public RidesConfig(List<RidesConfigObject> rides) {
        this.rides = rides;
    }

    public List<RidesConfigObject> getRides() {
        return rides;
    }

    public static RidesConfig fromConfigurationSection(ConfigurationSection configurationSection){
        List<RidesConfigObject> rides = configurationSection.getKeys(false)
                .stream()
                .map(i -> RidesConfigObject.fromConfigurationSection(configurationSection.getConfigurationSection(i)))
                .collect(Collectors.toList());

        return new RidesConfig(rides);
    }
}
