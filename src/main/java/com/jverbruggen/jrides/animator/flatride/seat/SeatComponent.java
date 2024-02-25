package com.jverbruggen.jrides.animator.flatride.seat;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.flatride.AbstractFlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.models.entity.Player;
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

    public SeatComponent(String identifier, String groupIdentifier, boolean root, List<FlatRideModel> flatRideModels, Seat seat, Quaternion rotationOffset, Vehicle parentVehicle) {
        super(identifier, groupIdentifier, root, flatRideModels);
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
    public Quaternion getRotation() {
        return Quaternion.multiply(super.getRotation(), rotationOffset);
    }

    @Override
    public List<Seat> getSeats() {
        return List.of(seat);
    }

    @Override
    public List<Player> getPassengers() {
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
    public void onPlayerEnter(Player player) {
        parentVehicle.onPlayerEnter(player);
    }

    @Override
    public void onPlayerExit(Player player) {
        parentVehicle.onPlayerExit(player);
    }
}
