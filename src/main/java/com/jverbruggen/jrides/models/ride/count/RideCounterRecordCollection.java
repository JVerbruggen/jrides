package com.jverbruggen.jrides.models.ride.count;

import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RideCounterRecordCollection implements ConfigurationSerializable {
    private List<RideCounterRecord> records;
    private String playerIdentifier;

    public RideCounterRecordCollection(List<RideCounterRecord> records, String playerIdentifier) {
        this.records = records;
        this.playerIdentifier = playerIdentifier;
    }

    public List<RideCounterRecord> getRecords() {
        return records;
    }

    public String getPlayerIdentifier() {
        return playerIdentifier;
    }

    public void saveToFile(){
        String fileName = getRideCounterFile(playerIdentifier);
        ConfigManager configManager = ServiceProvider.getSingleton(ConfigManager.class);

        YamlConfiguration configuration = configManager.getYamlConfiguration(fileName);
        configuration.set(ROOT_CONFIG_KEY, this);

        configManager.saveConfig(configuration, fileName);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> config = new HashMap<>();

        config.put("records", this.records);
        config.put("playerIdentifier", this.playerIdentifier);

        return config;
    }

    public static RideCounterRecordCollection deserialize(Map<String, Object> config){
        List<RideCounterRecord> records = (List<RideCounterRecord>) config.get("records");
        String playerIdentifier = (String) config.get("playerIdentifier");

        return new RideCounterRecordCollection(records, playerIdentifier);
    }

    private static String ROOT_CONFIG_KEY = "counters";

    private static String getRideCounterFile(String playerIdentifier){
        return "ridecounters/" + playerIdentifier + ".yml";
    }

    public static RideCounterRecordCollection loadRideCounters(String playerIdentifier){
        String fileName = getRideCounterFile(playerIdentifier);
        YamlConfiguration configuration = ServiceProvider.getSingleton(ConfigManager.class).getYamlConfiguration(fileName);

        if(!configuration.contains(ROOT_CONFIG_KEY))
            return new RideCounterRecordCollection(new ArrayList<>(), playerIdentifier);

        return (RideCounterRecordCollection) configuration.get(ROOT_CONFIG_KEY);
    }
}
