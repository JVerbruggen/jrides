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

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.models.entity.MessageReceiver;
import com.jverbruggen.jrides.models.entity.Passenger;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.TrackEnd;
import com.jverbruggen.jrides.models.properties.TrainEnd;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.ArrayList;
import java.util.List;

public class SimpleTrain extends AbstractVehicle implements Train {
    private final List<CoasterCart> carts;
    private final Frame middleOfTrainFrame;
    private final Frame headOfTrainFrame;
    private final Frame tailOfTrainFrame;
    private final List<Section> currentSections;
    private final List<Section> reservedSections;
    private Vector3 headLocation;
    private Vector3 middleLocation;
    private Vector3 tailLocation;

    private CoasterStationHandle onStation;
    private TrainHandle trainHandle;

    private String statusMessage;
    private final List<Player> statusMessageListeners;
    private final List<MessageReceiver> positionMessageListeners;
    private boolean drivingTowardsPositiveDirection;
    private boolean forwards;

    public SimpleTrain(String name, List<CoasterCart> carts, Frame headOfTrainFrame, Frame middleOfTrainFrame, Frame tailOfTrainFrame,
                       Vector3 headLocation, Vector3 middleLocation, Vector3 tailLocation, Section section, boolean debugMode) {
        super(name, debugMode);
        this.carts = carts;
        this.headOfTrainFrame = headOfTrainFrame;
        this.middleOfTrainFrame = middleOfTrainFrame;
        this.tailOfTrainFrame = tailOfTrainFrame;

        this.currentSections = new ArrayList<>();
        this.currentSections.add(section);
        this.headLocation = headLocation;
        this.middleLocation = middleLocation;
        this.tailLocation = tailLocation;

        this.reservedSections = new ArrayList<>();

        this.onStation = null;
        this.trainHandle = null;
        this.statusMessage = "";
        this.statusMessageListeners = new ArrayList<>();
        this.positionMessageListeners = new ArrayList<>();

        this.drivingTowardsPositiveDirection = true;
        this.forwards = true;

        getCarts().forEach(c -> c.setParentTrain(this));
    }

    @Override
    public List<CoasterCart> getCarts() {
        return carts;
    }

    @Override
    public int size() {
        return carts.size();
    }

    @Override
    public Frame getFrontFacingTrainFrame() {
        return forwards ? getHeadOfTrainFrame() : getTailOfTrainFrame();
    }

    @Override
    public Frame getBackFacingTrainFrame() {
        return forwards ? getTailOfTrainFrame() : getHeadOfTrainFrame();
    }

    @Override
    public Frame getMiddleOfTrainFrame() {
        return middleOfTrainFrame;
    }

    @Override
    public Frame getHeadOfTrainFrame() {
        return headOfTrainFrame;
    }

    @Override
    public Frame getTailOfTrainFrame() {
        return tailOfTrainFrame;
    }

    @Override
    public Vector3 getCurrentHeadLocation() {
        return headLocation;
    }

    @Override
    public Vector3 getCurrentLocation() {
        return middleLocation;
    }

    @Override
    public Vector3 getCurrentTailLocation() {
        return tailLocation;
    }

    @Override
    public void setCurrentLocation(Vector3 headLocation, Vector3 middleLocation, Vector3 tailLocation) {
        this.headLocation = headLocation;
        this.middleLocation = middleLocation;
        this.tailLocation = tailLocation;
    }

    @Override
    public Vector3 getMassMiddlePoint() {
        return Train.calculateMassMiddlePoint(headLocation, middleLocation, tailLocation);
    }

    @Override
    public List<Section> getReservedSections() {
        return reservedSections;
    }

    @Override
    public void addReservedSection(Section section) {
        reservedSections.add(section);
    }

    @Override
    public void removeReservedSection(Section section) {
        reservedSections.remove(section);
    }

    @Override
    public List<Section> getCurrentSections() {
        return currentSections;
    }

    @Override
    public Section getNextSection() {
        return getHeadSection().next(this, false);
    }

    @Override
    public Section getHeadSection() {
        if(currentSections.size() == 0) throw new RuntimeException("Train occupies 0 sections");
        return currentSections.get(0);
    }

    @Override
    public Section getTailSection() {
        if(currentSections.size() == 0) throw new RuntimeException("Train occupies 0 sections");
        return currentSections.get(currentSections.size()-1);
    }

    @Override
    public void addCurrentSection(Section section) {
        addCurrentSection(section, TrainEnd.HEAD);
    }

    @Override
    public void addCurrentSection(Section section, TrainEnd trainEnd) {
        if(currentSections.contains(section)) return;

        if(trainEnd.equals(TrainEnd.HEAD)){
            currentSections.add(0, section);
        }
        else
            currentSections.add(section);
    }

    @Override
    public void removeCurrentSection(Section section) {
        if(!this.currentSections.contains(section)) throw new RuntimeException("Section removal mismatch");
        this.currentSections.remove(section);
    }

    @Override
    public void setRestraintForAll(boolean locked) {
        for(CoasterCart cart : getCarts()){
            cart.setRestraint(locked);
        }

        if(locked) playRestraintCloseSound();
        else playRestraintOpenSound();
    }

    @Override
    public boolean getRestraintState() {
        return carts.stream().allMatch(CoasterCart::getRestraintState);
    }

    @Override
    public boolean equals(Train other) {
        return this.getName().equalsIgnoreCase(other.getName());
    }

    @Override
    public TrackEnd getDirection() {
        return getHandle().getSpeed().isPositive() ? TrackEnd.END : TrackEnd.START;
    }

    @Override
    public boolean isPositiveDrivingDirection() {
        return drivingTowardsPositiveDirection;
    }

    @Override
    public boolean drivingTowardsEnd() {
        return getDirection() == TrackEnd.END;
    }

    @Override
    public boolean isFacingForwards() {
        return forwards;
    }

    @Override
    public void setFacingForwards(boolean forwards) {
        this.forwards = forwards;
    }

    @Override
    public void setDrivingDirection(boolean positive) {
        drivingTowardsPositiveDirection = positive;
        getHandle().getSpeed().setInverted(!forwards); // TODO: untested
    }

    @Override
    public void setInvertedFrameAddition(boolean inverted) {
        headOfTrainFrame.setInvertedFrameAddition(inverted);
        middleOfTrainFrame.setInvertedFrameAddition(inverted);
        tailOfTrainFrame.setInvertedFrameAddition(inverted);

        for(CoasterCart cart : getCarts()){
            cart.setInvertedFrameAddition(inverted);
        }
    }

    @Override
    public void onPlayerEnter(Passenger passenger) {
        addPassenger(passenger);
        Player player = passenger.getPlayer();
        if(statusModeEnabled(player)){
            addStatusMessageListener(player);
        }

        if(isStationary()){
            onStation.onPlayerEnter(player);
        }
    }

    @Override
    public void onPlayerExit(Passenger passenger) {
        removePassenger(passenger);
        removeStatusMessageListener(passenger.getPlayer());
    }


    @Override
    public void setStationaryAt(CoasterStationHandle stationaryAt) {
        this.onStation = stationaryAt;
    }

    @Override
    public boolean isStationary() {
        return onStation != null;
    }

    @Override
    public CoasterStationHandle getStationaryAt() {
        return onStation;
    }

    @Override
    public void setHandle(TrainHandle trainHandle) {
        this.trainHandle = trainHandle;
    }

    @Override
    public TrainHandle getHandle() {
        return trainHandle;
    }

    @Override
    public void ejectPassengers() {
        carts.forEach(CoasterCart::ejectPassengers);
    }

    @Override
    public void playRestraintOpenSound() {
        playSound(trainHandle.getCoasterHandle().getSounds().getRestraintOpen());
    }

    @Override
    public void playRestraintCloseSound() {
        playSound(trainHandle.getCoasterHandle().getSounds().getRestraintClose());
    }

    @Override
    public void playDispatchSound() {
        playSound(trainHandle.getCoasterHandle().getSounds().getDispatch());
    }

    @Override
    public void sendPositionMessage(String positionMessage) {
        if(positionMessageListeners == null || positionMessageListeners.size() == 0) return;
        positionMessageListeners.forEach(r->r.sendMessage(positionMessage));
    }

    @Override
    public void addPositionMessageListener(MessageReceiver messageReceiver) {
        positionMessageListeners.add(messageReceiver);
    }

    @Override
    public void removePositionMessageListener(MessageReceiver messageReceiver) {
        positionMessageListeners.remove(messageReceiver);
    }

    @Override
    public boolean positionMessageEnabled(MessageReceiver messageReceiver) {
        return positionMessageListeners.contains(messageReceiver);
    }

    @Override
    public void setStatusMessage(String statusMessage) {
        if(!isDebugMode()) return;
        this.statusMessage = statusMessage;
        if(!statusMessage.equals(""))
            statusMessageListeners.forEach(l -> l.sendMessage(statusMessage));
    }

    @Override
    public void addStatusMessageListener(Player player) {
        if(!isDebugMode()) return;
        statusMessageListeners.add(player);
        player.sendMessage(statusMessage);
    }

    @Override
    public void removeStatusMessageListener(Player player) {
        if(!isDebugMode()) return;
        statusMessageListeners.remove(player);
    }

    @Override
    public boolean statusModeEnabled(Player player) {
        return isDebugMode() && player.getBukkitPlayer().hasPermission(Permissions.ELEVATED_STATUS_INSPECTION);
    }

    @Override
    public void despawn() {
        carts.forEach(CoasterCart::despawn);
    }

    @Override
    public void reset() {
        getHandle().resetEffects();
    }

    @Override
    public String toString() {
        String forwards_indicator = forwards ? "F" : "B";
        return "<Train " + getName() + "[" + forwards_indicator + "] at position " + getHeadOfTrainFrame() + ">";
    }
}
