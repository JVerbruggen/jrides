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

    public SimpleCart(VirtualArmorstand modelArmorstand) {
        this.seats = new ArrayList<>();
        this.modelArmorstand = modelArmorstand;
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
    public void setPosition(Vector3 position) {
        modelArmorstand.setLocation(position);
    }
}
