package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class ArmorstandAtoBMovementConfig extends BaseConfig {
    private final String identifier;
    private final Material material;
    private final int damage;
    private final boolean unbreakable;

    private final Vector3 locationFrom;
    private final Vector3 locationTo;
    private final Vector3 rotationFrom;
    private final Vector3 rotationTo;
    private final int animationTimeTicks;

    private final boolean locationHasDelta;
    private final boolean rotationHasDelta;

    public ArmorstandAtoBMovementConfig(String identifier, Material material, int damage, boolean unbreakable, Vector3 locationFrom, Vector3 locationTo, Vector3 rotationFrom, Vector3 rotationTo, int animationTimeTicks) {
        this.identifier = identifier;
        this.material = material;
        this.damage = damage;
        this.unbreakable = unbreakable;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.rotationFrom = rotationFrom;
        this.rotationTo = rotationTo;
        this.animationTimeTicks = animationTimeTicks;
        this.locationHasDelta = locationFrom != null && locationTo != null && locationTo.distanceSquared(locationFrom) > 0.01;
        this.rotationHasDelta = rotationFrom != null && rotationTo != null && rotationTo.distanceSquared(rotationFrom) > 0.01;
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

    public Vector3 getRotationFrom() {
        return rotationFrom;
    }

    public Vector3 getRotationTo() {
        return rotationTo;
    }

    public int getAnimationTimeTicks() {
        return animationTimeTicks;
    }

    public boolean isLocationHasDelta() {
        return locationHasDelta;
    }

    public boolean isRotationHasDelta() {
        return rotationHasDelta;
    }

    public static ArmorstandAtoBMovementConfig fromConfigurationSection(String identifier, ConfigurationSection configurationSection){
        ConfigurationSection item = configurationSection.getConfigurationSection("item");

        Material material = Material.valueOf(getString(item, "material"));
        int damage = getInt(item, "damage");
        boolean unbreakable = getBoolean(item, "unbreakable");

        Vector3 locationFrom = Vector3.fromDoubleList(getDoubleList(configurationSection, "locationFrom", null));
        Vector3 locationTo = Vector3.fromDoubleList(getDoubleList(configurationSection, "locationTo", null));
        Vector3 rotationFrom = Vector3.fromDoubleList(getDoubleList(configurationSection, "rotationFrom", null));
        Vector3 rotationTo = Vector3.fromDoubleList(getDoubleList(configurationSection, "rotationTo", null));
        int animationTimeTicks = getInt(configurationSection, "animationTimeTicks");

        return new ArmorstandAtoBMovementConfig(identifier, material, damage, unbreakable, locationFrom, locationTo, rotationFrom, rotationTo, animationTimeTicks);
    }
}
