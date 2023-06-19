package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovement;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.ArmorStandPose;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.models.ride.factory.SeatFactory;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleCart implements Cart {
    private final String name;
    private List<Seat> seats;
    private VirtualArmorstand modelArmorstand;
    private Vector3 trackOffset;
    private Frame frame;
    private Train parentTrain;

    private boolean hasEffects;
    private TrainEffectTriggerHandle nextEffect;

    private Quaternion currentOrientation;
    private Vector3 orientationOffset;

    public SimpleCart(String name, List<Seat> seats, VirtualArmorstand modelArmorstand, Vector3 trackOffset, Frame frame) {
        this.name = name;
        this.seats = seats;
        this.modelArmorstand = modelArmorstand;
        this.trackOffset = trackOffset;
        this.frame = frame;
        this.parentTrain = null;
        this.currentOrientation = new Quaternion(0,0,0,0);

        this.nextEffect = null;
        this.hasEffects = false;

        seats.forEach(s -> s.setParentCart(this));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Seat> getSeats() {
        return seats;
    }

    @Override
    public List<Player> getPassengers() {
        return seats.stream()
                .filter(Seat::hasPassenger)
                .map(Seat::getPassenger)
                .collect(Collectors.toList());
    }

    @Override
    public Frame getFrame() {
        return frame;
    }

    @Override
    public Vector3 getTrackOffset() {
        return trackOffset;
    }

    @Override
    public Vector3 getPosition() {
        return modelArmorstand.getLocation();
    }

    @Override
    public Quaternion getOrientation() {
        return currentOrientation;
    }

    private void updateOrientationOffset(Vector3 orientationOffset){
        this.orientationOffset = orientationOffset;
        updateOrientation(calculateOrientationWithOffset(currentOrientation));
    }

    private Quaternion calculateOrientationWithOffset(Quaternion original){
        Quaternion orientationWithOffset = original.clone();
        if(this.orientationOffset != null)
            orientationWithOffset.rotateYawPitchRoll(this.orientationOffset);

        return orientationWithOffset;
    }

    private void updateOrientation(Quaternion orientation){
        currentOrientation = orientation;
        modelArmorstand.setHeadpose(ArmorStandPose.getArmorStandPose(currentOrientation));
    }

    @Override
    public void setPosition(Vector3 position, Quaternion orientation) {
        updateOrientation(calculateOrientationWithOffset(orientation));
        setPosition(position);
    }

    @Override
    public void setPosition(Vector3 position) {
        modelArmorstand.setLocation(position, null);
        SeatFactory.moveSeats(seats, position, currentOrientation);
    }

    @Override
    public void setPosition(CartMovement cartMovement) {
        setPosition(cartMovement.getLocation(), cartMovement.getOrientation());
    }

    @Override
    public void setRestraint(boolean locked) {
        for(Seat seat : getSeats()){
            seat.setRestraint(locked);
        }
    }

    @Override
    public boolean getRestraintState() {
        return getSeats().stream().allMatch(Seat::restraintsActive);
    }

    @Override
    public void setParentTrain(Train train) {
        parentTrain = train;
    }

    @Override
    public Train getParentTrain() {
        return parentTrain;
    }

    @Override
    public void ejectPassengers() {
        seats.forEach(s -> {
            Player passenger = s.getPassenger();
            if(passenger != null){
                s.setPassenger(null);
                PlayerLocation ejectLocation = (parentTrain.isStationary())
                        ? parentTrain.getStationaryAt().getEjectLocation()
                        : getParentTrain().getHandle().getCoasterHandle().getEjectLocation();
                if(ejectLocation != null)
                    passenger.teleport(ejectLocation, true);
            }
        });
    }

    @Override
    public boolean shouldFaceForwards() {
        return !frame.isInvertedFrameAddition();
    }

    @Override
    public void setInvertedFrameAddition(boolean inverted) {
        frame.setInvertedFrameAddition(inverted);
    }


    @Override
    public void setNextEffect(TrainEffectTriggerHandle nextEffect) {
        this.nextEffect = nextEffect;
        this.hasEffects = true;
    }

    @Override
    public void playEffects() {

    }

    @Override
    public void despawn() {
        modelArmorstand.despawn();
    }
}
