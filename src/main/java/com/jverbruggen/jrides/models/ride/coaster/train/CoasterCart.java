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

package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovement;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.math.VectorQuaternionState;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.seat.SeatHost;


public interface CoasterCart extends SeatHost {
    static VectorQuaternionState calculateLocation(Vector3 trackLocation, Vector3 cartOffset, Quaternion orientation, Quaternion cartRotationOffset){
        Matrix4x4 matrix = new Matrix4x4();
        matrix.rotate(orientation);
        matrix.translate(cartOffset);
        matrix.rotate(cartRotationOffset);
        Vector3 cartTrackOffsetVector = matrix.toVector3();
        Vector3 totalVector = Vector3.add(trackLocation, cartTrackOffsetVector);

//        final Vector3 armorstandHeightCompensationVector = getArmorstandHeightCompensationVector();
//        totalVector = Vector3.add(totalVector, armorstandHeightCompensationVector);
//
        return new VectorQuaternionState(totalVector, matrix.getRotation());
    }

//    static Vector3 getArmorstandHeightCompensationVector(){
//        return new Vector3(0, -1.8, 0);
//    }

    String getName();
    Frame getFrame();
    int getWheelDistance();
    Vector3 getTrackOffset();
    Vector3 getPosition();

    /**
     * Total orientation of the cart (track rotation + rotation offset)
     * @return
     */
    Quaternion getOrientation();

    /**
     * Get individual rotation offset (usually a state of rotating the carts along the track)
     * @return
     */
    Quaternion getRotationOffset();
    void setPosition(Vector3 position, Quaternion orientation);
    void setPosition(Vector3 position);
    void setPosition(CartMovement cartMovement);
    void setParentTrain(Train train);
    Train getParentTrain();
    boolean shouldFaceForwards();
    void setInvertedFrameAddition(boolean inverted);
    void updateCustomOrientationOffset(Vector3 orientationOffset);

    void setNextEffect(TrainEffectTriggerHandle nextEffect);
    void playEffects();

}
