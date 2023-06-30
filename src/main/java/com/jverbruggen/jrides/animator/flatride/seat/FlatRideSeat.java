package com.jverbruggen.jrides.animator.flatride.seat;

import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.seat.AbstractSeat;
import com.jverbruggen.jrides.models.ride.seat.SeatHost;

public class FlatRideSeat extends AbstractSeat {
    public FlatRideSeat(FlatRideHandle parentRideHandle, SeatHost seatHost, VirtualEntity virtualEntity, Vector3 offset) {
        super(parentRideHandle, seatHost, virtualEntity, offset);
        virtualEntity.setHostSeat(this);
        setRestraint(parentRideHandle.getFirstTriggerContext().getRestraintTrigger().getLock().isUnlocked());
    }
}
