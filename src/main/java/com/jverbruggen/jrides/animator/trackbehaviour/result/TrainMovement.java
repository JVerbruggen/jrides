package com.jverbruggen.jrides.animator.trackbehaviour.result;

import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.Cart;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class TrainMovement {
    private final Speed newSpeed;
    private final Frame newMassMiddleFrame;
    private final Vector3 newTrainLocation;
    private final HashMap<Cart, CartMovement> cartMovements;

    public TrainMovement(Speed newSpeed, Frame newMassMiddleFrame, Vector3 newTrainLocation, HashMap<Cart, CartMovement> cartMovements) {
        this.newSpeed = newSpeed;
        this.newMassMiddleFrame = newMassMiddleFrame;
        this.newTrainLocation = newTrainLocation;
        this.cartMovements = cartMovements;
    }

    public Speed getNewSpeed() {
        return newSpeed;
    }

    public Frame getNewMassMiddleFrame() {
        return newMassMiddleFrame;
    }

    public Vector3 getNewTrainLocation() {
        return newTrainLocation;
    }

    public Set<Map.Entry<Cart, CartMovement>> getCartMovements() {
        return cartMovements.entrySet();
    }
}
