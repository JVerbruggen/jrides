package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class MovingFallingBlockPlatformConfig extends BaseTriggerConfig{
    private int length;
    private int width;
    private Material material;

    private Vector3 rotation;
    private Vector3 locationFrom;
    private Vector3 locationTo;

    private double animationTimeTicks;

    public MovingFallingBlockPlatformConfig(int length, int width, Material material, Vector3 rotation, Vector3 locationFrom, Vector3 locationTo, double animationTimeTicks) {
        super(TriggerType.MOVING_FALLING_BLOCK_PLATFORM);
        this.length = length;
        this.width = width;
        this.material = material;
        this.rotation = rotation;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.animationTimeTicks = animationTimeTicks;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public Material getMaterial() {
        return material;
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public Vector3 getLocationFrom() {
        return locationFrom;
    }

    public Vector3 getLocationTo() {
        return locationTo;
    }

    public double getAnimationTimeTicks() {
        return animationTimeTicks;
    }

    public static MovingFallingBlockPlatformConfig fromConfigurationSection(ConfigurationSection configurationSection){
        int length = configurationSection.getInt("length");
        int width = configurationSection.getInt("width");
        Material material = Material.valueOf(configurationSection.getString("material"));
        Vector3 rotation = Vector3.fromDoubleList(configurationSection.getDoubleList("rotation"));
        Vector3 locationFrom = Vector3.fromDoubleList(configurationSection.getDoubleList("locationFrom"));
        Vector3 locationTo = Vector3.fromDoubleList(configurationSection.getDoubleList("locationTo"));
        double animationTimeTicks = configurationSection.getDouble("animationTimeTicks");

        return new MovingFallingBlockPlatformConfig(length, width, material, rotation, locationFrom, locationTo, animationTimeTicks);
    }
}
