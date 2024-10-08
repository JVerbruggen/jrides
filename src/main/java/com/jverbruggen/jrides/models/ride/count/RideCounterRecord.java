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

package com.jverbruggen.jrides.models.ride.count;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.UnloadedRide;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RideCounterRecord implements ConfigurationSerializable {
    private final Ride ride;
    private final UUID playerUUID;
    private String playerName;
    private int rideCount;

    public RideCounterRecord(Ride ride, String playerName, UUID playerUUID, int rideCount) {
        this.ride = ride;
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.rideCount = rideCount;
    }

    public boolean isActive(){
        return this.ride.isLoaded();
    }

    public Ride getRide() {
        return ride;
    }

    public String getRideIdentifier(){
        return ride.getIdentifier();
    }

    public int getRideCount() {
        return rideCount;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void addOne(){
        rideCount++;
    }

    public void set(int count){
        rideCount = count;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> config = new HashMap<>();

        config.put("rideIdentifier", this.ride.getIdentifier());
        config.put("playerName", this.playerName);
        config.put("playerUUID", this.playerUUID.toString());
        config.put("rideCount", this.rideCount);

        return config;
    }

    public static RideCounterRecord deserialize(Map<String, Object> config){
        String rideIdentifier = (String) config.get("rideIdentifier");

        RideHandle rideHandle = ServiceProvider.getSingleton(RideManager.class)
                .getRideHandle(rideIdentifier);
        Ride ride;
        if(rideHandle != null) ride = rideHandle.getRide();
        else ride = new UnloadedRide(rideIdentifier);

        int rideCount = (int) config.get("rideCount");
        String playerName = (String) config.get("playerName");
        String playerUUID = (String) config.get("playerUUID");

        return new RideCounterRecord(ride, playerName, UUID.fromString(playerUUID), rideCount);
    }

    public boolean sameIdentityAs(RideCounterRecord otherRecord){
        if(otherRecord == this) return true;

        return otherRecord.getPlayerUUID() == this.playerUUID
                && otherRecord.getRideIdentifier().equalsIgnoreCase(this.getRideIdentifier());
    }

    public void updateTo(RideCounterRecord otherRecord){
        this.rideCount = otherRecord.rideCount;
        this.playerName = otherRecord.playerName;
    }
}
