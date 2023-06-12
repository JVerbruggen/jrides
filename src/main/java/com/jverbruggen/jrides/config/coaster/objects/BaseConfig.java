package com.jverbruggen.jrides.config.coaster.objects;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BaseConfig {
    private static void assertPresence(ConfigurationSection configurationSection, String key){
        if(configurationSection == null || !configurationSection.contains(key)){
            throw new RuntimeException("Missing key in config: " + key);
        }
    }

    private static void assertPresence(Map<String, Object> config, String key){
        if(config == null || !config.containsKey(key)){
            throw new RuntimeException("Missing key in config: " + key);
        }
    }

    private static boolean isPresent(ConfigurationSection configurationSection, String key){
        if(configurationSection == null) return false;
        return configurationSection.contains(key);
    }

    private static boolean isPresent(Map<String, Object> config, String key){
        if(config == null) return false;
        return config.containsKey(key);
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

    protected static String getString(Map<String, Object> config, String key, String defaultValue){
        if(!isPresent(config, key)) return defaultValue;
        return (String) config.get(key);
    }

    protected static String getString(ConfigurationSection configurationSection, String key, String defaultValue){
        if(!isPresent(configurationSection, key)) return defaultValue;
        return configurationSection.getString(key);
    }

    protected static String getString(Map<String, Object> config, String key){
        assertPresence(config, key);
        return (String) config.get(key);
    }

    protected static String getString(ConfigurationSection configurationSection, String key){
        assertPresence(configurationSection, key);
        return configurationSection.getString(key);
    }

    protected static boolean getBoolean(ConfigurationSection configurationSection, String key, boolean defaultValue) {
        if(!isPresent(configurationSection, key)) return defaultValue;
        return configurationSection.getBoolean(key);
    }

    protected static boolean getBoolean(ConfigurationSection configurationSection, String key) {
        assertPresence(configurationSection, key);
        return configurationSection.getBoolean(key);
    }

    protected static List<String> getStringList(ConfigurationSection configurationSection, String key){
        assertPresence(configurationSection, key);
        return configurationSection.getStringList(key);
    }

    protected static List<String> getStringList(ConfigurationSection configurationSection, String key, List<String> defaultValue){
        if(!isPresent(configurationSection, key)) return defaultValue;
        return configurationSection.getStringList(key);
    }

    protected static List<Double> getDoubleList(ConfigurationSection configurationSection, String key){
        assertPresence(configurationSection, key);

        return configurationSection.getList(key)
                .stream()
                .map(BaseConfig::toDouble)
                .collect(Collectors.toList());
    }

    protected static List<List<Double>> getDoubleListList(ConfigurationSection configurationSection, String key){
        assertPresence(configurationSection, key);

        return configurationSection.getList(key)
                .stream()
                .map(obj -> ((List<?>) obj).stream()
                        .map(BaseConfig::toDouble)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    protected static double toDouble(Object object){
        if(object instanceof Double){
            return (double) object;
        }else if(object instanceof Integer){
            return ((Integer)object).doubleValue();
        }else throw new RuntimeException("Cannot convert " + object + " to double");
    }

}
