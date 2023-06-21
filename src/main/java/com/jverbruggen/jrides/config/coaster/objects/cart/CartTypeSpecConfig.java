package com.jverbruggen.jrides.config.coaster.objects.cart;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;

public class CartTypeSpecConfig extends BaseConfig {
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

    public static CartTypeSpecConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        CartModelConfig model = CartModelConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "model"));
        CartSeatsConfig seats = CartSeatsConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "seats"));
        return new CartTypeSpecConfig(model, seats);
    }
}
