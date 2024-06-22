package com.jverbruggen.jrides.config.coaster.objects.cart;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;

public class CartTypeSpecConfig extends BaseConfig {
    private final ModelConfig model;
    private final CartSeatsConfig seats;

    public CartTypeSpecConfig(ModelConfig model, CartSeatsConfig seats) {
        this.model = model;
        this.seats = seats;
    }

    public ModelConfig getModel() {
        return model;
    }

    public CartSeatsConfig getSeats() {
        return seats;
    }

    public static CartTypeSpecConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        ModelConfig model = ModelConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "model"));
        CartSeatsConfig seats = CartSeatsConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "seats"));
        return new CartTypeSpecConfig(model, seats);
    }
}
