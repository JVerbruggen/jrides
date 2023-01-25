package com.jverbruggen.jrides.config.coaster.objects.cart;

import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.stream.Collectors;

public class CartSeatsConfig{
    private List<Vector3> positions;

    public CartSeatsConfig(List<Vector3> positions) {
        this.positions = positions;
    }

    public List<Vector3> getPositions() {
        return positions;
    }

    public static CartSeatsConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        List<List<Double>> positions = (List<List<Double>>) configurationSection.getList("positions");
        List<Vector3> vectors = positions.stream().map(p -> new Vector3(p.get(0), p.get(1), p.get(2))).collect(Collectors.toList());
        return new CartSeatsConfig(vectors);
    }
}
