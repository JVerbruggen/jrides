package com.jverbruggen.jrides.config.coaster.objects.section;

import org.bukkit.configuration.ConfigurationSection;

public class BlockSectionSpecConfig {
    private final String identifier;
    private final double engage;
    private final double driveSpeed;
    private final boolean canSpawn;

    private BlockSectionSpecConfig(String identifier, double engage, double driveSpeed, boolean canSpawn) {
        this.identifier = identifier;
        this.engage = engage;
        this.driveSpeed = driveSpeed;
        this.canSpawn = canSpawn;
    }

    public String getIdentifier() {
        return identifier;
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

    public static BlockSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection, String sectionIdentifier) {
        double engage = configurationSection.getDouble("engage");
        double driveSpeed = configurationSection.getDouble("driveSpeed");
        boolean canSpawn = configurationSection.getBoolean("canSpawn");

        return new BlockSectionSpecConfig(sectionIdentifier, engage, driveSpeed, canSpawn);
    }
}
