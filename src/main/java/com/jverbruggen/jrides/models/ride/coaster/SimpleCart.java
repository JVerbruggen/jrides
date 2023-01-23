package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.ArmorStandPose;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.models.ride.factory.SeatFactory;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleCart implements Cart {
    private List<Seat> seats;
    private VirtualArmorstand modelArmorstand;
    private Vector3 trackOffset;
    private int massMiddleOffset;

    public SimpleCart(List<Seat> seats, VirtualArmorstand modelArmorstand, Vector3 trackOffset, int massMiddleOffset) {
        this.seats = seats;
        this.modelArmorstand = modelArmorstand;
        this.trackOffset = trackOffset;
        this.massMiddleOffset = massMiddleOffset;
    }

    @Override
    public List<Seat> getSeats() {
        return null;
    }

    @Override
    public List<Player> getPassengers() {
        return seats.stream()
                .filter(Seat::hasPassenger)
                .map(Seat::getPassenger)
                .collect(Collectors.toList());
    }

    @Override
    public int getMassMiddleOffset() {
        return massMiddleOffset;
    }

    @Override
    public Vector3 getTrackOffset() {
        return trackOffset;
    }

    @Override
    public void setPosition(Vector3 position, Quaternion orientation) {
        setPosition(position);

        modelArmorstand.setHeadpose(ArmorStandPose.getArmorStandPose(orientation)); // TODO: expensive

        SeatFactory.moveSeats(seats, position, orientation);
    }

    @Override
    public void setPosition(Vector3 position) {
        modelArmorstand.setLocation(position, 0);
    }
}
