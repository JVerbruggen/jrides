package com.jverbruggen.jrides.config.coaster.objects.cart;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.stream.Collectors;

public class CartSeatsConfig extends BaseConfig {
    private List<Vector3> positions;

    public CartSeatsConfig(List<Vector3> positions) {
        this.positions = positions;
    }

    public List<Vector3> getPositions() {
        return positions;
    }

    public static CartSeatsConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        List<List<Double>> positions = getDoubleListList(configurationSection, "positions", List.of());
        List<Vector3> vectors = positions.stream()
                .map(Vector3::fromDoubleList)
                .collect(Collectors.toList());
        return new CartSeatsConfig(vectors);
    }
}
