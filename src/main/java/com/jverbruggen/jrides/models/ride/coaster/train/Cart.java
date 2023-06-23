package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovement;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.Seat;

import java.util.List;

public interface Cart {
    static Vector3 calculateLocation(Vector3 trackLocation, Vector3 cartOffset, Quaternion orientation){
        Matrix4x4 matrix = new Matrix4x4();
        matrix.rotate(orientation);
        matrix.translate(cartOffset);
        Vector3 cartTrackOffsetVector = matrix.toVector3();
        Vector3 totalVector = Vector3.add(trackLocation, cartTrackOffsetVector);

        final Vector3 armorstandHeightCompensationVector = getArmorstandHeightCompensationVector();
        totalVector = Vector3.add(totalVector, armorstandHeightCompensationVector);

        return totalVector;
    }

    static Vector3 getArmorstandHeightCompensationVector(){
        return new Vector3(0, -1.8, 0);
    }

    String getName();
    List<Seat> getSeats();
    List<Player> getPassengers();
    Frame getFrame();
    Vector3 getTrackOffset();
    Vector3 getPosition();
    Quaternion getOrientation();
    void setPosition(Vector3 position, Quaternion orientation);
    void setPosition(Vector3 position);
    void setPosition(CartMovement cartMovement);
    void setRestraint(boolean locked);
    boolean getRestraintState();
    void setParentTrain(Train train);
    Train getParentTrain();
    void ejectPassengers();
    boolean shouldFaceForwards();
    void setInvertedFrameAddition(boolean inverted);

    void setNextEffect(TrainEffectTriggerHandle nextEffect);
    void playEffects();

    void despawn();
}
