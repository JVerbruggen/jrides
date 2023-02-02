package com.jverbruggen.jrides.config.coaster.objects.section;

import org.bukkit.configuration.ConfigurationSection;

public class DriveSectionSpecConfig {
    private final double driveSpeed;
    private final double acceleration;

    public DriveSectionSpecConfig(double driveSpeed, double acceleration) {
        this.driveSpeed = driveSpeed;
        this.acceleration = acceleration;
    }

    public double getDriveSpeed() {
        return driveSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public static DriveSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double driveSpeed = configurationSection.getDouble("driveSpeed");
        double acceleration = configurationSection.contains("acceleration") ? configurationSection.getDouble("acceleration") : 1.0;

        return new DriveSectionSpecConfig(driveSpeed, acceleration);
    }
}
