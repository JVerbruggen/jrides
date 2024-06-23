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

package com.jverbruggen.jrides.api;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;

import java.util.List;

public class RidesAPI {
    private final RideManager rideManager;

    public RidesAPI(){
        this.rideManager = ServiceProvider.getSingleton(RideManager.class);
    }

    public List<RideHandle> getRides(){
        return this.rideManager.getRideHandles();
    }

    public RideHandle getRide(String rideIdentifier){
        return this.rideManager.getRideHandle(rideIdentifier);
    }
}
