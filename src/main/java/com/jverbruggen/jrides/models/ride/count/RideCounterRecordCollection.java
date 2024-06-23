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

import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RideCounterRecordCollection implements ConfigurationSerializable {
    private Map<String, RideCounterRecord> records;
    private String playerIdentifier;

    public RideCounterRecordCollection(Map<String, RideCounterRecord> records, String playerIdentifier) {
        this.records = records;
        this.playerIdentifier = playerIdentifier;
    }

    public RideCounterRecordCollection(String playerIdentifier) {
        this.records = new HashMap<>();
        this.playerIdentifier = playerIdentifier;
    }

    public RideCounterRecord findOrCreate(String rideIdentifier, JRidesPlayer player){
        RideCounterRecord record = this.records.get(rideIdentifier);
        if(record == null){
            Ride ride = ServiceProvider.getSingleton(RideManager.class)
                    .getRideHandle(rideIdentifier)
                    .getRide();

            record = new RideCounterRecord(ride, player.getName(), player.getUniqueId(), 0);
            this.records.put(rideIdentifier, record);
        }

        return record;
    }

    public Map<String, RideCounterRecord> getRecords() {
        return records;
    }

    public String getPlayerIdentifier() {
        return playerIdentifier;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> config = new HashMap<>();

        config.put("records", List.of(this.records.values().toArray()));
        config.put("playerIdentifier", this.playerIdentifier);

        return config;
    }

    public static RideCounterRecordCollection deserialize(Map<String, Object> config){
        List<RideCounterRecord> recordsList = (List<RideCounterRecord>) config.get("records");
        String playerIdentifier = (String) config.get("playerIdentifier");

        Map<String, RideCounterRecord> records = recordsList.stream()
                .collect(Collectors.toMap(RideCounterRecord::getRideIdentifier, r->r));

        return new RideCounterRecordCollection(records, playerIdentifier);
    }
}
