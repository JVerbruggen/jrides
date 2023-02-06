package com.jverbruggen.jrides.config.coaster.objects.section;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class LaunchEffectsConfig {
    private final List<String> launchEffects;

    public LaunchEffectsConfig(List<String> launchEffects) {
        this.launchEffects = launchEffects;
    }

    public List<String> getLaunchEffects() {
        return launchEffects;
    }

    public static LaunchEffectsConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        if(configurationSection == null) return new LaunchEffectsConfig(List.of());

        List<String> launchEffects = configurationSection.getStringList("launch");

        return new LaunchEffectsConfig(launchEffects);
    }
}

