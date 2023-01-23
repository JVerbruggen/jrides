package com.jverbruggen.jrides.config.coaster.objects;

import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.stream.Collectors;

public class CartSpecConfig {
    private final CartTypeSpecConfig _default;
    private final CartTypeSpecConfig head;
    private final CartTypeSpecConfig tail;

    public CartSpecConfig(CartTypeSpecConfig _default, CartTypeSpecConfig head, CartTypeSpecConfig tail) {
        this._default = _default;
        this.head = head;
        this.tail = tail;
    }

    public CartTypeSpecConfig getDefault() {
        return _default;
    }

    public CartTypeSpecConfig getHead() {
        return head;
    }

    public CartTypeSpecConfig getTail() {
        return tail;
    }

    public static CartSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        CartTypeSpecConfig _default = CartTypeSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("default"));
        CartTypeSpecConfig head = null;
        CartTypeSpecConfig tail = null;

        if(configurationSection.contains("head")){
            head = CartTypeSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("head"));
        }
        if(configurationSection.contains("tail")){
            tail = CartTypeSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("tail"));
        }

        return new CartSpecConfig(_default, head, tail);
    }
}

class CartTypeSpecConfig{
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

class CartModelConfig{
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

class CartModelItemConfig{
    private final String material;
    private final int damage;
    private final boolean unbreakable;

    public CartModelItemConfig(String material, int damage, boolean unbreakable) {
        this.material = material;
        this.damage = damage;
        this.unbreakable = unbreakable;
    }

    public String getMaterial() {
        return material;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public static CartModelItemConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String material = configurationSection.getString("material");
        int damage = configurationSection.getInt("damage");
        boolean unbreakable = configurationSection.getBoolean("unbreakable");
        return new CartModelItemConfig(material, damage, unbreakable);
    }
}

class CartSeatsConfig{
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