package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovement;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.SimpleFrame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.Cart;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.List;

public class FreeMovementTrackBehaviour implements TrackBehaviour {
    private final CartMovementFactory cartMovementFactory;

    public FreeMovementTrackBehaviour(CartMovementFactory cartMovementFactory) {
        this.cartMovementFactory = cartMovementFactory;
    }

    public TrainMovement move(Frame currentMassMiddleFrame, Speed currentSpeed, Vector3 currentLocation, List<Cart> carts, Track track){
//        Bukkit.broadcastMessage("Free");

        final double dragFactorPerTick = 0.999;
//        final double gravityAccelerationPerTick = 2;
        final double gravityAccelerationPerTick = 0.3;
        Speed newSpeed = currentSpeed.clone();

        int frameCount = track.getRawPositionsCount();
        int frameIncrement = currentSpeed.getFrameIncrement();
        Frame newMassMiddleFrame = new SimpleFrame(Math.floorMod(currentMassMiddleFrame.getValue()+ frameIncrement, frameCount));

        NoLimitsExportPositionRecord position = track.getRawPositions().get(newMassMiddleFrame.getValue());
        Vector3 location = position.toVector3();

        // --- Gravity speed calculations
        double dy = currentLocation.getY() - location.getY(); // negative if going up
        newSpeed.add(dy*gravityAccelerationPerTick);
        newSpeed.multiply(dragFactorPerTick);
        if(newSpeed.getSpeedPerTick() < 1) newSpeed.setSpeedPerTick(1);

        HashMap<Cart, CartMovement> cartMovements = cartMovementFactory.createOnTrackCartMovement(carts, track);

        return new TrainMovement(newSpeed, newMassMiddleFrame, location, cartMovements);
    }

    @Override
    public void trainExitedAtStart() {

    }

    @Override
    public void trainExitedAtEnd() {

    }

}
