/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

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

enum RideType {
    FLATRIDE,
    COASTER
}

public class RideState extends BaseConfig implements ConfigurationSerializable {
    private final String rideIdentifier;
    private OpenState openState;
    private final RideType rideType;

    public RideState(String rideIdentifier, OpenState openState, RideType rideType) {
        this.rideIdentifier = rideIdentifier;
        this.openState = openState;
        this.rideType = rideType;
    }

    public OpenState getOpenState() {
        return openState;
    }

    public boolean setStateOpened(RideHandle rideHandle){
        if(openState == OpenState.DISABLED){
            JRidesPlugin.getLogger().severe("Cannot open a ride which has the DISABLED status");
            return false;
        }

        if(openState.isOpen() || openState.isOpening())
            return false;
        OpenState newState = openState.open(rideHandle);
        boolean changed = newState != this.openState;
        this.openState = newState;
        return changed;
    }

    public boolean setStateClosed(RideHandle rideHandle){
        if(openState == OpenState.DISABLED){
            JRidesPlugin.getLogger().severe("Cannot close a ride which has the DISABLED status");
            return false;
        }

        if(!openState.isOpen())
            return false;
        OpenState newState = openState.close(rideHandle);
        boolean changed = newState != this.openState;
        this.openState = newState;
        return changed;
    }

    public boolean setInactive(){
        this.openState = OpenState.INACTIVE;
        return true;
    }

    public boolean isInactive(){
        return this.openState == OpenState.INACTIVE || this.openState == OpenState.DISABLED;
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
        serialized.put("rideType", rideType.toString());
        serialized.put("openState", openState.toString());

        return serialized;
    }

    public static RideState deserialize(Map<String, Object> config){
        String rideIdentifier = getString(config, "rideIdentifier");
        RideType rideType = RideType.valueOf(getString(config, "rideType"));
        OpenState openState = OpenState.valueOf(getString(config, "openState", "MAINTENANCE"));

        return new RideState(rideIdentifier, openState, rideType);
    }

    public boolean shouldLoadRide(){
        return openState != OpenState.DISABLED;
    }

    public void save(){
        ConfigManager configManager = ServiceProvider.getSingleton(ConfigManager.class);
        String fileName = switch (rideType) {
            case FLATRIDE -> getFlatrideFileName(configManager, rideIdentifier);
            case COASTER -> getCoasterFileName(configManager, rideIdentifier);
        };

        configManager.updateConfigFile(fileName, "state", this);
    }

    public static RideState loadCoasterState(String rideIdentifier) {
        ConfigManager configManager = ServiceProvider.getSingleton(ConfigManager.class);
        String fileName = getCoasterFileName(configManager, rideIdentifier);
        return load(rideIdentifier, fileName, RideType.COASTER);
    }

    public static RideState loadFlatrideState(String rideIdentifier) {
        ConfigManager configManager = ServiceProvider.getSingleton(ConfigManager.class);
        String fileName = getFlatrideFileName(configManager, rideIdentifier);
        return load(rideIdentifier, fileName, RideType.FLATRIDE);
    }

    private static RideState load(String rideIdentifier, String fileName, RideType rideType){
        ConfigManager configManager = ServiceProvider.getSingleton(ConfigManager.class);

        Optional<RideState> state = configManager.getConfigFileObject(fileName, "state", RideState.class);

        return state.orElseGet(() -> {
            JRidesPlugin.getLogger().warning("Could not get ride state for " + rideIdentifier + ", creating new one");
            RideState newState = new RideState(
                    rideIdentifier,
                    OpenState.MAINTENANCE,
                    rideType);
            configManager.updateConfigFile(fileName, "state", newState);
            return newState;
        });
    }

    private static String getCoasterFileName(ConfigManager configManager, String rideIdentifier){
        return configManager.getCoasterFolder(rideIdentifier) + "/" + rideIdentifier + ".state.yml";
    }

    private static String getFlatrideFileName(ConfigManager configManager, String rideIdentifier){
        return configManager.getFlatrideFolder(rideIdentifier) + "/" + rideIdentifier + ".state.yml";
    }
}
