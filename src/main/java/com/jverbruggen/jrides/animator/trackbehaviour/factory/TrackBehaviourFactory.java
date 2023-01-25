package com.jverbruggen.jrides.animator.trackbehaviour.factory;

import com.jverbruggen.jrides.animator.trackbehaviour.BlockBrakeTrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.FullStopAndGoTrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.FreeMovementTrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.models.properties.Frame;

public class TrackBehaviourFactory {
    private final CartMovementFactory cartMovementFactory;

    public TrackBehaviourFactory(CartMovementFactory cartMovementFactory) {
        this.cartMovementFactory = cartMovementFactory;
    }

    public TrackBehaviour getBrakeBehaviour(int stopTime){
        return new FullStopAndGoTrackBehaviour(cartMovementFactory, stopTime);
    }

    public TrackBehaviour getTrackBehaviour(){
        return new FreeMovementTrackBehaviour(cartMovementFactory);
    }

    public TrackBehaviour getBlockBrakeBehaviour(Frame blockBrakeEngageFrame){
        return new BlockBrakeTrackBehaviour(cartMovementFactory, blockBrakeEngageFrame);
    }
}
