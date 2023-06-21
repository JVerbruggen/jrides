package com.jverbruggen.jrides.config.coaster.objects.cart;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemConfig;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CartModelConfig extends BaseConfig {
    private final ItemConfig item;
    private final Vector3 position;

    public CartModelConfig(ItemConfig item, Vector3 position) {
        this.item = item;
        this.position = position;
    }

    public ItemConfig getItem() {
        return item;
    }

    public Vector3 getPosition() {
        return position;
    }

    public static CartModelConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        ItemConfig item = ItemConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "item"));
        List<Double> position = getDoubleList(configurationSection, "position", List.of(0d,0d,0d));
        Vector3 vector = new Vector3(position.get(0), position.get(1), position.get(2));
        return new CartModelConfig(item, vector);
    }
}
