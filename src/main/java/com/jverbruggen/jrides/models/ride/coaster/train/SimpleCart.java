package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovement;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.ArmorStandPose;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.LinkedFrame;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.models.ride.factory.SeatFactory;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleCart implements Cart {
    private List<Seat> seats;
    private VirtualArmorstand modelArmorstand;
    private Vector3 trackOffset;
    private LinkedFrame linkedFrame;
    private Train parentTrain;

    private Quaternion currentOrientation;

    public SimpleCart(List<Seat> seats, VirtualArmorstand modelArmorstand, Vector3 trackOffset, LinkedFrame linkedFrame) {
        this.seats = seats;
        this.modelArmorstand = modelArmorstand;
        this.trackOffset = trackOffset;
        this.linkedFrame = linkedFrame;
        this.parentTrain = null;
        this.currentOrientation = null;

        seats.forEach(s -> s.setParentCart(this));
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
    public LinkedFrame getFrame() {
        return linkedFrame;
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

    @Override
    public void setPosition(Vector3 position, Quaternion orientation) {
        currentOrientation = orientation;
        setPosition(position);
        modelArmorstand.setHeadpose(ArmorStandPose.getArmorStandPose(orientation));
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
                    passenger.teleport(ejectLocation);
            }
        });
    }
}
