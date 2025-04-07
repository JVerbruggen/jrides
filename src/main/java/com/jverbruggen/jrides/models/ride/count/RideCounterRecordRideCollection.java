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
import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

/**
 * Ride-bound ride counter record collection.
 */
public class RideCounterRecordRideCollection implements ConfigurationSerializable {
    private final List<RideCounterRecord> records;
    private final RideHandle rideHandle;

    public RideCounterRecordRideCollection(List<RideCounterRecord> records, RideHandle rideHandle) {
        this.records = records;
        this.rideHandle = rideHandle;
    }

    public RideCounterRecordRideCollection(RideHandle rideHandle) {
        this.records = new ArrayList<>();
        this.rideHandle = rideHandle;
    }

    private RideCounterRecord findSimilarRecord(RideCounterRecord record){
        return this.records.stream().filter(r -> r.sameIdentityAs(record)).findFirst().orElse(null);
    }

    public RideCounterRecord findOrCreate(JRidesPlayer player){
        Optional<RideCounterRecord> record = this.records.stream().filter(r -> r.getPlayerUUID().equals(player.getUniqueId())).findFirst();
        if(record.isPresent()) return record.get();

        RideCounterRecord newRecord = new RideCounterRecord(rideHandle.getRide(), player.getName(), player.getUniqueId(), 0);
        this.records.add(newRecord);
        return newRecord;
    }

    public boolean existsOn(JRidesPlayer player){
        Optional<RideCounterRecord> record = this.records.stream().filter(r -> r.getPlayerUUID().equals(player.getUniqueId())).findFirst();
        return record.isPresent();
    }

    public List<RideCounterRecord> getRecords() {
        return records;
    }

    public boolean update(RideCounterRecord record){
        if(!appliesToThisCollection(record)) return false;

        RideCounterRecord foundRecord = records.stream().filter(r -> r.sameIdentityAs(record)).findFirst().orElse(null);
        if(foundRecord == null) return false;

        foundRecord.updateTo(record);
        return true;
    }

    public void add(RideCounterRecord record){
        if(!appliesToThisCollection(record)) return;

        records.add(record);
    }

    public boolean remove(RideCounterRecord record){
        if(!appliesToThisCollection(record)) return false;

        if(records.contains(record)){
            records.remove(record);
            return true;
        }

        RideCounterRecord foundRecord = findSimilarRecord(record);
        if(foundRecord != null){
            records.remove(foundRecord);
            return true;
        }

        return false;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> config = new HashMap<>();

        config.put("records", records);
        config.put("rideIdentifier", this.rideHandle.getRide().getIdentifier());

        return config;
    }

    public static RideCounterRecordRideCollection deserialize(Map<String, Object> config){
        List<RideCounterRecord> records = (List<RideCounterRecord>) config.get("records");
        String rideIdentifier = (String) config.get("rideIdentifier");
        RideHandle rideHandle = ServiceProvider.getSingleton(RideManager.class).getRideHandle(rideIdentifier);

        return new RideCounterRecordRideCollection(records, rideHandle);
    }

    private boolean appliesToThisCollection(RideCounterRecord record){
        return record.getRide().getIdentifier().equalsIgnoreCase(rideHandle.getRide().getIdentifier());
    }
}
