package com.jverbruggen.jrides.config.coaster.objects.cart;

import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class CartModelConfig{
    private final CartModelItemConfig item;
    private final Vector3 position;

    public CartModelConfig(CartModelItemConfig item, Vector3 position) {
        this.item = item;
        this.position = position;
    }

    public CartModelItemConfig getItem() {
        return item;
    }

    public Vector3 getPosition() {
        return position;
    }

    public static CartModelConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        CartModelItemConfig item = CartModelItemConfig.fromConfigurationSection(configurationSection.getConfigurationSection("item"));
        List<Double> position = configurationSection.getDoubleList("position");
        Vector3 vector = new Vector3(position.get(0), position.get(1), position.get(2));
        return new CartModelConfig(item, vector);
    }
}
