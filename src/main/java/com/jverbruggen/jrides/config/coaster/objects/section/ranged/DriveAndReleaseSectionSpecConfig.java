package com.jverbruggen.jrides.config.coaster.objects.section.ranged;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class DriveAndReleaseSectionSpecConfig extends BaseConfig {
    private final double engage;
    private final double driveSpeed;
    private final double acceleration;
    private final double deceleration;
    private final int waitTicks;

    public DriveAndReleaseSectionSpecConfig(double engage, double driveSpeed, double acceleration, double deceleration, int waitTicks) {
        this.engage = engage;
        this.driveSpeed = driveSpeed;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.waitTicks = waitTicks;
    }

    public double getEngage() {
        return engage;
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

    public int getWaitTicks() {
        return waitTicks;
    }

    public static DriveAndReleaseSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double driveSpeed = getDouble(configurationSection, "driveSpeed", 1.0);
        double acceleration = getDouble(configurationSection, "acceleration", 1.0);
        double deceleration = getDouble(configurationSection, "deceleration", acceleration);
        double engage = getDouble(configurationSection, "engage", .5);
        int waitTicks = getInt(configurationSection, "waitIntervalTicks", 1);

        return new DriveAndReleaseSectionSpecConfig(engage, driveSpeed, acceleration, deceleration, waitTicks);
    }
}
