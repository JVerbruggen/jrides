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

package com.jverbruggen.jrides.animator.coaster.trackbehaviour.station;

public enum StationPhase{
    /**
     * Waiting for a train to arrive.
     */
    IDLE,

    /**
     * Used when a train is entering the station backwards.
     */
    REVERSING,

    /**
     * Train drives towards the stopping point.
     */
    ARRIVING,

    /**
     * Decelerating action towards stopping point.
     */
    STOPPING,

    /**
     * Stationary. Train is in a safe and still position.
     */
    STATIONARY,

    /**
     * Dispatch action was given, but still waiting for it to be safe.
     */
    WAITING,

    /**
     * Departing from station.
     */
    DEPARTING,

    /**
     * Passing through skips the station behaviour and just lets the train pass with default physics.
     */
    PASSING_THROUGH
}
