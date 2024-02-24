package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.result.BlockSectionSafetyResult;

import javax.annotation.Nonnull;

public interface SectionSafetyProvider {
    BlockSectionSafetyResult getEnteringSafety(Train train, Section nextSection);
    boolean canHandleOccupation(@Nonnull Train train);
    void handleNewReservation(@Nonnull Train train);
    void handleClearReservation(@Nonnull Train train);
    Train getReservation();
    void handleNewOccupation(@Nonnull Train train);
    void handleClearOccupation(@Nonnull Train train);
    boolean isReservedBy(@Nonnull Train train);
    boolean isOccupied();
    boolean isOccupiedBy(@Nonnull Train train);
}
