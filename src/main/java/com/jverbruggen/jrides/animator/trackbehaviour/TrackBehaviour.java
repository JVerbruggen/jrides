package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public interface TrackBehaviour {
    TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Track track);
    void trainExitedAtStart();
    void trainExitedAtEnd();
    String getName();
    boolean canBlock();
    boolean canSpawnOn();
    Frame getSpawnFrame();
    void setParentTrack(Track parentTrack);
}
