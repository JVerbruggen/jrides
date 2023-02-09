package com.jverbruggen.jrides.models.ride.coaster.transfer;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.*;
import com.jverbruggen.jrides.models.properties.Frame;
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
    private Matrix4x4 currentRotationMatrix;

    private boolean requestPending;
    private boolean moving;
    private boolean locked;
    private List<CartOffsetFromTransferOrigin> cartPositions;

    private List<TransferPosition> possiblePositions;
    private TransferPosition targetPosition;
    private Vector3 fromLocation;
    private Quaternion fromOrientation;

    private int animationTicks;
    private int animationFrameState;

    private Vector3 bakedOffsetLocation;
    private Quaternion bakedOffsetOrientation;
    private Matrix4x4 bakedOffsetRotationMatrix;

    private VirtualArmorstand modelArmorstand;
    private Vector3 modelOffset;
    private Vector3 modelOffsetRotation;

    public Transfer(List<TransferPosition> possiblePositions, VirtualArmorstand modelArmorstand, Vector3 modelOffset, Vector3 modelOffsetRotation) {
        this.locked = false;
        this.moving = false;
        this.requestPending = false;
        this.cartPositions = new ArrayList<>();
        this.possiblePositions = possiblePositions;

        TransferPosition origin = possiblePositions.get(0);
        this.currentLocation = origin.getLocation();
        this.currentOrientation = origin.getOrientation();
        this.currentRotationMatrix = calculateRotationMatrix(currentLocation, currentOrientation);

        this.fromLocation = null;
        this.fromOrientation = null;
        this.targetPosition = origin;
        this.animationTicks = 0;
        this.animationFrameState = 0;

        calculateBakedOffset();

        this.modelArmorstand = modelArmorstand;
        this.modelOffset = modelOffset;
        this.modelOffsetRotation = modelOffsetRotation;

        updateModelPosition();
    }

    public void lockTrain(){
        if(!hasTrain()) throw new RuntimeException("Cannot lock train on transfer if no train is present");
        this.locked = true;

        Matrix4x4 rotationMatrix = new Matrix4x4();
        rotationMatrix.translate(currentLocation);
        rotationMatrix.rotate(currentOrientation);

        for(Cart cart : train.getTrain().getCarts()){
            Quaternion currentCartOrientation = cart.getOrientation();
            if(currentCartOrientation == null) throw new RuntimeException("Cart doesn't have orientation");

            Frame cartFrame = cart.getFrame();
            Vector3 nonRotatedCartPosition = cartFrame.getTrack().getLocationFor(cartFrame);
            nonRotatedCartPosition = Vector3.add(nonRotatedCartPosition, cart.getTrackOffset());
            nonRotatedCartPosition = Vector3.add(nonRotatedCartPosition, Cart.getArmorstandHeightCompensationVector());

            Vector3 offsetCartPosition = Vector3.subtract(nonRotatedCartPosition, currentLocation);

            Quaternion orientationOffset = Quaternion.divide(currentCartOrientation, currentOrientation);
            cartPositions.add(new CartOffsetFromTransferOrigin(offsetCartPosition, orientationOffset, cart));
        }
    }

    public void unlockTrain(){
        if(!hasTrain()) throw new RuntimeException("Cannot unlock train on transfer if no train is present");
        this.locked = false;
        this.calculateBakedOffset();
        this.cartPositions.clear();
    }

    public void setTrain(TrainHandle train){
        if(this.locked)
            throw new RuntimeException("Cannot set train if transfer is locked");

        if(this.train != null && train != null && this.train != train)
            throw new RuntimeException("Cannot have multiple trains on same transfer");

        this.train = train;
    }

    private boolean hasTrain(){
        return train != null;
    }

    public TrainHandle getTrain(){
        return train;
    }

    public boolean tick(){
        if(moving){
            return doMoveTick();
        }
        return false;
    }

    private boolean doMoveTick(){
        if(animationFrameState >= animationTicks){
            move(targetPosition.getLocation().clone(), targetPosition.getOrientation().clone());

            animationFrameState = 0;
            moving = false;
            this.calculateBakedOffset();
            return true;
        }else{
            // -- Calculate new transfer position
            double theta = (double)animationFrameState / (double)animationTicks;
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
        currentRotationMatrix = calculateRotationMatrix(newLocation, newOrientation);
        updateModelPosition();

        if(hasTrain()){
            moveTrain();
        }
    }

    private void updateModelPosition(){
        Vector3 armorstandCompenstationVector = Cart.getArmorstandHeightCompensationVector();
        Vector3 modelOffsetCompensated = Vector3.subtract(modelOffset, armorstandCompenstationVector);

        Matrix4x4 orientationMatrix = new Matrix4x4();
        orientationMatrix.translate(getCurrentLocation());
        orientationMatrix.translate(armorstandCompenstationVector);
        orientationMatrix.rotate(getCurrentOrientation());
        orientationMatrix.translate(modelOffsetCompensated);

        Quaternion modelOrientation = orientationMatrix.getRotation();

        Vector3 modelLocation = orientationMatrix.toVector3();

        modelArmorstand.setLocation(modelLocation, null);
        modelOrientation.rotateYawPitchRoll(modelOffsetRotation);
        modelArmorstand.setHeadpose(ArmorStandPose.getArmorStandPose(modelOrientation));
    }

    private void moveTrain(){
        Matrix4x4 matrix = new Matrix4x4();
        matrix.translate(getCurrentLocation());
        Vector3 armorstandCompenstationVector = Cart.getArmorstandHeightCompensationVector();

        for(CartOffsetFromTransferOrigin cartProgramming : cartPositions){
            Quaternion cartOrientation = cartProgramming.getOrientation();
            Vector3 cartPosition = cartProgramming.getPosition();
            Quaternion cartOrientationInvert = cartOrientation.clone();
            cartOrientationInvert.invert();
            Vector3 cartPositionInvert = cartPosition.negate();

            matrix.translate(armorstandCompenstationVector);
            matrix.rotate(getCurrentOrientation());
            matrix.translate(Vector3.subtract(cartPosition, armorstandCompenstationVector));

            Quaternion newCartOrientation = matrix.getRotation().clone();
            newCartOrientation.multiply(cartOrientation);

            cartProgramming.getCart().setPosition(matrix.toVector3(), newCartOrientation);

            Quaternion inverted = getCurrentOrientation().clone();
            inverted.invert();
            matrix.translate(cartPositionInvert);
            matrix.rotate(inverted);
        }
    }

    public Vector3 getCurrentLocation() {
        return currentLocation;
    }

    public Quaternion getCurrentOrientation(){
        return currentOrientation;
    }

    public List<TransferPosition> getPossiblePositions() {
        return possiblePositions;
    }

    public void setTargetPosition(int i, boolean requestPending){
        if(this.requestPending)
            throw new RuntimeException("Cannot change target position if another request is still pending");

        TransferPosition newTargetPosition = possiblePositions.get(i);
        if(targetPosition == newTargetPosition)
            return;

        int fromTicks = 0;
        if(targetPosition != null)
            fromTicks = targetPosition.getMoveTicks();

        fromLocation = currentLocation.clone();
        fromOrientation = currentOrientation.clone();
        targetPosition = newTargetPosition;
        animationFrameState = 0;
        animationTicks = Math.abs(targetPosition.getMoveTicks() - fromTicks);
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

    private void calculateBakedOffset(){
        TransferPosition origin = possiblePositions.get(0);
        bakedOffsetLocation = Vector3.subtract(currentLocation, origin.getLocation());
        bakedOffsetOrientation = Quaternion.diff(origin.getOrientation(), currentOrientation);
    }

    public Vector3 getOffsetLocation(){
        return bakedOffsetLocation;
    }

    public Quaternion getOffsetOrientation(){
        return bakedOffsetOrientation;
    }

    public Matrix4x4 getOffsetRotationMatrix(){
        return currentRotationMatrix;
    }

    private Matrix4x4 calculateRotationMatrix(Vector3 location, Quaternion orientation){
        Matrix4x4 orientationMatrix = new Matrix4x4();
        orientationMatrix.translate(location);
        orientationMatrix.rotate(orientation);
        return orientationMatrix;
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

    public void trainExitedTransfer(){
        setTrain(null);
        setTargetPosition(0, false);
    }

    public VectorQuaternionState getOrigin(){
        TransferPosition position = possiblePositions.get(0);
        return new VectorQuaternionState(position.getLocation(), position.getOrientation());
    }

    public boolean isMoving(){
        return moving;
    }

    public boolean canSafelyInteractWith(TrainHandle train){
        if(train == null) return false;
        if(hasTrain()){
            return getTrain() == train;
        }
        return !isMoving();
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
