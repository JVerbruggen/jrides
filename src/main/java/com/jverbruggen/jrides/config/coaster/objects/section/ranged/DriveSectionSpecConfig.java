package com.jverbruggen.jrides.config.coaster.objects.section.ranged;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class DriveSectionSpecConfig extends BaseConfig {
    private final boolean ignoreDirection;
    private final double driveSpeed;
    private final double acceleration;
    private final double deceleration;

    public DriveSectionSpecConfig(boolean ignoreDirection, double driveSpeed, double acceleration, double deceleration) {
        this.ignoreDirection = ignoreDirection;
        this.driveSpeed = driveSpeed;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
    }

    public boolean isIgnoreDirection() {
        return ignoreDirection;
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

    public static DriveSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        boolean ignoreDirection = getBoolean(configurationSection, "ignoreDirection",  false);
        double driveSpeed = getDouble(configurationSection, "driveSpeed", 1.0);
        double acceleration = getDouble(configurationSection, "acceleration", 1.0);
        double deceleration = getDouble(configurationSection, "deceleration", acceleration);

        return new DriveSectionSpecConfig(ignoreDirection, driveSpeed, acceleration, deceleration);
    }
}
