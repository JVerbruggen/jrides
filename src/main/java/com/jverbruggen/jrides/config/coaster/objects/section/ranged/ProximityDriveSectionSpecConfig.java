package com.jverbruggen.jrides.config.coaster.objects.section.ranged;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class ProximityDriveSectionSpecConfig extends BaseConfig {
    private final boolean canSpawn;
    private final double driveSpeed;
    private final double acceleration;
    private final double deceleration;
    private final int minTrainDistance;

    public ProximityDriveSectionSpecConfig(boolean canSpawn, double driveSpeed, double acceleration, double deceleration, int minTrainDistance) {
        this.canSpawn = canSpawn;
        this.driveSpeed = driveSpeed;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.minTrainDistance = minTrainDistance;
    }

    public boolean canSpawn() {
        return canSpawn;
    }

    public double getDriveSpeed() {
        return driveSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getDeceleration() {
        return deceleration;
    }

    public int getMinTrainDistance() {
        return minTrainDistance;
    }

    public static ProximityDriveSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        boolean canSpawn = getBoolean(configurationSection, "canSpawn", false);
        double driveSpeed = getDouble(configurationSection, "driveSpeed", 1.0);
        double acceleration = getDouble(configurationSection, "acceleration", 1.0);
        double deceleration = getDouble(configurationSection, "deceleration", acceleration);
        int minTrainDistance = getInt(configurationSection, "minTrainDistance", 50);

        return new ProximityDriveSectionSpecConfig(canSpawn, driveSpeed, acceleration, deceleration, minTrainDistance);
    }
}
