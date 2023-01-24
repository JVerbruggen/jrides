package com.jverbruggen.jrides.animator.trackbehaviour.factory;

import com.jverbruggen.jrides.animator.trackbehaviour.FullStopAndGoTrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.FreeMovementTrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;

public class TrackBehaviourFactory {
    private final CartMovementFactory cartMovementFactory;

    public TrackBehaviourFactory(CartMovementFactory cartMovementFactory) {
        this.cartMovementFactory = cartMovementFactory;
    }

    public TrackBehaviour getBrakeBehaviour(){
        return new FullStopAndGoTrackBehaviour(cartMovementFactory);
    }

    public TrackBehaviour getTrackBehaviour(){
        return new FreeMovementTrackBehaviour(cartMovementFactory);
    }
}
