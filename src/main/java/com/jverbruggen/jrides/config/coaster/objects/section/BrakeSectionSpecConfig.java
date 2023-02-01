package com.jverbruggen.jrides.config.coaster.objects.section;

import org.bukkit.configuration.ConfigurationSection;

public class BrakeSectionSpecConfig {
    private final double driveSpeed;
    private final double deceleration;

    public BrakeSectionSpecConfig(double driveSpeed, double deceleration) {
        this.driveSpeed = driveSpeed;
        this.deceleration = deceleration;
    }

    public static BrakeSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double driveSpeed = configurationSection.getDouble("driveSpeed");
        double deceleration = configurationSection.getDouble("deceleration");

        return new BrakeSectionSpecConfig(driveSpeed, deceleration);
    }
}
