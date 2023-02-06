package com.jverbruggen.jrides.config.coaster.objects;

import org.bukkit.configuration.ConfigurationSection;

public abstract class BaseConfig {
    private static void assertPresence(ConfigurationSection configurationSection, String key){
        if(!configurationSection.contains(key)){
            throw new RuntimeException("Missing key in config: " + key);
        }
    }

    private static boolean isPresent(ConfigurationSection configurationSection, String key){
        return configurationSection.contains(key);
    }

    protected static double getDouble(ConfigurationSection configurationSection, String key){
        assertPresence(configurationSection, key);
        return configurationSection.getDouble(key);
    }

    protected static double getDouble(ConfigurationSection configurationSection, String key, double defaultValue){
        if(!isPresent(configurationSection, key)) return defaultValue;
        return configurationSection.getDouble(key);
    }

    protected static int getInt(ConfigurationSection configurationSection, String key){
        assertPresence(configurationSection, key);
        return configurationSection.getInt(key);
    }

    protected static int getInt(ConfigurationSection configurationSection, String key, int defaultValue){
        if(!isPresent(configurationSection, key)) return defaultValue;
        return configurationSection.getInt(key);
    }

    protected static String getString(ConfigurationSection configurationSection, String key){
        assertPresence(configurationSection, key);
        return configurationSection.getString(key);
    }

    protected static boolean getBoolean(ConfigurationSection configurationSection, String key) {
        assertPresence(configurationSection, key);
        return configurationSection.getBoolean(key);
    }
}
