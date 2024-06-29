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

package com.jverbruggen.jrides.animator.flatride.seat;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.flatride.AbstractFlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.rotor.ModelWithOffset;
import com.jverbruggen.jrides.models.entity.Passenger;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;
import com.jverbruggen.jrides.models.ride.factory.SeatFactory;
import com.jverbruggen.jrides.models.ride.seat.Seat;
import com.jverbruggen.jrides.models.ride.seat.SeatHost;

import java.util.List;

public class SeatComponent extends AbstractFlatRideComponent implements SeatHost {
    private final Seat seat;
    private final Quaternion rotationOffset;
    private final Vehicle parentVehicle;

    public SeatComponent(String identifier, String groupIdentifier, boolean root, List<ModelWithOffset> modelWithOffsets, Seat seat, Quaternion rotationOffset, Vehicle parentVehicle) {
        super(identifier, groupIdentifier, root, modelWithOffsets);
        this.seat = seat;
        this.rotationOffset = rotationOffset;
        this.parentVehicle = parentVehicle;
    }

    @Override
    public void tick() {
        super.tick();

        SeatFactory.moveFlatRideSeat(this.seat, getPositionMatrix(), getRotation());
    }

    @Override
    public void forwardSeatRequest(Seat seat) {
        throw new RuntimeException("Cannot forward seat request on top of a seat component!");
    }

    @Override
    public Seat getForwardingSeatRequest() {
        return null;
    }

    @Override
    public Quaternion getRotation() {
        return Quaternion.multiply(super.getRotation(), rotationOffset);
    }

    @Override
    public List<Seat> getSeats() {
        return List.of(seat);
    }

    @Override
    public List<Passenger> getPassengers() {
        return List.of(seat.getPassenger());
    }

    @Override
    public void ejectPassengers() {
        if(seat.hasPassenger())
            seat.setPassenger(null);
    }

    @Override
    public void despawn() {

    }

    @Override
    public void setRestraint(boolean locked) {
        seat.setRestraint(locked);
    }

    @Override
    public boolean getRestraintState() {
        return seat.restraintsActive();
    }

    @Override
    public PlayerLocation getEjectLocation() {
        return seat.getParentRideHandle().getEjectLocation();
    }

    @Override
    public RideHandle getRideHandle() {
        return seat.getParentRideHandle();
    }

    @Override
    public void onPlayerEnter(Passenger passenger) {
        parentVehicle.onPlayerEnter(passenger);
    }

    @Override
    public void onPlayerExit(Passenger passenger) {
        parentVehicle.onPlayerExit(passenger);
    }
}
