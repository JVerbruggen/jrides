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

package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.math.Vector3PlusYaw;
import com.jverbruggen.jrides.models.ride.seat.AbstractSeat;
import com.jverbruggen.jrides.models.ride.seat.SeatHost;

public class CoasterSeat extends AbstractSeat {
    private CoasterCart parentCart;

    public CoasterSeat(RideHandle parentRideHandle, VirtualEntity virtualEntity, Vector3PlusYaw offset) {
        super(parentRideHandle, null, virtualEntity, offset);
        this.parentCart = null;
    }

    @Override
    public void setParentSeatHost(SeatHost seatHost) {
        super.setParentSeatHost(seatHost);

        if(seatHost instanceof CoasterCart)
            parentCart = (CoasterCart) seatHost;
        else throw new RuntimeException("Cannot set non-coaster-cart as parent for coaster seat");
    }

    @Override
    public SeatHost getParentSeatHost() {
        return parentCart;
    }

    public CoasterCart getParentCart() {
        return this.parentCart;
    }

    public static Vector3 getViewingAngleOffsetFromSeat(){
        return new Vector3(0, 1.5, 0);
    }
}
