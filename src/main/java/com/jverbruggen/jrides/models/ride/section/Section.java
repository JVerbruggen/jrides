package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public interface Section {
    int getStartFrame();
    int getEndFrame();
    boolean isInSection(int frame);
    TrackBehaviour getTrackBehaviour();
    boolean isOccupied();
    void setOccupation(Train train);
}
