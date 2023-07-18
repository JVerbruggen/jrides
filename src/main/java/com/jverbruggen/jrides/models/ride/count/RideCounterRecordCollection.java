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
