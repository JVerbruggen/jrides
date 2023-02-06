package com.jverbruggen.jrides.config.coaster.objects.section;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class DriveSectionSpecConfig extends BaseConfig {
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
        double driveSpeed = getDouble(configurationSection, "driveSpeed");
        double acceleration = getDouble(configurationSection, "acceleration", 1.0);

        return new DriveSectionSpecConfig(driveSpeed, acceleration);
    }
}
