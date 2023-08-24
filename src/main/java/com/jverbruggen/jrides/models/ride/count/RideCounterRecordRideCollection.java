package com.jverbruggen.jrides.models.ride.count;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

/**
 * Ride-bound ride counter record collection.
 * Used for saving 'top x records' bound to a ride.
 * Only a few records apply for being present on this board.
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

    public boolean existsOn(JRidesPlayer player){
        Optional<RideCounterRecord> record = this.records.stream().filter(r -> r.getPlayerUUID().equals(player.getUniqueId())).findFirst();
        return record.isPresent();
    }

    public boolean existsOn(RideCounterRecord record){
        return this.records.stream().anyMatch(r -> r.sameIdentityAs(record));
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
