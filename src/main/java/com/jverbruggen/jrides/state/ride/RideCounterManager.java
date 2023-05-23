package com.jverbruggen.jrides.state.ride;

import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecordCollection;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;

public class RideCounterManager {
    private static String ROOT_CONFIG_KEY = "counters";

    private Map<String, RideCounterRecordCollection> cached;

    public RideCounterManager() {
        this.cached = new HashMap<>();
    }

    public RideCounterRecordCollection getCollection(Player player){
        return getCollection(player.getIdentifier());
    }

    public RideCounterRecordCollection getCollection(String playerIdentifier){
        RideCounterRecordCollection collection = getCached(playerIdentifier);
        if(collection != null)
            return collection;

        collection = loadFromFile(playerIdentifier);
        cached.put(playerIdentifier, collection);
        return collection;
    }

    public void saveAndUnloadAll(){
        for(Map.Entry<String, RideCounterRecordCollection> entry : cached.entrySet()){
            saveToFile(entry.getKey(), entry.getValue());
        }

        cached.clear();
    }

    public void saveAndUnload(String playerIdentifier){
        RideCounterRecordCollection rideCounterRecordCollection = getCached(playerIdentifier);
        if(saveToFile(playerIdentifier, rideCounterRecordCollection)){
            removeCached(playerIdentifier);
        }
    }
    private RideCounterRecordCollection getCached(String playerIdentifier){
        return cached.get(playerIdentifier);
    }

    private void removeCached(String playerIdentifier){
        cached.remove(playerIdentifier);
    }

    private String getRideCounterFile(String playerIdentifier){
        return "ridecounters/" + playerIdentifier + ".yml";
    }

    private RideCounterRecordCollection loadFromFile(String playerIdentifier){
        String fileName = getRideCounterFile(playerIdentifier);
        YamlConfiguration configuration = ServiceProvider.getSingleton(ConfigManager.class).getYamlConfiguration(fileName);

        if(configuration == null || !configuration.contains(ROOT_CONFIG_KEY))
            return new RideCounterRecordCollection(playerIdentifier);

        return (RideCounterRecordCollection) configuration.get(ROOT_CONFIG_KEY);
    }

    public boolean saveToFile(String playerIdentifier, RideCounterRecordCollection rideCounterRecordCollection){
        if(rideCounterRecordCollection == null) return false;

        String fileName = getRideCounterFile(playerIdentifier);
        ConfigManager configManager = ServiceProvider.getSingleton(ConfigManager.class);

        YamlConfiguration configuration = configManager.getOrCreateConfiguration(fileName);
        configuration.set(ROOT_CONFIG_KEY, rideCounterRecordCollection);

        configManager.saveConfig(configuration, fileName);
        return true;
    }
}
