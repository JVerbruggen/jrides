package com.jverbruggen.jrides.config.coaster.objects.section;

import org.bukkit.configuration.ConfigurationSection;

public class BlockSectionSpecConfig {
    private final double engage;
    private final boolean canSpawn;

    private BlockSectionSpecConfig(double engage, boolean canSpawn) {
        this.engage = engage;
        this.canSpawn = canSpawn;
    }

    public double getEngage() {
        return engage;
    }

    public boolean canSpawn() {
        return canSpawn;
    }

    public static BlockSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double engage = configurationSection.getDouble("engage");
        boolean canSpawn = configurationSection.getBoolean("canSpawn");

        return new BlockSectionSpecConfig(engage, canSpawn);
    }
}
