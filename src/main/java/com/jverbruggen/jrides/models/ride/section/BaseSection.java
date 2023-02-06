package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class BaseSection implements Section{
    private Train occupiedBy;
    private Section previousSection;
    private Section nextSection;
    private Track parentTrack;

    public BaseSection() {
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
    public Section next() {
        return nextSection;
    }

    @Override
    public Section previous() {
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
}
