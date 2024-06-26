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

package com.jverbruggen.jrides.models.map.ridecounter;

import com.jverbruggen.jrides.event.ride.RideFinishedEvent;
import com.jverbruggen.jrides.event.ride.RideInitializedEvent;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecord;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecordCollection;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecordRideCollection;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideCounterManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RideCounterMapListener implements Listener {

    private final RideCounterMapFactory rideCounterMapFactory;
    private final RideCounterManager rideCounterManager;

    public RideCounterMapListener() {
        this.rideCounterMapFactory = ServiceProvider.getSingleton(RideCounterMapFactory.class);
        this.rideCounterManager = ServiceProvider.getSingleton(RideCounterManager.class);
    }

    @EventHandler
    public void onRidesInitialized(RideInitializedEvent e){
        rideCounterMapFactory.initializeMaps();
    }

    // TODO: Update this functionality to add support for all ride counter map types
    @EventHandler
    public void onRideFinished(RideFinishedEvent e) {
        RideCounterRecordRideCollection rideBoundCollection = rideCounterManager.getCollectionRideBound(e.getRide().getIdentifier());
        int lineCount = rideCounterMapFactory.getLineCount(e.getRide().getIdentifier(), RideCounterMapType.TOP);

        // For each player in the event, check if they should be in the top map
        for (Player player : e.getPlayers()) {
            if(player == null || !player.getBukkitPlayer().isOnline()) continue;

            // If the player is already in the list, update the record
            if (rideBoundCollection.existsOn(player)) {
                rideBoundCollection.update(getPlayerRecord(player, e.getRide().getIdentifier()));
            } else {
                // If the list is not full, add the player
                if (rideBoundCollection.getRecords().size() < lineCount) {
                    rideBoundCollection.add(getPlayerRecord(player, e.getRide().getIdentifier()));
                }else {
                    // Loop through the records and find the lowest one
                    int lowest = 0;
                    for (int i = 0; i < rideBoundCollection.getRecords().size(); i++) {
                        if (rideBoundCollection.getRecords().get(i).getRideCount() < rideBoundCollection.getRecords().get(lowest).getRideCount()) {
                            lowest = i;
                        }
                    }
                    // If the player has more rides than the lowest, add them to the list
                    if(rideBoundCollection.getRecords().size() > lowest) {
                        if (getPlayerRecord(player, e.getRide().getIdentifier()).getRideCount() > rideBoundCollection.getRecords().get(lowest).getRideCount()) {
                            rideBoundCollection.add(player.getRideCounters().getRecords().get(e.getRide().getIdentifier()));
                        }
                    }
                }
            }
        }

        // Sort the records by ride count
        rideBoundCollection.getRecords().sort((o1, o2) -> o2.getRideCount() - o1.getRideCount());

        // If the list is too long, remove the records with the lowest ride count to match the line count
        while (rideBoundCollection.getRecords().size() > lineCount) {
            int lowest = 0;
            for (int i = 0; i < rideBoundCollection.getRecords().size(); i++) {
                if (rideBoundCollection.getRecords().get(i).getRideCount() < rideBoundCollection.getRecords().get(lowest).getRideCount()) {
                    lowest = i;
                }
            }
            rideBoundCollection.getRecords().remove(lowest);
        }

        // Save the ride counter collection
        rideCounterManager.saveToRideFile(e.getRide().getIdentifier(), rideBoundCollection);
    }

    private RideCounterRecord getPlayerRecord(Player player, String rideIdentifier){
        RideCounterRecordCollection collection = rideCounterManager.getCollection(player.getIdentifier());
        return collection.findOrCreate(rideIdentifier, player);
    }

}
