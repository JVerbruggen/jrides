package com.jverbruggen.jrides.models.ride.coaster.track.compound;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;
import java.util.stream.Collectors;

public class LooseEndedSplineBasedTrack implements CompoundTrackPart {
    private final String identifier;

    private List<NoLimitsExportPositionRecord> splinePositions;
    private List<Section> sections;
    private int splinePositionsCount;

    private Track nextTrack;
    private Track previousTrack;

    private Frame startFrame;
    private Frame endFrame;

    public LooseEndedSplineBasedTrack(String identifier, List<NoLimitsExportPositionRecord> splinePositions, List<Section> sections, Frame startFrame, Frame endFrame) {
        this.identifier = identifier;
        this.splinePositions = splinePositions;
        this.sections = sections;
        this.splinePositionsCount = splinePositions.size();
        this.startFrame = startFrame;
        this.endFrame = endFrame;

        this.startFrame.setTrack(this);
        this.endFrame.setTrack(this);

        this.sections.forEach(s-> s.setParentTrack(this));
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public int getLength() {
        return splinePositionsCount;
    }

    @Override
    public List<Section> getSections() {
        return sections;
    }

    @Override
    public Section getNextSpawnSection() {
        for(Section section : sections){
            if(!section.isOccupied() && section.canTrainSpawnOn()) return section;
        }
        return null;
    }

    @Override
    public Vector3 getLocationFor(Frame frame) {
        if(!frame.getTrack().equals(this))
            throwTrackNotSameException(frame);

        if(frame.getValue() >= splinePositions.size())
            JRidesPlugin.getLogger().severe(frame + " out of bounds");

        return splinePositions.get(frame.getValue()).toVector3();
    }

    @Override
    public Quaternion getOrientationFor(Frame frame) {
        if(!frame.getTrack().equals(this))
            throwTrackNotSameException(frame);

        return splinePositions.get(frame.getValue()).getOrientation();
    }

    @Override
    public List<Vector3> getAllPositions() {
        return splinePositions.stream().map(NoLimitsExportPositionRecord::toVector3).collect(Collectors.toList());
    }

    @Override
    public int getLowerFrame() {
        return startFrame.getValue();
    }

    @Override
    public int getUpperFrame() {
        return endFrame.getValue();
    }

    @Override
    public boolean inThisTrack(int frame) {
        return this.startFrame.getValue() <= frame && frame <= this.endFrame.getValue();
    }

    public int countPositions(){
        return splinePositions.size();
    }

    public NoLimitsExportPositionRecord getPosition(int index){
        return splinePositions.get(index);
    }

    @Override
    public String toString() {
        return "<LESBTrack '" + getIdentifier() + "'>";
    }

    @Override
    public Track getNextTrack() {
        return nextTrack;
    }

    @Override
    public Track getPreviousTrack() {
        return previousTrack;
    }

    @Override
    public void setNextTrack(Track track) {
        nextTrack = track;
    }

    @Override
    public void setPreviousTrack(Track track) {
        previousTrack = track;
    }

    @Override
    public Frame getStartFrame() {
        return startFrame;
    }

    @Override
    public Frame getEndFrame() {
        return endFrame;
    }

    private void throwTrackNotSameException(Frame frame){
        throw new RuntimeException("Cannot get frame location if the frame is not on this track.\n" +
                "This: " + this + ", other: " + frame.getTrack() + "\n" +
                "Frame: " + frame);
    }
}
