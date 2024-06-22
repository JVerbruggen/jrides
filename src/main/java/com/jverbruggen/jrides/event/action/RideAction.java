package com.jverbruggen.jrides.event.action;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;

public interface RideAction {

    void accept(VirtualEntity virtualEntity, RideHandle rideHandle, Player player);
}
