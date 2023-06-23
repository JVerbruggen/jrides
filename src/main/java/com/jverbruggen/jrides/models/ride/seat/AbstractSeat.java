package com.jverbruggen.jrides.models.ride.seat;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.event.player.PlayerStandUpEvent;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.state.ride.SoftEjector;

public abstract class AbstractSeat implements Seat {
    private final RideHandle parentRideHandle;
    private Player passenger;
    private final VirtualArmorstand virtualArmorstand;
    private final Vector3 offset;
    private boolean restraintLocked;
    private SeatHost seatHost;

    public AbstractSeat(RideHandle parentRideHandle, SeatHost seatHost, VirtualArmorstand virtualArmorstand, Vector3 offset) {
        this.parentRideHandle = parentRideHandle;
        this.seatHost = seatHost;
        this.passenger = null;
        this.virtualArmorstand = virtualArmorstand;
        this.offset = offset;
        this.restraintLocked = true;
    }

    @Override
    public void setParentSeatHost(SeatHost seatHost) {
        this.seatHost = seatHost;
    }

    @Override
    public SeatHost getParentSeatHost() {
        return seatHost;
    }

    @Override
    public Player getPassenger() {
        return passenger;
    }

    @Override
    public boolean hasPassenger() {
        return passenger != null;
    }

    @Override
    public void setPassenger(Player player) {
        VirtualEntity virtualArmorstand = getEntity();
        Player passenger = getPassenger();

        if(passenger != null){ // Overtaking seat or player = null
            onPassengerExit(passenger);
        }

        this.passenger = player;
        virtualArmorstand.setPassenger(player);
        if(player != null){
            if(!player.hasPermission(Permissions.RIDE_ENTER)){
                JRidesPlugin.getLanguageFile().sendMessage(player, LanguageFileField.ERROR_GENERAL_NO_PERMISSION_MESSAGE);
                return;
            }

            onPassengerEnter(player);
        }
    }

    @Override
    public boolean ejectPassengerSoft(boolean teleport) {
        if(!hasPassenger()) return false;
        Player passenger = getPassenger();

        if(SoftEjector.hasTimer(passenger)){
            SoftEjector.removeTimer(passenger);
            setPassenger(null);
            if(teleport){
                PlayerLocation ejectLocation = getParentSeatHost().getEjectLocation();
                passenger.teleport(ejectLocation, true);
            }
            return true;
        }else{
            SoftEjector.addTimer(passenger);
            JRidesPlugin.getLanguageFile().sendMessage(passenger, LanguageFileField.NOTIFICATION_SHIFT_EXIT_CONFIRMATION);
            return false;
        }
    }

    @Override
    public Vector3 getOffset() {
        return offset;
    }

    @Override
    public void setLocation(Vector3 location, Quaternion orientation) {
        virtualArmorstand.setLocation(location, orientation);

        if(hasPassenger()){
            Quaternion smoothAnimationRotation = orientation.clone();
            smoothAnimationRotation.rotateY(90);
            passenger.setSmoothAnimationRotation(smoothAnimationRotation);
        }
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
    public RideHandle getParentRideHandle() {
        return parentRideHandle;
    }

    protected void onPassengerExit(Player passenger){
        passenger.setSeatedOn(null);
        virtualArmorstand.setPassenger(null);
        passenger.clearSmoothAnimationRotation();
        PlayerStandUpEvent.send(passenger, getParentSeatHost().getRideHandle().getRide());

        seatHost.onPlayerExit(passenger);
    }

    protected void onPassengerEnter(Player passenger){
        passenger.setSeatedOn(this);
    }
}