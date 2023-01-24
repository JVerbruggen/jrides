package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public interface Section {
    Frame getStartFrame();
    Frame getEndFrame();
    boolean isInSection(Frame frame);
    TrackBehaviour getTrackBehaviour();
    boolean isOccupied();
    void setOccupation(Train train);
}
