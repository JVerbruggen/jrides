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

package com.jverbruggen.jrides.state.ride.menu;

import com.jverbruggen.jrides.event.ride.RideInitializedEvent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RideMenuLoader implements Listener {
    private final RideOverviewMenuFactory rideOverviewMenuFactory;
    private final RideManager rideManager;

    public RideMenuLoader(){
        rideOverviewMenuFactory = ServiceProvider.getSingleton(RideOverviewMenuFactory.class);
        rideManager = ServiceProvider.getSingleton(RideManager.class);
    }

    @EventHandler
    public void onRidesInitialized(RideInitializedEvent event){
        rideOverviewMenuFactory.createRideOverviewMenu(rideManager.getRideHandles());
    }
}
