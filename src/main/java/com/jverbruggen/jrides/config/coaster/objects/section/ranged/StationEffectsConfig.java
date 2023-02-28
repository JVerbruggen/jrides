package com.jverbruggen.jrides.config.coaster.objects.section.ranged;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class StationEffectsConfig {
    private final List<String> entryEffects;
    private final List<String> exitEffects;

    public StationEffectsConfig(List<String> entryEffects, List<String> exitEffects) {
        this.entryEffects = entryEffects;
        this.exitEffects = exitEffects;
    }

    public StationEffectsConfig() {
        this.entryEffects = null;
        this.exitEffects = null;
    }

    public List<String> getEntryEffects() {
        return entryEffects;
    }

    public List<String> getExitEffects() {
        return exitEffects;
    }

    public static StationEffectsConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        if(configurationSection == null) return new StationEffectsConfig();
        List<String> entryEffects = configurationSection.getStringList("entry");
        List<String> exitEffects = configurationSection.getStringList("exit");

        return new StationEffectsConfig(entryEffects, exitEffects);
    }
}

