package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.SimpleFrame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class FreeMovementTrackBehaviour extends BaseTrackBehaviour implements TrackBehaviour {
    public FreeMovementTrackBehaviour(CartMovementFactory cartMovementFactory) {
        super(cartMovementFactory);
    }

    public TrainMovement move(Speed currentSpeed, Vector3 currentMassMiddleLocation, Train train, Track track) {
//        Bukkit.broadcastMessage("Free");

        final double dragFactorPerTick = 0.9992;
//        final double gravityAccelerationPerTick = 2;
        final double gravityAccelerationPerTick = 0.2;
        Speed newSpeed = currentSpeed.clone();

        int frameCount = track.getRawPositionsCount();
        int frameIncrement = currentSpeed.getFrameIncrement();
        Frame newMassMiddleFrame = new SimpleFrame(Math.floorMod(train.getMassMiddleFrame().getValue()+ frameIncrement, frameCount));

        NoLimitsExportPositionRecord position = track.getRawPositions().get(newMassMiddleFrame.getValue());
        Vector3 massMiddleLocation = position.toVector3();

        // --- Gravity speed calculations
        double dy = currentMassMiddleLocation.getY() - massMiddleLocation.getY(); // negative if going up
        newSpeed.add(dy*gravityAccelerationPerTick);
        newSpeed.multiply(dragFactorPerTick);
        if(newSpeed.getSpeedPerTick() < 1) newSpeed.setSpeedPerTick(1);

        return calculateTrainMovement(train, track, newSpeed);
//
//        HashMap<Cart, CartMovement> cartMovements = cartMovementFactory.createOnTrackCartMovement(train.getCarts(), track);
//
//        return new TrainMovement(newSpeed, newHeadOfTrainFrame, location, cartMovements);
    }

    @Override
    public void trainExitedAtStart() {

    }

    @Override
    public void trainExitedAtEnd() {

    }

    @Override
    public String getName() {
        return "FreeMovement";
    }

    @Override
    public boolean canBlock() {
        return false;
    }

}
