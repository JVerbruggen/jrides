package com.jverbruggen.jrides.animator.flatride.seat;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.flatride.AbstractFlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.MatrixMath;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.seat.Seat;
import com.jverbruggen.jrides.models.ride.seat.SeatHost;

import java.util.List;

public class SeatComponent extends AbstractFlatRideComponent implements SeatHost {
    private final Seat seat;
    private final Quaternion rotationOffset;

    public SeatComponent(String identifier, List<FlatRideModel> flatRideModels, Seat seat, Quaternion rotationOffset) {
        super(identifier, flatRideModels);
        this.seat = seat;
        this.rotationOffset = rotationOffset;
    }

    @Override
    public void tick() {
        super.tick();

        this.seat.setLocation(getPosition(), getRotation());
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

    }

    @Override
    public void onPlayerExit(Player player) {

    }
}
