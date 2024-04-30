package com.jverbruggen.jrides.config.coaster.objects.cart;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;

public class CartTypeSpecConfig extends BaseConfig {
    private final ModelConfig model;
    private final CartSeatsConfig seats;
    private final int wheelDistance;

    public CartTypeSpecConfig(ModelConfig model, CartSeatsConfig seats, int wheelDistance) {
        this.model = model;
        this.seats = seats;
        this.wheelDistance = wheelDistance;
    }

    public ModelConfig getModel() {
        return model;
    }

    public CartSeatsConfig getSeats() {
        return seats;
    }

    public int getWheelDistance() {
        return wheelDistance;
    }

    public static CartTypeSpecConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        ModelConfig model = ModelConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "model"));
        CartSeatsConfig seats = CartSeatsConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "seats"));
        if(configurationSection != null) {
            int wheelDistance = configurationSection.getInt("wheelDistance", 0);
            return new CartTypeSpecConfig(model, seats, wheelDistance);
        }
        return new CartTypeSpecConfig(model, seats, 0);
    }
}
