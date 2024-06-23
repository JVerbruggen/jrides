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
