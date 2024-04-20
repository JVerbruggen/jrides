package com.jverbruggen.jrides.config.coaster.objects.controller;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class DualControllerSpecConfig extends BaseConfig {
    private String stationLeft;
    private String stationRight;

    public DualControllerSpecConfig(String stationLeft, String stationRight) {
        this.stationLeft = stationLeft;
        this.stationRight = stationRight;
    }

    public String getStationLeft() {
        return stationLeft;
    }

    public String getStationRight() {
        return stationRight;
    }

    public static DualControllerSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String stationLeft = getString(configurationSection, "stationLeft");
        String stationRight = getString(configurationSection, "stationRight");

        return new DualControllerSpecConfig(stationLeft, stationRight);
    }
}
