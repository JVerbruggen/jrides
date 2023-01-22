package com.jverbruggen.jrides.config.ride;

import org.bukkit.configuration.ConfigurationSection;

public class RideConfigObject {
    private String identifier;
    private String type;

    public RideConfigObject(String identifier, String type) {
        this.identifier = identifier;
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getType() {
        return type;
    }

    public static RideConfigObject fromConfigurationSection(ConfigurationSection configurationSection){
        String identifier = configurationSection.getString("identifier");
        String type = configurationSection.getString("type");
        return new RideConfigObject(identifier, type);
    }
}
