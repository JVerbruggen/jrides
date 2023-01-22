package com.jverbruggen.jrides.config.coaster.objects;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class TrackConfig {
    private String mode;
    private List<Float> position;

    public TrackConfig(String mode, List<Float> position) {
        this.mode = mode;
        this.position = position;
    }

    public String getMode() {
        return mode;
    }

    public List<Float> getPosition() {
        return position;
    }

    public static TrackConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String mode = configurationSection.getString("mode");
        List<Float> position = configurationSection.getFloatList("position");

        return new TrackConfig(mode, position);
    }
}
