package com.jverbruggen.jrides.config.coaster.objects.section.ranged;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class StationEffectsConfig {
    private final List<String> entryBlockingEffects;
    private final List<String> exitBlockingEffects;
    private final List<String> exitEffects;

    public StationEffectsConfig(List<String> entryBlockingEffects, List<String> exitBlockingEffects, List<String> exitEffects) {
        this.entryBlockingEffects = entryBlockingEffects;
        this.exitBlockingEffects = exitBlockingEffects;
        this.exitEffects = exitEffects;
    }

    public StationEffectsConfig() {
        this.entryBlockingEffects = null;
        this.exitBlockingEffects = null;
        this.exitEffects = null;
    }

    public List<String> getEntryBlockingEffects() {
        return entryBlockingEffects;
    }

    public List<String> getExitBlockingEffects() {
        return exitBlockingEffects;
    }

    public List<String> getExitEffects() {
        return exitEffects;
    }

    public static StationEffectsConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        if(configurationSection == null) return new StationEffectsConfig();
        List<String> entryBlockingEffects = configurationSection.getStringList("entryBlocking");
        List<String> exitBlockingEffects = configurationSection.getStringList("exitBlocking");
        List<String> exitEffects = configurationSection.getStringList("exit");

        return new StationEffectsConfig(entryBlockingEffects, exitBlockingEffects, exitEffects);
    }
}

