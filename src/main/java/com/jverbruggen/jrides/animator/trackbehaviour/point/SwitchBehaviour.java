package com.jverbruggen.jrides.animator.trackbehaviour.point;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.BaseTrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.trackswitch.SwitchPosition;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.reference.SectionReference;

import java.util.List;
import java.util.Map;

public class SwitchBehaviour extends BaseTrackBehaviour {
    private List<SwitchPosition> destinations;
    private SwitchPosition origin;

    private Section selectedDestination;

    public SwitchBehaviour(CartMovementFactory cartMovementFactory, List<SwitchPosition> destinations, SwitchPosition origin) {
        super(cartMovementFactory);
        this.destinations = destinations;
        this.origin = origin;
    }

    private void selectNewDestination(){
        if(destinations.size() == 0)
            throw new RuntimeException("Switch does not lead anywhere!");

        selectedDestination = destinations.get(0).getDestination();
    }

    @Override
    protected void setParentTrackOnFrames(Track parentTrack) {

    }

    @Override
    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Section section) {
        return selectedDestination.getTrackBehaviour().move(currentSpeed, trainHandle, section);
    }

    @Override
    public void trainExitedAtStart() {
        this.selectNewDestination();
    }

    @Override
    public void trainExitedAtEnd() {
        this.selectNewDestination();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean canBlock() {
        return false;
    }

    @Override
    public boolean canSpawnOn() {
        return false;
    }

    @Override
    public Frame getSpawnFrame() {
        return null;
    }

    @Override
    public boolean definesNextSection() {
        return true;
    }

    @Override
    public Section getSectionAtStart(Train train) {
        return origin.getDestination();
    }

    @Override
    public Section getSectionAtEnd(Train train) {
        return selectedDestination;
    }

    @Override
    public void populateSectionReferences(Map<SectionReference, Section> sectionMap) {
        for(SwitchPosition destination : destinations){
            destination.populateWith(sectionMap);
        }

        origin.populateWith(sectionMap);

        this.selectNewDestination();
    }
}
