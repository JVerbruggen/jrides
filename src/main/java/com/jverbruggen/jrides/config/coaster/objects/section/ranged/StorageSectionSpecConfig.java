package com.jverbruggen.jrides.config.coaster.objects.section.ranged;

import org.bukkit.configuration.ConfigurationSection;

public class StorageSectionSpecConfig {
    private final String sectionAtEnd;

    public StorageSectionSpecConfig(String sectionAtEnd) {
        this.sectionAtEnd = sectionAtEnd;
    }

    public static StorageSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String sectionAtEnd = configurationSection.getString("sectionAtEnd");

        return new StorageSectionSpecConfig(sectionAtEnd);
    }
}
