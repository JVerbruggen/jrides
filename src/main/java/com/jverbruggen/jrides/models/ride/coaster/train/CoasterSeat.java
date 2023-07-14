package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.seat.AbstractSeat;
import com.jverbruggen.jrides.models.ride.seat.SeatHost;

public class CoasterSeat extends AbstractSeat {
    private CoasterCart parentCart;

    public CoasterSeat(RideHandle parentRideHandle, VirtualEntity virtualEntity, Vector3 offset) {
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

    public static Vector3 getHeightCompensation(){
        return new Vector3(0, 1.5, 0);
    }
}
