package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.Seat;

import java.util.List;

public interface Cart {
    List<Seat> getSeats();
    List<Player> getPassengers();
    int getMassMiddleOffset();
    Vector3 getTrackOffset();
    void setPosition(Vector3 position, Quaternion orientation);
    void setPosition(Vector3 position);

    static Vector3 calculateLocation(Vector3 trackLocation, Vector3 cartOffset, Quaternion orientation){
        Matrix4x4 matrix = new Matrix4x4();
        matrix.rotate(orientation);
        matrix.translate(cartOffset);
        Vector3 cartTrackOffsetVector = matrix.toVector3();
        Vector3 totalVector = Vector3.add(trackLocation, cartTrackOffsetVector);

        final Vector3 armorstandHeightCompensationVector = new Vector3(0, -0.8, 0);
        totalVector = Vector3.add(totalVector, armorstandHeightCompensationVector);

        return totalVector;
    }
}
