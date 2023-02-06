package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.train.Cart;

import java.util.ArrayList;
import java.util.List;

public class Transfer {
    private TrainHandle train;
    private Vector3 currentLocation;
    private boolean locked;
    private List<CartOffsetFromTransferOrigin> cartPositions;

    public Transfer() {
        this.locked = false;
        this.cartPositions = new ArrayList<>();
    }

    public void lockTrain(){
        if(!hasTrain()) throw new RuntimeException("Cannot lock train on transfer if no train is present");
        this.locked = true;

        for(Cart cart : train.getTrain().getCarts()){
            Vector3 currentCartPosition = cart.getPosition();
            Quaternion currentCartOrientation = cart.getOrientation();
            if(currentCartOrientation == null) throw new RuntimeException("Cart doesn't have orientation");

            cartPositions.add(new CartOffsetFromTransferOrigin(currentCartPosition, currentCartOrientation, cart));
        }
    }

    public void unlockTrain(){
        if(!hasTrain()) throw new RuntimeException("Cannot unlock train on transfer if no train is present");
        this.locked = false;
    }

    public void setTrain(TrainHandle train){
        if(this.train != null && train != null && this.train != train)
            throw new RuntimeException("Cannot have multiple trains on same transfer");

        this.train = train;
    }

    private boolean hasTrain(){
        return train != null;
    }

    public void move(Vector3 position, Quaternion orientation){
        if(!locked && hasTrain()) throw new RuntimeException("Cannot move transfer if train is not locked");

        if(hasTrain()){
            moveTrain(position, orientation);
        }
    }

    private void moveTrain(Vector3 position, Quaternion orientation){
        Matrix4x4 orientationMatrix = new Matrix4x4();
        orientationMatrix.rotate(orientation);

        for(CartOffsetFromTransferOrigin cartProgramming : cartPositions){
            Quaternion cartOrientation = cartProgramming.getOrientation();
            Vector3 cartPosition = cartProgramming.getPosition();
            Quaternion cartOrientationInvert = cartOrientation.clone();
            cartOrientationInvert.invert();
            Vector3 cartPositionInvert = cartPosition.negate();

            orientationMatrix.rotate(cartOrientation);
            orientationMatrix.translate(cartPosition);

            Vector3 newAbsoluteCartPosition = Vector3.add(position, orientationMatrix.toVector3());
            cartProgramming.getCart().setPosition(newAbsoluteCartPosition);

            orientationMatrix.translate(cartPositionInvert);
            orientationMatrix.rotate(cartOrientationInvert);
        }
    }

    public Vector3 getCurrentLocation() {
        return currentLocation;
    }
}

class CartOffsetFromTransferOrigin{
    private final Vector3 position;
    private final Quaternion orientation;
    private final Cart cart;

    CartOffsetFromTransferOrigin(Vector3 position, Quaternion orientation, Cart cart) {
        this.position = position;
        this.orientation = orientation;
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Quaternion getOrientation() {
        return orientation;
    }
}
