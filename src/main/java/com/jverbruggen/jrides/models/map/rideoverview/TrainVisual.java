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

package com.jverbruggen.jrides.models.map.rideoverview;

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import dev.cerus.maps.api.Marker;

public class TrainVisual {
    private final TrainHandle trainHandle;
    private final MapScope mapScope;
    private Marker marker;

    public TrainVisual(TrainHandle trainHandle, MapScope mapScope) {
        this.trainHandle = trainHandle;
        this.mapScope = mapScope;
        this.marker = new Marker(0,0,
                (byte)0, // Marker direction
                (byte)4, // Marker type
                true);
    }

    public int getX(){
        return (int) trainHandle.getTrain().getCurrentLocation().getX();
    }

    public int getZ(){
        return (int) trainHandle.getTrain().getCurrentLocation().getZ();
    }

    public void update(){
        int markerX = mapScope.toScreenXMarker(getX());
        int markerZ = mapScope.toScreenZMarker(getZ());

        marker.setX(markerX);
        marker.setY(markerZ);
    }

    public Marker getMarker(){
        return marker;
    }
}
