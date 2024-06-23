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

package com.jverbruggen.jrides.models.ride.coaster.transfer;

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.*;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.Unlockable;
import com.jverbruggen.jrides.models.ride.section.reference.SectionReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Transfer implements Unlockable {
    private final Vector3 origin;

    private TrainHandle train;
    private Vector3 currentLocation;
    private Quaternion currentOrientation;
    private Matrix4x4 currentRotationMatrix;

    private boolean requestPending;
    private boolean moving;
    private boolean locked;
    private final List<CartOffsetFromTransferOrigin> cartPositions;

    private final List<TransferPosition> possiblePositions;
    private TransferPosition targetPosition;
    private Vector3 fromLocation;
    private Quaternion fromOrientation;

    private int animationTicks;
    private int animationFrameState;

    private Vector3 bakedOffsetLocation;
    private Quaternion bakedOffsetOrientation;

    private final VirtualEntity modelEntity;
    private final Vector3 modelOffset;
    private final Vector3 modelOffsetRotation;

    public Transfer(List<TransferPosition> possiblePositions, VirtualEntity modelEntity, Vector3 origin, Vector3 modelOffset, Vector3 modelOffsetRotation) {
        this.origin = origin;
        this.locked = false;
        this.moving = false;
        this.requestPending = false;
        this.cartPositions = new ArrayList<>();
        this.possiblePositions = possiblePositions;

        TransferPosition firstTransferposition = possiblePositions.get(0);
        this.currentLocation = firstTransferposition.getLocation();
        this.currentOrientation = firstTransferposition.getOrientation();
        this.currentRotationMatrix = calculateRotationMatrix(currentLocation, currentOrientation);

        this.fromLocation = null;
        this.fromOrientation = null;
        this.targetPosition = firstTransferposition;
        this.animationTicks = 0;
        this.animationFrameState = 0;

        calculateBakedOffset();

        this.modelEntity = modelEntity;
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

        for(CoasterCart cart : train.getTrain().getCarts()){
            Quaternion currentCartOrientation = cart.getOrientation();
            if(currentCartOrientation == null) throw new RuntimeException("Cart doesn't have orientation");

            Vector3 nonRotatedCartPosition = cart.getPosition();
            nonRotatedCartPosition = Vector3.add(nonRotatedCartPosition, Vector3.subtract(currentLocation, getOrigin()));

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
        Vector3 armorstandCompenstationVector = CoasterCart.getArmorstandHeightCompensationVector();
        Vector3 modelOffsetCompensated = Vector3.subtract(modelOffset, armorstandCompenstationVector);

        Matrix4x4 orientationMatrix = new Matrix4x4();
        orientationMatrix.translate(getCurrentLocation());
        orientationMatrix.translate(armorstandCompenstationVector);
        orientationMatrix.rotate(getCurrentOrientation());
        orientationMatrix.translate(modelOffsetCompensated);

        Quaternion modelOrientation = orientationMatrix.getRotation();

        Vector3 modelLocation = orientationMatrix.toVector3();

        modelEntity.setLocation(modelLocation);
        modelOrientation.rotateYawPitchRoll(modelOffsetRotation);
        modelEntity.setRotation(modelOrientation);
    }

    private void moveTrain(){
        Vector3 armorstandCompenstationVector = CoasterCart.getArmorstandHeightCompensationVector();

        for(CartOffsetFromTransferOrigin cartProgramming : cartPositions){
            Matrix4x4 matrix = new Matrix4x4();
            matrix.translate(getCurrentLocation());

            Quaternion cartOrientation = cartProgramming.orientation();
            Vector3 cartPosition = cartProgramming.position();

            matrix.translate(armorstandCompenstationVector);
            matrix.rotate(getCurrentOrientation());
            matrix.translate(Vector3.subtract(cartPosition, armorstandCompenstationVector));

            Quaternion newCartOrientation = matrix.getRotation().clone();
            newCartOrientation.multiply(cartOrientation);

            cartProgramming.cart().setPosition(matrix.toVector3(), newCartOrientation);
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
    }

    public void resetPosition(){
        setTargetPosition(0, false);
    }

    public Vector3 getOrigin(){
        return origin;
    }

    public boolean isMoving(){
        return moving;
    }

    public boolean canSafelyInteractWith(TrainHandle train){
        if(hasTrain()){
            return getTrain() == train;
        }
        if(isMoving())
            return false;
        if(train == null)
            return true;

        List<Section> currentTrainSections = train.getTrain().getCurrentSections();
        TransferPosition currentTransferPosition = getCurrentTransferPosition();

        Section currentSectionAtStart = currentTransferPosition.getSectionAtStart();
        Section currentSectionAtEnd = currentTransferPosition.getSectionAtEnd();

        if(currentSectionAtStart != null && currentTrainSections.contains(currentSectionAtStart))
            return true;
        else if(currentSectionAtEnd != null && currentTrainSections.contains(currentSectionAtEnd))
            return true;
        else
            return false;
    }

    @Override
    public void unlock(Train authority) {
        if(this.train != null && this.train != authority.getHandle()){
            throw new RuntimeException("Train " + authority.getName() + " tried to unlock transfer while it wasn't the occupier");
        }
        trainExitedTransfer();
        resetPosition();
    }
}

record CartOffsetFromTransferOrigin(Vector3 position, Quaternion orientation, CoasterCart cart) {
}
