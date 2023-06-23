package com.jverbruggen.jrides.animator.coaster.trackbehaviour.result;

import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.train.Cart;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class TrainMovement {
    private final Speed newSpeed;
    private final Frame newHeadOfTrainFrame;
    private final Frame newMiddleOfTrainFrame;
    private final Frame newTailOfTrainFrame;
    private final Vector3 newTrainLocation;
    private final HashMap<Cart, CartMovement> cartMovements;

    public TrainMovement(Speed newSpeed, Frame newHeadOfTrainFrame, Frame newMiddleOfTrainFrame, Frame newTailOfTrainFrame, Vector3 newTrainLocation, HashMap<Cart, CartMovement> cartMovements) {
        this.newSpeed = newSpeed;
        this.newHeadOfTrainFrame = newHeadOfTrainFrame;
        this.newMiddleOfTrainFrame = newMiddleOfTrainFrame;
        this.newTailOfTrainFrame = newTailOfTrainFrame;
        this.newTrainLocation = newTrainLocation;
        this.cartMovements = cartMovements;
    }

    public Speed getNewSpeed() {
        return newSpeed;
    }

    public Frame getNewHeadOfTrainFrame() {
        return newHeadOfTrainFrame;
    }

    public Frame getNewMiddleOfTrainFrame() {
        return newMiddleOfTrainFrame;
    }

    public Frame getNewTailOfTrainFrame() {
        return newTailOfTrainFrame;
    }

    public Vector3 getNewTrainLocation() {
        return newTrainLocation;
    }

    public @Nullable Set<Map.Entry<Cart, CartMovement>> getCartMovements() {
        if(cartMovements == null) return null;
        return cartMovements.entrySet();
    }
}
