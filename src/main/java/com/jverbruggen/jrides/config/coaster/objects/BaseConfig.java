package com.jverbruggen.jrides.config.coaster.objects;

import com.jverbruggen.jrides.config.utils.CycleIntegerSupplier;
import com.jverbruggen.jrides.config.utils.IntegerSupplier;
import com.jverbruggen.jrides.config.utils.RandomIntegerSupplier;
import com.jverbruggen.jrides.config.utils.SimpleIntegerSupplier;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class BaseConfig {
    private static Random random = new Random();

    private static String getAvailableKeys(ConfigurationSection configurationSection){
        if(configurationSection == null) return "<none>";
        return String.join(", ", configurationSection.getKeys(false));
    }

    private static void assertPresence(ConfigurationSection configurationSection, String key){
        if(configurationSection == null || !configurationSection.contains(key)){
            throw new RuntimeException("Missing key in config: '" + key + "', available: " + getAvailableKeys(configurationSection));
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
        return getDouble(configurationSection, key);
    }

    protected static int getInt(ConfigurationSection configurationSection, String key){
        assertPresence(configurationSection, key);
        return configurationSection.getInt(key);
    }

    protected static int getInt(ConfigurationSection configurationSection, String key, int defaultValue){
        if(!isPresent(configurationSection, key)) return defaultValue;
        return getInt(configurationSection, key);
    }

    protected static IntegerSupplier getIntSupplier(ConfigurationSection configurationSection, String key){
        assertPresence(configurationSection, key);
        Object value = configurationSection.get(key);
        if(value instanceof List<?>){
            List<Integer> ints = ((List<?>)value).stream().map(o -> (int)o).toList();
            if(ints.size() != 2) throw new RuntimeException("Range can only have 2 items");
            return new RandomIntegerSupplier(ints.get(0), ints.get(1));
        }else if(value instanceof String sValue && sValue.startsWith("<")){
            List<Integer> ints = Arrays.stream(sValue.substring(1, sValue.length() - 1).replace(" ", "").split(","))
                    .map(Integer::parseInt)
                    .toList();
            return new CycleIntegerSupplier(ints);
        }

        assert value instanceof Integer;
        return new SimpleIntegerSupplier((int) value);
    }

    protected static IntegerSupplier getIntSupplier(ConfigurationSection configurationSection, String key, int defaultValue){
        if(!isPresent(configurationSection, key)) return new SimpleIntegerSupplier(defaultValue);
        return getIntSupplier(configurationSection, key);
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

    protected static @Nullable ConfigurationSection getConfigurationSection(ConfigurationSection configurationSection, String key){
        if(!isPresent(configurationSection, key)) return null;
        return configurationSection.getConfigurationSection(key);
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

    protected static List<Double> getDoubleList(ConfigurationSection configurationSection, String key, List<Double> defaultValue){
        if(!isPresent(configurationSection, key)) return defaultValue;
        return getDoubleList(configurationSection, key);
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

    protected static List<List<Double>> getDoubleListList(ConfigurationSection configurationSection, String key, List<List<Double>> defaultValue){
        if(!isPresent(configurationSection, key)) return defaultValue;
        return getDoubleListList(configurationSection, key);
    }

    protected static double toDouble(Object object){
        if(object instanceof Double){
            return (double) object;
        }else if(object instanceof Integer){
            return ((Integer)object).doubleValue();
        }else throw new RuntimeException("Cannot convert " + object + " to double");
    }

}
