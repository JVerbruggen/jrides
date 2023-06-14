package com.jverbruggen.jrides.animator.trackbehaviour.point;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.BaseTrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.trackswitch.SwitchPosition;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.reference.SectionReference;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SwitchBehaviour extends BaseTrackBehaviour {
    private List<SwitchPosition> destinations;
    private SwitchPosition origin;

    private Section selectedDestination;

    private int roundRobinState;

    public SwitchBehaviour(CartMovementFactory cartMovementFactory, List<SwitchPosition> destinations, SwitchPosition origin) {
        super(cartMovementFactory);
        this.destinations = destinations;
        this.origin = origin;
        this.roundRobinState = 0;
    }

    private void updateRoundRobinState(){
        if(++this.roundRobinState >= destinations.size())
            this.roundRobinState = 0;
    }

    private void selectNewDestination(){
        if(destinations.size() == 0)
            throw new RuntimeException("Switch does not lead anywhere!");

        selectedDestination = destinations.get(this.roundRobinState).getDestination();
        JRidesPlugin.getLogger().info(LogType.SECTIONS, "Next destination for switch: " + selectedDestination);
        updateRoundRobinState();
    }

    @Override
    protected void setParentTrackOnFrames(Track parentTrack) {

    }

    @Override
    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Section section) {
        throw new RuntimeException("Should not move in point");
    }

    @Override
    public boolean definesNextAccepting() {
        return true;
    }

    @Override
    public Section acceptAsNext(Train train, boolean canProcessPassed) {
        if(canProcessPassed) trainPassed(train);

        return getSectionAtEnd(train);
    }

    @Override
    public void trainExitedAtStart() {
    }

    @Override
    public void trainExitedAtEnd() {
    }

    @Override
    public void trainPassed(Train train) {
        selectNewDestination();
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
        if(train != null){
            List<Section> matchingInReservation = train.getReservedSections().stream()
                    .filter(s -> destinations.stream()
                            .anyMatch(d -> d.getDestination().equals(s)))
                    .collect(Collectors.toList());

            if(matchingInReservation.size() > 0){
                Bukkit.broadcastMessage("Matches in reservation: " + matchingInReservation.get(0));
                return matchingInReservation.get(0);
            }
        }

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
