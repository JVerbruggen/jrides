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

package com.jverbruggen.jrides.animator.flatride.seat;

import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.models.entity.Passenger;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.math.Vector3PlusYaw;
import com.jverbruggen.jrides.models.ride.flatride.PlayerControl;
import com.jverbruggen.jrides.models.ride.seat.AbstractSeat;
import com.jverbruggen.jrides.models.ride.seat.InstructionType;
import com.jverbruggen.jrides.models.ride.seat.SeatHost;

public class FlatRideSeat extends AbstractSeat {
    private PlayerControl playerControl;

    public FlatRideSeat(FlatRideHandle parentRideHandle, SeatHost seatHost, VirtualEntity virtualEntity, Vector3PlusYaw offset) {
        super(parentRideHandle, seatHost, virtualEntity, offset);
        this.playerControl = null;

        virtualEntity.setHostSeat(this);
        setRestraint(parentRideHandle.getFirstTriggerContext().getRestraintTrigger().getLock().isUnlocked());
    }

    public void setPlayerControl(PlayerControl playerControl) {
        this.playerControl = playerControl;
    }

    @Override
    public boolean supportsPlayerControl() {
        return playerControl != null;
    }

    @Override
    public void sendPlayerControlInstruction(InstructionType instruction) {
        if(playerControl == null) return;

        this.playerControl.processInstructionAsync(instruction);
    }

    @Override
    protected void onPassengerEnter(Passenger passenger) {
        super.onPassengerEnter(passenger);

        if(playerControl != null)
            playerControl.addControlling(passenger.getPlayer());
    }

    @Override
    protected void onPassengerExit(Passenger passenger) {
        super.onPassengerExit(passenger);

        if(playerControl != null)
            playerControl.removeControlling(passenger.getPlayer());
    }
}
