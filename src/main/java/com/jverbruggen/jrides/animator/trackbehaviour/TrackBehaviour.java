package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;

public interface TrackBehaviour {
    TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Section section);
    void trainExitedAtStart();
    void trainExitedAtEnd();

    String getName();
    boolean canBlock();
    boolean canSpawnOn();
    Frame getSpawnFrame();

    void setParentTrack(Track parentTrack);
    Track getParentTrack();

    Section getSectionAtStart();
    Section getSectionAtEnd();

    boolean canMoveFromParentTrack();
    Vector3 getBehaviourDefinedPosition();

}
