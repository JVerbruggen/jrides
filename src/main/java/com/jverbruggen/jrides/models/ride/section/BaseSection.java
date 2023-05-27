package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseSection implements Section{
    protected final TrackBehaviour trackBehaviour;
    protected Track parentTrack;

    private Train reservedBy;
    private Train occupiedBy;
    private Section previousSection;
    private Section nextSection;
    private List<Section> additionalPreviousSections;

    public BaseSection(TrackBehaviour trackBehaviour) {
        this.trackBehaviour = trackBehaviour;
        this.reservedBy = null;
        this.occupiedBy = null;
        this.previousSection = null;
        this.nextSection = null;
        this.parentTrack = null;
        this.additionalPreviousSections = new ArrayList<>();
    }

    @Nullable
    @Override
    public Train getReservedBy() {
        return reservedBy;
    }

    @Override
    public boolean canReserveLocally(@Nonnull Train train) {
        return reservedBy == null || reservedBy == train;
    }

    @Override
    public boolean canReserveEntireBlock(@Nonnull Train train) {
        boolean canReserveLocally = canReserveLocally(train);
        if(!canReserveLocally) return false;

        if(canBlock()) return true;
        else return nextSection.canReserveEntireBlock(train);
    }

    @Override
    public void setEntireBlockReservation(@Nonnull Train train) {
        if(getReservedBy() == null){
            setLocalReservation(train);
        }else throw new RuntimeException("Could not make reservation although committed to it");

        if(!canBlock()) nextSection.setEntireBlockReservation(train);
    }

    @Override
    public void clearEntireBlockReservation(@Nonnull Train authority) {
        clearEntireBlockReservation(authority, new ArrayList<>());
    }

    @Override
    public void clearEntireBlockReservation(@Nonnull Train authority, List<Section> done) {
        clearLocalReservation(authority);
        done.add(this);

        if(!canBlock() && !done.contains(nextSection)) nextSection.clearEntireBlockReservation(authority, done);

        clearPreviousSectionReservation(authority, done, previousSection);
        if(!additionalPreviousSections.isEmpty())
            additionalPreviousSections.forEach(s-> clearPreviousSectionReservation(authority, done,s));
    }

    private void clearPreviousSectionReservation(@Nonnull Train authority, List<Section> done, Section previousSection){
        if(previousSection == null) return;

        if(!previousSection.canBlock() && !done.contains(previousSection)){
            previousSection.clearEntireBlockReservation(authority, done);
        }else{
            previousSection.clearLocalReservation(authority);
        }
    }

    @Override
    public void setLocalReservation(@Nonnull Train train) {
        if(reservedBy != null)
            throw new RuntimeException("Cannot reserve an already-reserved section!");

        reservedBy = train;
        reservedBy.addReservedSection(this);
//        Bukkit.broadcastMessage("Set reservation " + getName());
    }

    @Override
    public void clearLocalReservation(@Nonnull Train authority) {
        if(reservedBy == null)
            return;

        if(reservedBy != authority){
//            Bukkit.broadcastMessage("Not authorised to clear reservation!");
            return;
        }

        reservedBy.removeReservedSection(this);
        reservedBy = null;
//        Bukkit.broadcastMessage("Cleared reservation " + getName());
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
    public boolean isBlockSectionSafe(@Nullable Train train) {
        return isBlockSectionSafe(train, true);
    }

    @Override
    public Vector3 getLocationFor(Frame frame) {
        if(trackBehaviour.definesNextSection()){
            return trackBehaviour.getBehaviourDefinedPosition(parentTrack.getLocationFor(frame));
        }

        return parentTrack.getLocationFor(frame);
    }

    @Override
    public Quaternion getOrientationFor(Frame frame) {
        if(trackBehaviour.definesNextSection()){
            return trackBehaviour.getBehaviourDefinedOrientation(parentTrack.getOrientationFor(frame));
        }
        return parentTrack.getOrientationFor(frame);
    }

    @Override
    public void addOccupation(@NonNull Train train) {
        if( occupiedBy != null && occupiedBy != train) throw new RuntimeException("Two trains cannot be in same section! "
                + train.toString() + " trying to enter section with " + occupiedBy.toString());

        if(reservedBy == null || reservedBy != train)
            throw new RuntimeException("Train should reserve section before trying to occupy it!");

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
        return next(train, false);
    }

    @Override
    public Section next(Train train, boolean processPassing) {
        if(trackBehaviour.definesNextSection()){
            return trackBehaviour.getSectionNext(train);
        }

        return nextSection.acceptAsNext(train, processPassing);
    }

    @Override
    public Section previous(Train train) {
        return previous(train, false);
    }

    @Override
    public Section previous(Train train, boolean processPassing) {
        if(trackBehaviour.definesNextSection()){
            return trackBehaviour.getSectionPrevious(train);
        }

        return previousSection;
    }

    @Override
    public Collection<Section> allNextSections(Train train) {
        if(trackBehaviour.definesNextSection())
            return trackBehaviour.getAllNextSections(train);
        return List.of(next(train, false));
    }

    @Override
    public Collection<Section> allPreviousSections(Train train) {
        if(trackBehaviour.definesNextSection())
            return trackBehaviour.getAllPreviousSections(train);
        return List.of(previous(train, false));
    }

    @Override
    public Section acceptAsNext(Train train, boolean processPassing) {
        if(trackBehaviour.definesNextAccepting()){
            return trackBehaviour.acceptAsNext(train, processPassing);
        }

        return this;
    }

    @Override
    public boolean isNextSectionFor(Train train, Section section) {
        return next(train) == section;
    }

    @Override
    public boolean isPreviousSectionFor(Train train, Section section) {
        return previous(train) == section || additionalPreviousSections.contains(section);
    }

    @Override
    public void setNext(Section section) {
        if(nextSection != null) throw new RuntimeException("Cannot set next section twice!");
        nextSection = section;
    }

    @Override
    public void setPrevious(Section section) {
        if(previousSection != null){
//            throw new RuntimeException("Cannot set previous section twice! (Check if multiple sections point to the same singular section)");
            additionalPreviousSections.add(section);
            return;
        }
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
        if(trackBehaviour.definesNextSection()){
            if(trackBehaviour.getSectionAtEnd(forTrain) == section) return true;
            else if(trackBehaviour.getSectionAtStart(forTrain) == section) return false;
            else throw new RuntimeException("Cannot determine behaviour-defined direction to go to section " + section + " for train " + forTrain);
        }else{
            if(this.isNextSectionFor(forTrain, section)) return true;
            else if(this.isPreviousSectionFor(forTrain, section)) return false;
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

    @Override
    public int compareTo(Object o) {
        if(o instanceof Section){
            Section otherSection = (Section) o;
            if(this.getParentTrack().getIdentifier().compareTo(otherSection.getParentTrack().getIdentifier()) == 0)
                return Integer.compare(getStartFrame().getValue(), otherSection.getStartFrame().getValue());
        }
        return 0;
    }

    @Override
    public void setConflictSections(List<Section> sections) {

    }
}
