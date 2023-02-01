package com.jverbruggen.jrides.config.coaster.objects.section;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class StationEffectsConfig {
    private final List<String> entryEffects;
    private final List<String> exitEffects;

    public StationEffectsConfig(List<String> entryEffects, List<String> exitEffects) {
        this.entryEffects = entryEffects;
        this.exitEffects = exitEffects;
    }

    public List<String> getEntryEffects() {
        return entryEffects;
    }

    public List<String> getExitEffects() {
        return exitEffects;
    }

    public static StationEffectsConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        List<String> entryEffects = configurationSection.getStringList("entry");
        List<String> exitEffects = configurationSection.getStringList("exit");

        return new StationEffectsConfig(entryEffects, exitEffects);
    }
}

