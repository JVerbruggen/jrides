package com.jverbruggen.jrides.config.ride;

import org.bukkit.configuration.ConfigurationSection;

public class RidesConfigObject {
    private String identifier;
    private String type;

    public RidesConfigObject(String identifier, String type) {
        this.identifier = identifier;
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getType() {
        return type;
    }

    public static RidesConfigObject fromConfigurationSection(ConfigurationSection configurationSection){
        String identifier = configurationSection.getString("identifier");
        String type = configurationSection.getString("type");
        return new RidesConfigObject(identifier, type);
    }
}
