package com.jverbruggen.jrides.models.ride.coaster.trackswitch;

import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.reference.SectionReference;

import javax.annotation.Nullable;
import java.util.Map;

public class SwitchPosition {
    private String destinationReference;
    private Section destination;

    public SwitchPosition(String destinationReference) {
        this.destinationReference = destinationReference;
        this.destination = null;
    }

    public String getDestinationReference() {
        return destinationReference;
    }

    public Section getDestination() {
        return destination;
    }

    public void setDestination(Section destination) {
        this.destination = destination;
    }

    public void populateWith(Map<SectionReference, Section> sectionMap){
        Section found = sectionMap.entrySet().stream()
                .filter(entry -> entry.getKey().getSectionIdentifier().equalsIgnoreCase(destinationReference))
                .findFirst().orElseThrow().getValue();
        setDestination(found);
    }

    public boolean availableFor(@Nullable Train train){
        Train occupiedBy = destination.getOccupiedBy();
        Train reservedBy = destination.getReservedBy();
        return (occupiedBy == null || occupiedBy == train) && (reservedBy == null || reservedBy == train);
    }

    @Override
    public String toString() {
        return "<SPos to=" + destination.getName() + ">";
    }
}
