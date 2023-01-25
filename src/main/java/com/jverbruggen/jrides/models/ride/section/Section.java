package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.Train;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Section {
    Frame getStartFrame();
    Frame getEndFrame();
    boolean isInSection(Frame frame);
    TrackBehaviour getTrackBehaviour();
    boolean isOccupied();
    boolean canBlock();
    boolean isBlockSectionSafe();
    void addOccupation(@NonNull  Train train);
    void removeOccupation(@NonNull  Train train);
    boolean canTrainSpawnOn();
    boolean hasPassed(Frame staticFrame, Frame movingFrame);

    Section next();
    Section previous();
    void setNext(Section section);
    void setPrevious(Section section);
}
