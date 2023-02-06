package com.jverbruggen.jrides.config.coaster.objects.section;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class BrakeSectionSpecConfig extends BaseConfig {
    private final double driveSpeed;
    private final double deceleration;

    public BrakeSectionSpecConfig(double driveSpeed, double deceleration) {
        this.driveSpeed = driveSpeed;
        this.deceleration = deceleration;
    }

    public double getDriveSpeed() {
        return driveSpeed;
    }

    public double getDeceleration() {
        return deceleration;
    }

    public static BrakeSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double driveSpeed = getDouble(configurationSection, "driveSpeed");
        double deceleration = getDouble(configurationSection, "deceleration");

        return new BrakeSectionSpecConfig(driveSpeed, deceleration);
    }
}
