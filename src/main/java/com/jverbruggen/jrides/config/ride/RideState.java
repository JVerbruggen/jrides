package com.jverbruggen.jrides.config.ride;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.models.ride.state.OpenState;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RideState extends BaseConfig implements ConfigurationSerializable {
    private final String rideIdentifier;
    private OpenState openState;

    public RideState(String rideIdentifier, OpenState openState) {
        this.rideIdentifier = rideIdentifier;
        this.openState = openState;
    }

    public OpenState getOpenState() {
        return openState;
    }

    public boolean setStateOpened(RideHandle rideHandle){
        if(openState.isOpen() || openState.isOpening())
            return false;
        OpenState newState = openState.open(rideHandle);
        boolean changed = newState != this.openState;
        this.openState = newState;
        return changed;
    }

    public boolean setStateClosed(RideHandle rideHandle){
        if(!openState.isOpen())
            return false;
        OpenState newState = openState.close(rideHandle);
        boolean changed = newState != this.openState;
        this.openState = newState;
        return changed;
    }

    public boolean setStateFullyClosed(){
        if(openState != OpenState.TRANSITION_TO_CLOSE){
            return false;
        }
        this.openState = OpenState.CLOSED;
        return true;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("rideIdentifier", rideIdentifier);
        serialized.put("openState", openState.toString());

        return serialized;
    }

    public static RideState deserialize(Map<String, Object> config){
        String rideIdentifier = getString(config, "rideIdentifier");
        OpenState openState = OpenState.valueOf(getString(config, "openState", "MAINTENANCE"));

        return new RideState(rideIdentifier, openState);
    }

    public boolean shouldLoadRide(){
        return openState != OpenState.DISABLED;
    }

    public void save(){
        ConfigManager configManager = ServiceProvider.getSingleton(ConfigManager.class);
        String fileName = getFileName(configManager, rideIdentifier);
        configManager.updateConfigFile(fileName, "state", this);
    }

    public static RideState load(String rideIdentifier) {
        ConfigManager configManager = ServiceProvider.getSingleton(ConfigManager.class);
        String fileName = getFileName(configManager, rideIdentifier);

        Optional<RideState> state = configManager.getConfigFileObject(fileName, "state", RideState.class);

        return state.orElseGet(() -> {
            JRidesPlugin.getLogger().warning("Could not get ride state for " + rideIdentifier + ", creating new one");
            RideState newState = new RideState(
                    rideIdentifier,
                    OpenState.MAINTENANCE
            );
            configManager.updateConfigFile(fileName, "state", newState);
            return newState;
        });

    }

    private static String getFileName(ConfigManager configManager, String rideIdentifier){
        return configManager.getFolder(rideIdentifier) + "/" + rideIdentifier + ".state.yml";
    }
}
