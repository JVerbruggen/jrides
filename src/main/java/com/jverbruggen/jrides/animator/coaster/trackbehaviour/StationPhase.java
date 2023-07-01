package com.jverbruggen.jrides.animator.coaster.trackbehaviour;

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
