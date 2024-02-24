package com.jverbruggen.jrides.config.coaster.objects.section.ranged;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class ProximityDriveSectionSpecConfig extends BaseConfig {
    private final double driveSpeed;
    private final double acceleration;
    private final double deceleration;
    private final double minTrainDistance;

    public ProximityDriveSectionSpecConfig(double driveSpeed, double acceleration, double deceleration, double minTrainDistance) {
        this.driveSpeed = driveSpeed;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.minTrainDistance = minTrainDistance;
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

    public double getMinTrainDistance() {
        return minTrainDistance;
    }

    public static ProximityDriveSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double driveSpeed = getDouble(configurationSection, "driveSpeed", 1.0);
        double acceleration = getDouble(configurationSection, "acceleration", 1.0);
        double deceleration = getDouble(configurationSection, "deceleration", acceleration);
        double minTrainDistance = getDouble(configurationSection, "minTrainDistance", 1.0);

        return new ProximityDriveSectionSpecConfig(driveSpeed, acceleration, deceleration, minTrainDistance);
    }
}
