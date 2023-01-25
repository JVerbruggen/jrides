package com.jverbruggen.jrides.animator.trackbehaviour.result;

import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.Cart;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class TrainMovement {
    private final Speed newSpeed;
    private final Frame newHeadOfTrainFrame;
    private final Vector3 newTrainLocation;
    private final HashMap<Cart, CartMovement> cartMovements;

    public TrainMovement(Speed newSpeed, Frame newHeadOfTrainFrame, Vector3 newTrainLocation, HashMap<Cart, CartMovement> cartMovements) {
        this.newSpeed = newSpeed;
        this.newHeadOfTrainFrame = newHeadOfTrainFrame;
        this.newTrainLocation = newTrainLocation;
        this.cartMovements = cartMovements;
    }

    public Speed getNewSpeed() {
        return newSpeed;
    }

    public Frame getNewHeadOfTrainFrame() {
        return newHeadOfTrainFrame;
    }

    public Vector3 getNewTrainLocation() {
        return newTrainLocation;
    }

    public @Nullable Set<Map.Entry<Cart, CartMovement>> getCartMovements() {
        if(cartMovements == null) return null;
        return cartMovements.entrySet();
    }
}
