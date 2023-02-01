package com.jverbruggen.jrides.config.coaster.objects.section;

import org.bukkit.configuration.ConfigurationSection;

public class DriveSectionSpecConfig {
    private final double driveSpeed;

    public DriveSectionSpecConfig(double driveSpeed) {
        this.driveSpeed = driveSpeed;
    }

    public double getDriveSpeed() {
        return driveSpeed;
    }

    public static DriveSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double driveSpeed = configurationSection.getDouble("driveSpeed");

        return new DriveSectionSpecConfig(driveSpeed);
    }
}
