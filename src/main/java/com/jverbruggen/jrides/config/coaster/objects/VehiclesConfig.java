package com.jverbruggen.jrides.config.coaster.objects;

import org.bukkit.configuration.ConfigurationSection;

public class VehiclesConfig extends BaseConfig {
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
        int trains = getInt(configurationSection, "trains", 1);
        int carts = getInt(configurationSection, "carts", 1);
        int cartDistance = getInt(configurationSection, "cartDistance", 20);

        return new VehiclesConfig(trains, carts, cartDistance);
    }
}
