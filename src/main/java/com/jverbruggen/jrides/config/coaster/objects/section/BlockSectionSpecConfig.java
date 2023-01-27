package com.jverbruggen.jrides.config.coaster.objects.section;

import org.bukkit.configuration.ConfigurationSection;

public class BlockSectionSpecConfig {
    private final String identifier;
    private final double engage;
    private final boolean canSpawn;

    private BlockSectionSpecConfig(String identifier, double engage, boolean canSpawn) {
        this.identifier = identifier;
        this.engage = engage;
        this.canSpawn = canSpawn;
    }

    public String getIdentifier() {
        return identifier;
    }

    public double getEngage() {
        return engage;
    }

    public boolean canSpawn() {
        return canSpawn;
    }

    public static BlockSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection, String sectionIdentifier) {
        double engage = configurationSection.getDouble("engage");
        boolean canSpawn = configurationSection.getBoolean("canSpawn");

        return new BlockSectionSpecConfig(sectionIdentifier, engage, canSpawn);
    }
}
