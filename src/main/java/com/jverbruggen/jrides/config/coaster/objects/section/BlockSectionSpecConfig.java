package com.jverbruggen.jrides.config.coaster.objects.section;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class BlockSectionSpecConfig extends BaseConfig {
    private final double engage;
    private final double driveSpeed;
    private final boolean canSpawn;

    private BlockSectionSpecConfig(double engage, double driveSpeed, boolean canSpawn) {
        this.engage = engage;
        this.driveSpeed = driveSpeed;
        this.canSpawn = canSpawn;
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

    public static BlockSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double engage = getDouble(configurationSection, "engage");
        double driveSpeed = getDouble(configurationSection, "driveSpeed");
        boolean canSpawn = getBoolean(configurationSection, "canSpawn");

        return new BlockSectionSpecConfig(engage, driveSpeed, canSpawn);
    }
}
