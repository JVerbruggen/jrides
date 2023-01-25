package com.jverbruggen.jrides.config.coaster.objects.cart;

import org.bukkit.configuration.ConfigurationSection;

public class CartTypeSpecConfig{
    private final CartModelConfig model;
    private final CartSeatsConfig seats;

    public CartTypeSpecConfig(CartModelConfig model, CartSeatsConfig seats) {
        this.model = model;
        this.seats = seats;
    }

    public CartModelConfig getModel() {
        return model;
    }

    public CartSeatsConfig getSeats() {
        return seats;
    }

    public static CartTypeSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        CartModelConfig model = CartModelConfig.fromConfigurationSection(configurationSection.getConfigurationSection("model"));
        CartSeatsConfig seats = CartSeatsConfig.fromConfigurationSection(configurationSection.getConfigurationSection("seats"));
        return new CartTypeSpecConfig(model, seats);
    }
}
