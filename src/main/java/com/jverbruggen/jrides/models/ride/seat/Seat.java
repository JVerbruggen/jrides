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

package com.jverbruggen.jrides.models.ride.seat;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.models.entity.Passenger;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.math.Vector3PlusYaw;
import com.jverbruggen.jrides.models.ride.flatride.PlayerControl;

import javax.annotation.Nonnull;

public interface Seat {
    Passenger getPassenger();
    void setPassenger(Player player);
    boolean hasPassenger();
    boolean ejectPassengerSoft(boolean teleport);

    Vector3PlusYaw getOffset();
    void setLocation(Vector3 location, Quaternion orientation);

    VirtualEntity getEntity();
    void setRestraint(boolean locked);
    boolean restraintsActive();

    void setParentSeatHost(SeatHost seatHost);
    SeatHost getParentSeatHost();

    RideHandle getParentRideHandle();

    boolean supportsPlayerControl();
    @Nonnull PlayerControl getPlayerControl();
    void sendPlayerControlInstruction(InstructionType instruction);
}
