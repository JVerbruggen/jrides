package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Section {
    Frame getStartFrame();
    Frame getSpawnFrame();
    Frame getEndFrame();
    boolean isInSection(Frame frame);
    Track getParentTrack();
    void setParentTrack(Track track);
    TrackBehaviour getTrackBehaviour();
    boolean isOccupied();
    Train getOccupiedBy();
    boolean canBlock();
    boolean isBlockSectionSafe();
    void addOccupation(@NonNull  Train train);
    void removeOccupation(@NonNull  Train train);
    boolean canTrainSpawnOn();
    boolean hasPassed(Frame staticFrame, Frame movingFrame);
    boolean passesCycle();

    Section next();
    Section previous();
    void setNext(Section section);
    void setPrevious(Section section);
    boolean spansOver(Train train);
}
