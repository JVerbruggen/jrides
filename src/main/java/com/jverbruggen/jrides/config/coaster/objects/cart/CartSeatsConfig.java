package com.jverbruggen.jrides.config.coaster.objects.cart;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.math.Vector3PlusYaw;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.stream.Collectors;

public class CartSeatsConfig extends BaseConfig {
    private List<Vector3PlusYaw> positions;

    public CartSeatsConfig(List<Vector3PlusYaw> positions) {
        this.positions = positions;
    }

    public List<Vector3PlusYaw> getPositions() {
        return positions;
    }

    public static CartSeatsConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        List<List<Double>> positions = getDoubleListList(configurationSection, "positions", List.of());
        List<Vector3PlusYaw> vectors = positions.stream()
                .map(Vector3PlusYaw::fromDoubleList)
                .collect(Collectors.toList());
        return new CartSeatsConfig(vectors);
    }
}
