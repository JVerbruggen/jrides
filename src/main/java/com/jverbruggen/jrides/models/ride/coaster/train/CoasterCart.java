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

        final Vector3 armorstandHeightCompensationVector = getArmorstandHeightCompensationVector();
        totalVector = Vector3.add(totalVector, armorstandHeightCompensationVector);

        return new VectorQuaternionState(totalVector, matrix.getRotation());
    }

    static Vector3 getArmorstandHeightCompensationVector(){
        return new Vector3(0, -1.8, 0);
    }

    String getName();
    Frame getFrame();
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

    void setNextEffect(TrainEffectTriggerHandle nextEffect);
    void playEffects();

}
