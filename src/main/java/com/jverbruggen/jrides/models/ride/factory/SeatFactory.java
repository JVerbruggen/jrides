package com.jverbruggen.jrides.models.ride.factory;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterSeat;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;

import java.util.ArrayList;
import java.util.List;

public class SeatFactory {
    private final ViewportManager viewportManager;

    public SeatFactory() {
        this.viewportManager = ServiceProvider.getSingleton(ViewportManager.class);
    }

    public List<Seat> createSeats(RideHandle rideHandle, List<Vector3> seatOffsets, Vector3 cartLocation, Quaternion orientation){
        List<Seat> seats = new ArrayList<>();
        Matrix4x4 rotationMatrix = new Matrix4x4();
        rotationMatrix.rotate(orientation);

        for(Vector3 seatOffset : seatOffsets){
            Vector3 relativeSeatLocation = calculateSeatLocationOnMatrix(rotationMatrix, seatOffset);
            Vector3 absoluteSeatLocation = Vector3.add(cartLocation, relativeSeatLocation);

            double yawRotation = orientation.getPacketYaw();
            VirtualArmorstand seatArmorStand = viewportManager.spawnVirtualArmorstand(absoluteSeatLocation, yawRotation);
            Seat seat = new CoasterSeat(rideHandle, seatArmorStand, seatOffset);
            seatArmorStand.setHostSeat(seat);
            seats.add(seat);
        }
        return seats;
    }

    private static Vector3 calculateSeatLocationOnMatrix(Matrix4x4 alreadyRotatedMatrix, Vector3 seatOffset){
        final Vector3 heightCompensationVector = CoasterSeat.getHeightCompensation();
        Vector3 compensatedSeatOffset = Vector3.add(seatOffset, heightCompensationVector);
        alreadyRotatedMatrix.translate(compensatedSeatOffset);

        Vector3 relativeSeatLocation = alreadyRotatedMatrix.toVector3();
        Vector3 compensatedRelativeSeatLocation = Vector3.subtract(relativeSeatLocation, heightCompensationVector);

        alreadyRotatedMatrix.translate(compensatedSeatOffset.negate());
        return compensatedRelativeSeatLocation;
    }

    public static void moveSeats(List<Seat> seats, Vector3 cartLocation, Quaternion orientation){
        Matrix4x4 rotationMatrix = new Matrix4x4();
        rotationMatrix.rotate(orientation);

        for(Seat seat : seats){
            Vector3 relativeSeatLocation = calculateSeatLocationOnMatrix(rotationMatrix, seat.getOffset());
            Vector3 absoluteSeatLocation = Vector3.add(cartLocation, relativeSeatLocation);

            seat.setLocation(absoluteSeatLocation, orientation);
        }
    }
}
