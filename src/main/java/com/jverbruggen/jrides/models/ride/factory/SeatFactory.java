package com.jverbruggen.jrides.models.ride.factory;

import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.math.Vector3PlusYaw;
import com.jverbruggen.jrides.models.ride.seat.Seat;
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

    public List<CoasterSeat> createCoasterSeats(CoasterHandle coasterHandle, List<Vector3PlusYaw> seatOffsets, Vector3 cartLocation, Quaternion orientation){
        List<CoasterSeat> seats = new ArrayList<>();
        Matrix4x4 rotationMatrix = new Matrix4x4();
        rotationMatrix.rotate(orientation);

        for(Vector3PlusYaw seatOffset : seatOffsets){
            Vector3 relativeSeatLocation = calculateSeatLocationOnMatrix(rotationMatrix, seatOffset);
            Vector3 absoluteSeatLocation = Vector3.add(cartLocation, relativeSeatLocation);

            double yawRotation = orientation.getPacketYaw();
            yawRotation += seatOffset.getYaw();

            VirtualEntity seatEntity = viewportManager.spawnSeatEntity(absoluteSeatLocation, yawRotation, null);
            CoasterSeat seat = new CoasterSeat(coasterHandle, seatEntity, seatOffset);
            seatEntity.setHostSeat(seat);
            seats.add(seat);
        }
        return seats;
    }

    private static Vector3 calculateSeatLocationOnMatrix(Matrix4x4 alreadyRotatedMatrix, Vector3PlusYaw seatOffset){
        final Vector3 heightCompensationVector = CoasterSeat.getHeightCompensation();
        Vector3 compensatedSeatOffset = Vector3.add(seatOffset, heightCompensationVector);
        alreadyRotatedMatrix.translate(compensatedSeatOffset);

        Vector3 relativeSeatLocation = alreadyRotatedMatrix.toVector3();
        Vector3 compensatedRelativeSeatLocation = Vector3.subtract(relativeSeatLocation, heightCompensationVector);

        alreadyRotatedMatrix.translate(compensatedSeatOffset.negate());
        return compensatedRelativeSeatLocation;
    }

    public static void moveCoasterSeats(List<CoasterSeat> seats, Vector3 cartLocation, Quaternion orientation){
        Matrix4x4 rotationMatrix = new Matrix4x4();
        rotationMatrix.rotate(orientation);

        for(Seat seat : seats){
            Vector3PlusYaw seatOffset = seat.getOffset();
            Vector3 relativeSeatLocation = calculateSeatLocationOnMatrix(rotationMatrix, seatOffset);
            Vector3 absoluteSeatLocation = Vector3.add(cartLocation, relativeSeatLocation);

            double yaw = seatOffset.getYaw();
            orientation.rotateYawPitchRoll(0, yaw, 0);
            seat.setLocation(absoluteSeatLocation, orientation);
            orientation.rotateYawPitchRoll(0, -yaw, 0);
        }
    }

    public static void moveFlatRideSeat(Seat seat, Matrix4x4 positionMatrix, Quaternion endRotation){
        Vector3 position = calculateSeatLocationOnMatrix(positionMatrix, Vector3PlusYaw.zero());
        seat.setLocation(position, endRotation);
    }
}
