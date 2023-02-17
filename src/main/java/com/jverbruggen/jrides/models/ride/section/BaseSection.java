package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class BaseSection implements Section{
    protected final TrackBehaviour trackBehaviour;
    protected Track parentTrack;

    private Train occupiedBy;
    private Section previousSection;
    private Section nextSection;

    public BaseSection(TrackBehaviour trackBehaviour) {
        this.trackBehaviour = trackBehaviour;
        this.occupiedBy = null;
        this.previousSection = null;
        this.nextSection = null;
        this.parentTrack = null;
    }

    @Override
    public Train getOccupiedBy() {
        return occupiedBy;
    }

    @Override
    public boolean isOccupied() {
        return occupiedBy != null;
    }

    @Override
    public Vector3 getLocationFor(Frame frame) {
        if(trackBehaviour.canMoveFromParentTrack()){
            return trackBehaviour.getBehaviourDefinedPosition(parentTrack.getLocationFor(frame));
        }

        return parentTrack.getLocationFor(frame);
    }

    @Override
    public Quaternion getOrientationFor(Frame frame) {
        if(trackBehaviour.canMoveFromParentTrack()){
            return trackBehaviour.getBehaviourDefinedOrientation(parentTrack.getOrientationFor(frame));
        }
        return parentTrack.getOrientationFor(frame);
    }

    @Override
    public void addOccupation(@NonNull Train train) {
        if( occupiedBy != null && occupiedBy != train) throw new RuntimeException("Two trains cannot be in same section! "
                + train.toString() + " trying to enter section with " + occupiedBy.toString());
        occupiedBy = train;

        JRidesPlugin.getLogger().info(LogType.SECTIONS,"Section " + this.toString() + " occupied by " + train);
    }

    @Override
    public void removeOccupation(@NonNull Train train) {
        if(occupiedBy != train){
            String occupationString = occupiedBy != null ? occupiedBy.toString() : "null";
            throw new RuntimeException("Trying to remove train " + train + " from section " + this + " while occupation is different: " + occupationString);
        }

        occupiedBy = null;
        JRidesPlugin.getLogger().info(LogType.SECTIONS, "Section " + this.toString() + " has been exited");
    }

    @Override
    public Section next(Train train) {
        if(trackBehaviour.canMoveFromParentTrack()){
            return trackBehaviour.getSectionNext(train);
        }

        return nextSection;
    }

    @Override
    public Section previous(Train train) {
        if(trackBehaviour.canMoveFromParentTrack()){
            return trackBehaviour.getSectionPrevious(train);
        }

        return previousSection;
    }


    @Override
    public void setNext(Section section) {
        if(nextSection != null) throw new RuntimeException("Cannot set next section twice!");
        nextSection = section;
    }

    @Override
    public void setPrevious(Section section) {
        if(previousSection != null) throw new RuntimeException("Cannot set previous section twice! (Check if multiple sections point to the same singular section)");
        previousSection = section;
    }

    @Override
    public Track getParentTrack() {
        return parentTrack;
    }

    @Override
    public void setParentTrack(Track track) {
        if(parentTrack != null)
            throw new RuntimeException("Section already has parent track");
        parentTrack = track;
        getTrackBehaviour().setParentTrack(track);
    }

    @Override
    public String getName() {
        return toString();
    }

    @Override
    public boolean positiveDirectionToGoTo(Section section, Train forTrain) {
        if(trackBehaviour.canMoveFromParentTrack()){
            if(trackBehaviour.getSectionAtEnd(forTrain) == section) return true;
            else if(trackBehaviour.getSectionAtStart(forTrain) == section) return false;
            else throw new RuntimeException("Cannot determine behaviour-defined direction to go to section " + section + " for train " + forTrain);
        }else{
            if(this.next(forTrain) == section) return true;
            else if(this.previous(forTrain) == section) return false;
            else throw new RuntimeException("Cannot determine direction to go to section " + section + " for train " + forTrain);
        }
    }

    @Override
    public boolean shouldJumpAtEnd() {
        return false;
    }

    @Override
    public boolean shouldJumpAtStart() {
        return false;
    }
}
