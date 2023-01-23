package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.Seat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleCart implements Cart {
    private List<Seat> seats;
    private VirtualArmorstand modelArmorstand;
    private Vector3 trackOffset;
    private int massMiddleOffset;

    public SimpleCart(VirtualArmorstand modelArmorstand, Vector3 trackOffset, int massMiddleOffset) {
        this.seats = new ArrayList<>();
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
    public void setRotation(Vector3 rotation) {
        modelArmorstand.setHeadpose(rotation);
    }

    @Override
    public void setPosition(Vector3 position) {
        modelArmorstand.setLocation(position);
    }
}
