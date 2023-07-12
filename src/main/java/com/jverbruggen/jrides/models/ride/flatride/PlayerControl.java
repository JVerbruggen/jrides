package com.jverbruggen.jrides.models.ride.flatride;

import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.models.ride.seat.InstructionType;

public interface PlayerControl {
    /**
     * This function is called out of the bukkit main thread.
     * To avoid spamming of instruction, it is required to debounce
     * incoming instructions.
     * @param instruction Incoming instruction of player
     */
    void processInstructionAsync(InstructionType instruction);

    /**
     * Called by synchronized bukkit thread
     * to apply control to the component
     */
    void apply();

    /**
     * Reset the player control
     */
    void reset();

    /**
     * Add player that controls the PlayerControl
     * Used for communication
     * @param player player to add
     */
    void addControlling(JRidesPlayer player);

    /**
     * Remove player that controls the PlayerControl
     * Used for communication
     * @param player player to remove
     */
    void removeControlling(JRidesPlayer player);

    /**
     * Send notification that the control phase has started
     */
    void sendStartNotification();

    /**
     * Send notification that the control phase has stopped
     */
    void sendStopNotification();
}
