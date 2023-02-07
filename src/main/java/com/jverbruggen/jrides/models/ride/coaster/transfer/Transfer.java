package com.jverbruggen.jrides.models.ride.coaster.transfer;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.train.Cart;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.SectionReference;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Transfer {
    private TrainHandle train;
    private Vector3 currentLocation;
    private Quaternion currentOrientation;

    private boolean requestPending;
    private boolean moving;
    private boolean locked;
    private List<CartOffsetFromTransferOrigin> cartPositions;

    private List<TransferPosition> possiblePositions;
    private TransferPosition targetPosition;
    private Vector3 fromLocation;
    private Quaternion fromOrientation;

    private int animationFrameState;

    private Vector3 bakedOffsetLocation;
    private Quaternion bakedOffsetOrientation;

    private VirtualArmorstand modelArmorstand;
    private Vector3 modelOffset;

    public Transfer(List<TransferPosition> possiblePositions, VirtualArmorstand modelArmorstand, Vector3 modelOffset) {
        this.locked = false;
        this.moving = false;
        this.requestPending = false;
        this.cartPositions = new ArrayList<>();
        this.possiblePositions = possiblePositions;

        TransferPosition origin = possiblePositions.get(0);
        this.currentLocation = origin.getLocation();
        this.currentOrientation = origin.getOrientation();

        this.fromLocation = null;
        this.fromOrientation = null;
        this.targetPosition = null;
        this.animationFrameState = 0;

        this.bakedOffsetLocation = new Vector3(0,0,0);
        this.bakedOffsetOrientation = Quaternion.fromAnglesVector(new Vector3(0,0,0));

        this.modelArmorstand = modelArmorstand;
        this.modelOffset = modelOffset;
    }

    public void lockTrain(){
        if(!hasTrain()) throw new RuntimeException("Cannot lock train on transfer if no train is present");
        this.locked = true;

        for(Cart cart : train.getTrain().getCarts()){
            Vector3 currentCartPosition = cart.getPosition();
            Quaternion currentCartOrientation = cart.getOrientation();
            if(currentCartOrientation == null) throw new RuntimeException("Cart doesn't have orientation");

            Vector3 cartOffset = Vector3.subtract(currentCartPosition, currentLocation);

            Bukkit.broadcastMessage("CARTOFFSET: " + cartOffset + " ROT " + currentCartOrientation);

            cartPositions.add(new CartOffsetFromTransferOrigin(cartOffset, currentCartOrientation, cart));
        }

        Bukkit.broadcastMessage("lock");
    }

    public void unlockTrain(){
        if(!hasTrain()) throw new RuntimeException("Cannot unlock train on transfer if no train is present");
        this.locked = false;
        this.calculateOffset();
        Bukkit.broadcastMessage("unlock");
    }

    public void setTrain(TrainHandle train){
        if(this.locked)
            throw new RuntimeException("Cannot set train if transfer is locked");

        if(this.train != null && train != null && this.train != train)
            throw new RuntimeException("Cannot have multiple trains on same transfer");

        this.train = train;
        Bukkit.broadcastMessage("transfer set: " + train);
    }

    private boolean hasTrain(){
        return train != null;
    }

    public boolean tick(){
        if(moving){
            return doMoveTick();
        }
        return false;
    }

    private boolean doMoveTick(){
        int totalTicks = targetPosition.getMoveTicks();
        if(animationFrameState >= totalTicks){
//            move(targetPosition); // Hard move
            animationFrameState = 0;
            moving = false;
            return true;
        }else{
            // -- Calculate new transfer position

            double theta = (double)animationFrameState / (double)totalTicks;
            if(theta > 1) theta = 1d;
            Vector3 delta = Vector3.subtract(targetPosition.getLocation(), fromLocation);

            Vector3 increment = Vector3.multiply(delta, theta);
            Vector3 newLocation = Vector3.add(fromLocation, increment);
            Quaternion newOrientation = Quaternion.lerp(fromOrientation, targetPosition.getOrientation(), theta);

            move(newLocation, newOrientation);
            animationFrameState++;
            return false;
        }
    }

    private void move(Vector3 newLocation, Quaternion newOrientation){
        if(!locked && hasTrain()) throw new RuntimeException("Cannot move transfer if train is not locked");

        currentLocation = newLocation;
        currentOrientation = newOrientation;
        modelArmorstand.setLocation(Vector3.add(newLocation, modelOffset), newOrientation);

        if(hasTrain()){
            moveTrain(currentLocation, currentOrientation);
        }
    }

    private void moveTrain(Vector3 currentLocation, Quaternion currentOrientation){
        Matrix4x4 orientationMatrix = new Matrix4x4();
        orientationMatrix.rotate(currentOrientation);

        for(CartOffsetFromTransferOrigin cartProgramming : cartPositions){
            Quaternion cartOrientation = cartProgramming.getOrientation();
            Vector3 cartPosition = cartProgramming.getPosition();
            Quaternion cartOrientationInvert = cartOrientation.clone();
            cartOrientationInvert.invert();
            Vector3 cartPositionInvert = cartPosition.negate();

            orientationMatrix.rotate(cartOrientation);
            orientationMatrix.translate(cartPosition);

            Vector3 newAbsoluteCartPosition = Vector3.add(currentLocation, orientationMatrix.toVector3());
            cartProgramming.getCart().setPosition(newAbsoluteCartPosition, orientationMatrix.getRotation());

            orientationMatrix.translate(cartPositionInvert);
            orientationMatrix.rotate(cartOrientationInvert);
        }
    }

    public Vector3 getCurrentLocation() {
        return currentLocation;
    }

    public List<TransferPosition> getPossiblePositions() {
        return possiblePositions;
    }

    public void setTargetPosition(int i, boolean requestPending){
        if(this.requestPending)
            throw new RuntimeException("Cannot change target position if another request is still pending");

        fromLocation = currentLocation.clone();
        fromOrientation = currentOrientation.clone();
        targetPosition = possiblePositions.get(i);
        animationFrameState = 0;
        moving = true;

        if(requestPending)
            this.requestPending = true;
    }

    public boolean hasReachedRequest(){
        return requestPending && !moving;
    }

    public void releaseRequest(){
        if(!this.requestPending)
            throw new RuntimeException("No request was pending when releasing");

        this.requestPending = false;
    }

    private void calculateOffset(){
        TransferPosition origin = possiblePositions.get(0);
        bakedOffsetLocation = Vector3.subtract(currentLocation, origin.getLocation());
        bakedOffsetOrientation = Quaternion.diff(origin.getOrientation(), currentOrientation);
    }

    public Vector3 getOffset(){
        return bakedOffsetLocation;
    }

    public TransferPosition getCurrentTransferPosition(){
        return targetPosition;
    }

    public void populateTransferPositionSections(Map<SectionReference, Section> sectionMap){
        for(TransferPosition position : possiblePositions){
            String sectionAtStartReference = position.getSectionAtStartReference();
            String sectionAtEndReference = position.getSectionAtEndReference();

            Section sectionAtStart = null;
            Section sectionAtEnd = null;

            if(sectionAtStartReference != null && !sectionAtStartReference.equals(""))
                sectionAtStart = SectionReference.findByIdentifier(sectionAtStartReference, sectionMap);

            if(sectionAtEndReference != null && !sectionAtEndReference.equals(""))
                sectionAtEnd = SectionReference.findByIdentifier(sectionAtEndReference, sectionMap);

            position.setSectionAtStart(sectionAtStart);
            position.setSectionAtEnd(sectionAtEnd);
        }
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
