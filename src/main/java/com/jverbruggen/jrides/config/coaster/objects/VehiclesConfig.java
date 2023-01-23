package com.jverbruggen.jrides.config.coaster.objects;

import org.bukkit.configuration.ConfigurationSection;

public class VehiclesConfig {
    private final int trains;
    private final int carts;

    public VehiclesConfig(int trains, int carts) {
        this.trains = trains;
        this.carts = carts;
    }

    public static VehiclesConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        int trains = configurationSection.getInt("trains");
        int carts = configurationSection.getInt("carts");

        return new VehiclesConfig(trains, carts);
    }
}
