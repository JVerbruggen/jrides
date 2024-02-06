package com.jverbruggen.jrides.state.ride;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecord;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecordCollection;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecordRideCollection;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;

public class RideCounterManager {
    private static final String ROOT_CONFIG_KEY = "counters";

    private final LanguageFile languageFile;
    private final Map<String, RideCounterRecordCollection> cachedPlayerCollections;
    private final Map<String, RideCounterRecordRideCollection> cachedRideCollections;

    public RideCounterManager() {
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        this.cachedPlayerCollections = new HashMap<>();
        this.cachedRideCollections = new HashMap<>();
    }

    public void sendRideCounterUpdateMessage(JRidesPlayer player, RideCounterRecord record){
        languageFile.sendMultilineMessage(player, LanguageFileField.NOTIFICATION_RIDE_COUNTER_UPDATE,
                builder -> builder
                        .add(LanguageFileTag.rideCount, String.valueOf(record.getRideCount()))
                        .add(LanguageFileTag.rideDisplayName, record.getRide().getDisplayName()));
    }

    public RideCounterRecordCollection getCollection(String playerIdentifier){
        RideCounterRecordCollection collection = getCachedPlayerCollection(playerIdentifier);
        if(collection != null)
            return collection;

        collection = loadFromFile(playerIdentifier);
        cachedPlayerCollections.put(playerIdentifier, collection);
        return collection;
    }

    public RideCounterRecordRideCollection getCollectionRideBound(String rideIdentifier){
        RideCounterRecordRideCollection collection = getCachedRideCollection(rideIdentifier);
        if(collection != null)
            return collection;

        collection = loadForRideFromFile(rideIdentifier);
        cachedRideCollections.put(rideIdentifier, collection);
        return collection;
    }

    public void saveAndUnloadAll(){
        for(Map.Entry<String, RideCounterRecordCollection> entry : cachedPlayerCollections.entrySet()){
            saveToFile(entry.getKey(), entry.getValue());
        }

        cachedPlayerCollections.clear();
    }

    public void saveAndUnload(String playerIdentifier){
        RideCounterRecordCollection rideCounterRecordCollection = getCachedPlayerCollection(playerIdentifier);
        if(saveToFile(playerIdentifier, rideCounterRecordCollection)){
            removeCached(playerIdentifier);
        }
    }
    private RideCounterRecordCollection getCachedPlayerCollection(String playerIdentifier){
        return cachedPlayerCollections.get(playerIdentifier);
    }

    private RideCounterRecordRideCollection getCachedRideCollection(String rideIdentifier){
        return cachedRideCollections.get(rideIdentifier);
    }

    private void removeCached(String playerIdentifier){
        cachedPlayerCollections.remove(playerIdentifier);
    }

    private String getRideCounterFile(String playerIdentifier){
        return "ridecounters/player/" + playerIdentifier + ".yml";
    }

    private String getRideCounterFileRide(String rideIdentifier){
        return "ridecounters/ride/" + rideIdentifier + ".yml";
    }

    private RideCounterRecordCollection loadFromFile(String playerIdentifier){
        String fileName = getRideCounterFile(playerIdentifier);
        YamlConfiguration configuration = ServiceProvider.getSingleton(ConfigManager.class).getYamlConfiguration(fileName);

        if(configuration == null || !configuration.contains(ROOT_CONFIG_KEY))
            return new RideCounterRecordCollection(playerIdentifier);

        return (RideCounterRecordCollection) configuration.get(ROOT_CONFIG_KEY);
    }

    private RideCounterRecordRideCollection loadForRideFromFile(String rideIdentifier){
        String fileName = getRideCounterFileRide(rideIdentifier);
        YamlConfiguration configuration = ServiceProvider.getSingleton(ConfigManager.class).getYamlConfiguration(fileName);

        if(configuration == null || !configuration.contains(ROOT_CONFIG_KEY)){
            RideHandle rideHandle = ServiceProvider.getSingleton(RideManager.class).getRideHandle(rideIdentifier);
            return new RideCounterRecordRideCollection(rideHandle);
        }

        return (RideCounterRecordRideCollection) configuration.get(ROOT_CONFIG_KEY);
    }

    public boolean saveToFile(String playerIdentifier, RideCounterRecordCollection rideCounterRecordCollection){
        if(rideCounterRecordCollection == null) return false;

        String fileName = getRideCounterFile(playerIdentifier);
        saveToYamlConfiguration(fileName, rideCounterRecordCollection);
        return true;
    }

    public boolean saveToRideFile(String rideIdentifier, RideCounterRecordRideCollection rideCounterRecordRideCollection){
        if(rideCounterRecordRideCollection == null) return false;

        String fileName = getRideCounterFileRide(rideIdentifier);
        saveToYamlConfiguration(fileName, rideCounterRecordRideCollection);
        return true;
    }

    private void saveToYamlConfiguration(String fileName, Object object){
        ConfigManager configManager = ServiceProvider.getSingleton(ConfigManager.class);

        YamlConfiguration configuration = configManager.getOrCreateConfiguration(fileName);
        configuration.set(ROOT_CONFIG_KEY, object);

        configManager.saveConfig(configuration, fileName);
    }
}
