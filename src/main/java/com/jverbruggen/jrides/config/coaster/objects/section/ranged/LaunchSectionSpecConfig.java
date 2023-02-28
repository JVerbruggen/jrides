package com.jverbruggen.jrides.config.coaster.objects.section.ranged;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.checkerframework.checker.nullness.qual.NonNull;

public class LaunchSectionSpecConfig extends BaseConfig {
    private final double driveSpeed;
    private final double acceleration;
    private final double deceleration;
    private final double engage;
    private final int waitTicks;

    private final double launchAcceleration;
    private final double launchMaxSpeed;
    private final LaunchEffectsConfig launchEffectsConfig;

    public LaunchSectionSpecConfig(double driveSpeed, double acceleration, double deceleration, double engage, int waitTicks, double launchAcceleration, double launchMaxSpeed, LaunchEffectsConfig launchEffectsConfig) {
        this.driveSpeed = driveSpeed;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.engage = engage;
        this.waitTicks = waitTicks;
        this.launchAcceleration = launchAcceleration;
        this.launchMaxSpeed = launchMaxSpeed;
        this.launchEffectsConfig = launchEffectsConfig;
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

    public double getEngage() {
        return engage;
    }

    public int getWaitTicks() {
        return waitTicks;
    }

    public double getLaunchAcceleration() {
        return launchAcceleration;
    }

    public double getLaunchMaxSpeed() {
        return launchMaxSpeed;
    }

    @NonNull
    public LaunchEffectsConfig getLaunchEffectsConfig() {
        return launchEffectsConfig;
    }

    public static LaunchSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double engage = getDouble(configurationSection, "engage", 0.0);
        double driveSpeed = getDouble(configurationSection, "driveSpeed", 1.0);
        double acceleration = getDouble(configurationSection, "acceleration", 0.1);
        double deceleration = getDouble(configurationSection, "deceleration", acceleration);
        int waitTicks = getInt(configurationSection, "waitIntervalTicks", -1);
        double launchAcceleration = getDouble(configurationSection, "launchAcceleration", 0.1);
        double launchMaxSpeed = getDouble(configurationSection, "launchSpeed", 5.0);
        LaunchEffectsConfig launchEffectsConfig = LaunchEffectsConfig.fromConfigurationSection(configurationSection.getConfigurationSection("effects"));

        return new LaunchSectionSpecConfig(driveSpeed, acceleration, deceleration, engage, waitTicks, launchAcceleration, launchMaxSpeed, launchEffectsConfig);
    }
}
