package com.jverbruggen.jrides.config.coaster.objects;

import org.bukkit.configuration.ConfigurationSection;

public class VehiclesConfig {
    private final int trains;
    private final int carts;
    private final int cartDistance;

    public VehiclesConfig(int trains, int carts, int cartDistance) {
        this.trains = trains;
        this.carts = carts;
        this.cartDistance = cartDistance;
    }

    public int getCarts() {
        return carts;
    }

    public int getTrains() {
        return trains;
    }

    public int getCartDistance() {
        return cartDistance;
    }

    public static VehiclesConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        int trains = configurationSection.getInt("trains");
        int carts = configurationSection.getInt("carts");
        int cartDistance = configurationSection.getInt("cartDistance");

        return new VehiclesConfig(trains, carts, cartDistance);
    }
}
