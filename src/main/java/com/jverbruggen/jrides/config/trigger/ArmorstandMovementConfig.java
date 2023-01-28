package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class ArmorstandMovementConfig{
    private String identifier;
    private Material material;
    private int damage;
    private boolean unbreakable;

    private Vector3 locationFrom;
    private Vector3 locationTo;
    private int animationTimeTicks;

    public ArmorstandMovementConfig(String identifier, Material material, int damage, boolean unbreakable, Vector3 locationFrom, Vector3 locationTo, int animationTimeTicks) {
        this.identifier = identifier;
        this.material = material;
        this.damage = damage;
        this.unbreakable = unbreakable;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.animationTimeTicks = animationTimeTicks;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Material getMaterial() {
        return material;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public Vector3 getLocationFrom() {
        return locationFrom;
    }

    public Vector3 getLocationTo() {
        return locationTo;
    }

    public int getAnimationTimeTicks() {
        return animationTimeTicks;
    }

    public static ArmorstandMovementConfig fromConfigurationSection(String identifier, ConfigurationSection configurationSection){
        ConfigurationSection item = configurationSection.getConfigurationSection("item");

        Material material = Material.valueOf(item.getString("material"));
        int damage = item.getInt("damage");
        boolean unbreakable = item.getBoolean("unbreakable");

        Vector3 locationFrom = Vector3.fromDoubleList(configurationSection.getDoubleList("locationFrom"));
        Vector3 locationTo = Vector3.fromDoubleList(configurationSection.getDoubleList("locationTo"));
        int animationTimeTicks = configurationSection.getInt("animationTimeTicks");

        return new ArmorstandMovementConfig(identifier, material, damage, unbreakable, locationFrom, locationTo, animationTimeTicks);
    }
}
