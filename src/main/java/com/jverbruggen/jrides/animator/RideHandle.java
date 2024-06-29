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

package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.config.coaster.objects.SoundsConfig;
import com.jverbruggen.jrides.config.ride.RideState;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Passenger;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecord;

import javax.annotation.Nonnull;
import java.util.List;

public interface RideHandle {
    void tick();
    Ride getRide();
    RideController getRideController();
    Menu getRideControlMenu();
    PlayerLocation getCustomEjectLocation();

    void setRideController(RideController rideController, Menu rideControlMenu);
    DispatchTrigger getDispatchTrigger();
    TriggerContext getTriggerContext(@Nonnull String contextOwner);
    TriggerContext getFirstTriggerContext();

    PlayerLocation getEjectLocation();
    List<StationHandle> getStationHandles();

    List<RideCounterRecord> getTopRideCounters();
    List<Passenger> getPassengers();

    void setState(RideState state);
    RideState getState();
    boolean isOpen();

    void open(Player authority);
    void close(Player authority);
    boolean canFullyClose();

    void broadcastRideOpen();
    void broadcastRideClose();

    void unload(boolean save);
    boolean isLoaded();

    SoundsConfig getSounds();
}
