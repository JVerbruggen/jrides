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
import com.jverbruggen.jrides.api.JRidesRide;
import com.jverbruggen.jrides.event.player.PlayerQuitEvent;
import com.jverbruggen.jrides.event.ride.RideFinishedEvent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideCounterManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RideCounterListener implements Listener {
    private final RideCounterManager rideCounterManager;

    public RideCounterListener() {
        rideCounterManager = ServiceProvider.getSingleton(RideCounterManager.class);
    }

    @EventHandler
    public void onRideFinish(RideFinishedEvent event) {
        JRidesRide ride = event.getRide();
        RideCounterRecordRideCollection rideBoundCollection = rideCounterManager.getCollectionRideBound(ride.getIdentifier());

        for (JRidesPlayer player : event.getPlayers()) {
            RideCounterRecordCollection collection = rideCounterManager.getCollection(player.getIdentifier());
            RideCounterRecord record = collection.findOrCreate(ride.getIdentifier(), player);
            record.addOne();

            RideCounterRecord rideBoundRecord = rideBoundCollection.findOrCreate(player);
            rideBoundRecord.set(record.getRideCount());

            rideCounterManager.sendRideCounterUpdateMessage(player, record);
            rideCounterManager.saveToFile(player.getIdentifier(), collection);
        }

        rideCounterManager.saveToRideFile(ride.getIdentifier(), rideBoundCollection);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        JRidesPlayer player = event.getPlayer();

        rideCounterManager.saveAndUnload(player.getIdentifier());
    }
}
