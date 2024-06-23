/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

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
