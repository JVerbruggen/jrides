package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.train.Cart;

public interface Seat {
    Player getPassenger();
    void setPassenger(Player player);
    boolean hasPassenger();
    boolean ejectPassengerSoft(boolean teleport);

    Vector3 getOffset();
    void setLocation(Vector3 location, Quaternion orientation);

    VirtualEntity getEntity();
    void setRestraint(boolean locked);
    boolean restraintsActive();

    void setParentCart(Cart cart);
    Cart getParentCart();
}
