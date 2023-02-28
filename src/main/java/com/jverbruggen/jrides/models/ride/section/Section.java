package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nullable;

public interface Section {
    Frame getSpawnFrame();
    Frame getStartFrame();
    Frame getEndFrame();
    boolean isInSection(Frame frame);
    Track getParentTrack();
    void setParentTrack(Track track);
    TrackBehaviour getTrackBehaviour();
    boolean isOccupied();
    Train getOccupiedBy();
    boolean canBlock();
    boolean isBlockSectionSafe(Train train);
    void addOccupation(@NonNull  Train train);
    void removeOccupation(@NonNull  Train train);
    boolean canTrainSpawnOn();
    boolean hasPassed(Frame staticFrame, Frame movingFrame);
    boolean passesCycle();
    boolean shouldJumpAtStart();
    boolean shouldJumpAtEnd();

    Vector3 getLocationFor(Frame frame);
    Quaternion getOrientationFor(Frame frame);

    @Nullable
    Section next(Train train);
    @Nullable
    Section previous(Train train);
    boolean isNextSectionFor(Train train, Section section);
    boolean isPreviousSectionFor(Train train, Section section);
    void setNext(Section section);
    void setPrevious(Section section);
    boolean spansOver(Train train);
    boolean positiveDirectionToGoTo(Section section, Train forTrain);

    String getName();
}
