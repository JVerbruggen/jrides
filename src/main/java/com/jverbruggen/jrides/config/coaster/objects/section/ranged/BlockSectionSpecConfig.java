package com.jverbruggen.jrides.config.coaster.objects.section.ranged;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class BlockSectionSpecConfig extends BaseConfig {
    private final double engage;
    private final double driveSpeed;
    private final boolean canSpawn;
    private final double acceleration;
    private final double deceleration;
    private final int minWaitTicks;

    private BlockSectionSpecConfig(double engage, double driveSpeed, boolean canSpawn, double acceleration, double deceleration, int minWaitTicks) {
        this.engage = engage;
        this.driveSpeed = driveSpeed;
        this.canSpawn = canSpawn;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.minWaitTicks = minWaitTicks;
    }

    public double getEngage() {
        return engage;
    }

    public double getDriveSpeed() {
        return driveSpeed;
    }

    public boolean canSpawn() {
        return canSpawn;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getDeceleration() {
        return deceleration;
    }

    public int getMinWaitTicks() {
        return minWaitTicks;
    }

    public static BlockSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double engage = getDouble(configurationSection, "engage", 0.01);
        double driveSpeed = getDouble(configurationSection, "driveSpeed", 1.0);
        boolean canSpawn = getBoolean(configurationSection, "canSpawn", false);
        double acceleration = getDouble(configurationSection, "acceleration", 0.1);
        double deceleration = getDouble(configurationSection, "deceleration", acceleration);
        int minWaitTicks = getInt(configurationSection, "minWaitTicks", 0);

        return new BlockSectionSpecConfig(engage, driveSpeed, canSpawn, acceleration, deceleration, minWaitTicks);
    }
}
