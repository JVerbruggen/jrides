package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.Seat;
import org.bukkit.Bukkit;

public class CoasterSeat implements Seat {
    private Player passenger;
    private final VirtualArmorstand virtualArmorstand;
    private final Vector3 offset;
    private boolean restraintLocked;

    private Cart parentCart;

    public CoasterSeat(VirtualArmorstand virtualArmorstand, Vector3 offset) {
        this.passenger = null;
        this.virtualArmorstand = virtualArmorstand;
        this.offset = offset;
        this.restraintLocked = true;
        this.parentCart = null;
    }

    @Override
    public Player getPassenger() {
        return passenger;
    }

    @Override
    public void setPassenger(Player player) {
        if(passenger != null){ // Overtaking seat
            passenger.setSeatedOn(null);
            virtualArmorstand.setPassenger(null);
            passenger.clearSmoothAnimationRotation();
            parentCart.getParentTrain().onPlayerExit(passenger);
        }

        passenger = player;
        virtualArmorstand.setPassenger(player);
        if(player != null){
            player.setSeatedOn(this);
            parentCart.getParentTrain().onPlayerEnter(player);
        }
    }

    @Override
    public boolean hasPassenger() {
        return passenger != null;
    }

    @Override
    public Vector3 getOffset() {
        return offset;
    }

    @Override
    public void setLocation(Vector3 location, Quaternion orientation) {
        virtualArmorstand.setLocation(location, orientation.getEntityYaw());

        if(hasPassenger()) passenger.setSmoothAnimationRotation(orientation);
    }

    @Override
    public VirtualEntity getEntity() {
        return virtualArmorstand;
    }

    @Override
    public void setRestraint(boolean locked) {
        restraintLocked = locked;
    }

    @Override
    public boolean restraintsActive() {
        return restraintLocked;
    }

    @Override
    public void setParentCart(Cart cart) {
        this.parentCart = cart;
    }
}
