package com.jverbruggen.jrides.config.coaster.objects.section.ranged;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class DriveStopDriveSectionSpecConfig extends BaseConfig {
    private final double engage;
    private final double driveSpeedIn;
    private final double driveSpeedOut;
    private final double acceleration;
    private final double deceleration;
    private final int waitTicks;

    public DriveStopDriveSectionSpecConfig(double engage, double driveSpeedIn, double driveSpeedOut, double acceleration, double deceleration, int waitTicks) {
        this.engage = engage;
        this.driveSpeedIn = driveSpeedIn;
        this.driveSpeedOut = driveSpeedOut;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.waitTicks = waitTicks;
    }

    public double getEngage() {
        return engage;
    }

    public double getDriveSpeedIn() {
        return driveSpeedIn;
    }

    public double getDriveSpeedOut() {
        return driveSpeedOut;
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

    public static DriveStopDriveSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double defaultDriveSpeed = getDouble(configurationSection, "driveSpeed", 1.0);

        double driveSpeedIn = getDouble(configurationSection, "driveSpeedIn", defaultDriveSpeed);
        double driveSpeedOut = getDouble(configurationSection, "driveSpeedOut", defaultDriveSpeed);
        double acceleration = getDouble(configurationSection, "acceleration", 1.0);
        double deceleration = getDouble(configurationSection, "deceleration", acceleration);
        double engage = getDouble(configurationSection, "engage", .5);
        int waitTicks = getInt(configurationSection, "waitIntervalTicks", 10);

        return new DriveStopDriveSectionSpecConfig(engage, driveSpeedIn, driveSpeedOut, acceleration, deceleration, waitTicks);
    }
}
