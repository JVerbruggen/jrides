package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovement;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.Cart;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.List;

public class FullStopAndGoTrackBehaviour implements TrackBehaviour{
    private final CartMovementFactory cartMovementFactory;

    private final int stopTime;
    private Phase phase;
    private int stopTimeCounter;

    public FullStopAndGoTrackBehaviour(CartMovementFactory cartMovementFactory) {
        this.cartMovementFactory = cartMovementFactory;

        this.stopTime = 50;
        trainExitedAtEnd();
    }

    @Override
    public TrainMovement move(int currentMassMiddleFrame, Speed currentSpeed, Vector3 currentLocation, List<Cart> carts, Track track) {
        Bukkit.broadcastMessage("Brake " + phase.toString());
        Speed newSpeed = currentSpeed.clone();

        final double deceleration = 0.5;
        final double acceleration = 0.1;

        switch (phase){
            case STOPPING:
                if(currentSpeed.is(0)) phase = Phase.STOPPED;
                newSpeed.minus(deceleration, 0);
                break;
            case STOPPED:
                if(stopTimeCounter <= 0) phase = Phase.LEAVING;
                stopTimeCounter--;
                break;
            case LEAVING:
                newSpeed.add(acceleration, 1.0);
                break;
        }

        int newMassMiddleFrame = currentMassMiddleFrame + currentSpeed.getFrameIncrement();
        Vector3 newTrainLocation = track.getRawPositions().get(newMassMiddleFrame).toVector3();

        HashMap<Cart, CartMovement> cartMovements = cartMovementFactory.createOnTrackCartMovement(carts, track, newMassMiddleFrame);

        return new TrainMovement(newSpeed, newMassMiddleFrame, newTrainLocation, cartMovements);
    }

    @Override
    public void trainExitedAtStart() {

    }

    @Override
    public void trainExitedAtEnd(){
        this.phase = Phase.STOPPING;
        this.stopTimeCounter = this.stopTime;
    }
}

enum Phase{
    STOPPING,
    STOPPED,
    LEAVING
}