package com.jverbruggen.jrides.models.map.rideoverview;

import com.jverbruggen.jrides.animator.TrainHandle;
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
