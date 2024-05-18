package com.jverbruggen.jrides.config.coaster.objects.section.ranged;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.checkerframework.checker.nullness.qual.NonNull;

public class LaunchSectionSpecConfig extends BaseConfig {
    private final boolean canSpawn;
    private final double driveSpeed;
    private final double acceleration;
    private final double deceleration;
    private final double engage;
    private final int waitTicks;
    private final boolean forwardsLaunch;

    private final double launchAcceleration;
    private final double launchSpeed;
    private final double launchSpeedBackward;
    private final LaunchEffectsConfig launchEffectsConfig;

    public LaunchSectionSpecConfig(boolean canSpawn, double driveSpeed, double acceleration, double deceleration, double engage, int waitTicks, boolean forwardsLaunch, double launchAcceleration, double launchSpeed, double launchSpeedBackward, LaunchEffectsConfig launchEffectsConfig) {
        this.canSpawn = canSpawn;
        this.driveSpeed = driveSpeed;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.engage = engage;
        this.waitTicks = waitTicks;
        this.forwardsLaunch = forwardsLaunch;
        this.launchAcceleration = launchAcceleration;
        this.launchSpeed = launchSpeed;
        this.launchSpeedBackward = launchSpeedBackward;
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

    public boolean isForwardsLaunch() {
        return forwardsLaunch;
    }

    public double getLaunchAcceleration() {
        return launchAcceleration;
    }

    public double getLaunchSpeed() {
        return launchSpeed;
    }

    public double getLaunchSpeedBackward() {
        return launchSpeedBackward;
    }

    public boolean canSpawn() {
        return canSpawn;
    }

    @NonNull
    public LaunchEffectsConfig getLaunchEffectsConfig() {
        return launchEffectsConfig;
    }

    public static LaunchSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        boolean canSpawn = getBoolean(configurationSection, "canSpawn", false);
        double engage = getDouble(configurationSection, "engage", 0.0);
        double driveSpeed = getDouble(configurationSection, "driveSpeed", 1.0);
        double acceleration = getDouble(configurationSection, "acceleration", 0.1);
        double deceleration = getDouble(configurationSection, "deceleration", acceleration);
        int waitTicks = getInt(configurationSection, "waitIntervalTicks", -1);
        double launchAcceleration = getDouble(configurationSection, "launchAcceleration", 0.1);
        double launchSpeed = getDouble(configurationSection, "launchSpeed", 5.0);
        double launchSpeedBackward = getDouble(configurationSection, "launchSpeedBackward", launchSpeed);
        LaunchEffectsConfig launchEffectsConfig = LaunchEffectsConfig.fromConfigurationSection(configurationSection.getConfigurationSection("effects"));
        boolean forwardsLaunch = getBoolean(configurationSection, "forwardsLaunch", true);

        if(launchSpeed <= 0)
            throw new RuntimeException("launchSpeed should be bigger than 0");
        if(launchSpeedBackward <= 0)
            throw new RuntimeException("launchSpeedBackward should be bigger than 0");

        return new LaunchSectionSpecConfig(canSpawn, driveSpeed, acceleration, deceleration, engage, waitTicks, forwardsLaunch, launchAcceleration, launchSpeed, launchSpeedBackward, launchEffectsConfig);
    }
}
