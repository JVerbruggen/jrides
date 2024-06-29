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

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.event.player.PlayerStandUpEvent;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.Passenger;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.SeatedOnContext;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.math.Vector3PlusYaw;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.flatride.PlayerControl;
import com.jverbruggen.jrides.state.ride.SoftEjector;

import javax.annotation.Nonnull;

public abstract class AbstractSeat implements Seat {
    private final RideHandle parentRideHandle;
    private Passenger passenger;
    private final VirtualEntity virtualEntity;
    private final Vector3PlusYaw offset;
    private boolean restraintLocked;
    private SeatHost seatHost;

    public AbstractSeat(RideHandle parentRideHandle, SeatHost seatHost, VirtualEntity virtualEntity, Vector3PlusYaw offset) {
        this.parentRideHandle = parentRideHandle;
        this.seatHost = seatHost;
        this.passenger = null;
        this.virtualEntity = virtualEntity;
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
    public Passenger getPassenger() {
        return passenger;
    }

    @Override
    public boolean hasPassenger() {
        return passenger != null;
    }

    @Override
    public void setPassenger(Player player) {
        VirtualEntity virtualArmorstand = getEntity();
        Passenger passenger = getPassenger();

        if(passenger != null){ // Overtaking seat or player = null
            if(passenger.getPlayer() == player) return;

            onPassengerExit(passenger);
        }

        Passenger newPassenger = new Passenger(player, this);
        this.passenger = newPassenger;
        virtualArmorstand.setPassenger(player);
        if(player != null){
            if(!player.hasPermission(Permissions.RIDE_ENTER)){
                JRidesPlugin.getLanguageFile().sendMessage(player, LanguageFileField.ERROR_GENERAL_NO_PERMISSION_MESSAGE);
                return;
            }

            onPassengerEnter(newPassenger);
        }
    }

    @Override
    public boolean ejectPassengerSoft(boolean teleport) {
        if(!hasPassenger()) return false;
        Passenger passenger = getPassenger();
        Player player = passenger.getPlayer();

        if(SoftEjector.hasTimer(player)){
            SoftEjector.removeTimer(player);
            setPassenger(null);
            if(teleport){
                PlayerLocation ejectLocation = getParentSeatHost().getEjectLocation();
                if(ejectLocation == null) ejectLocation = PlayerLocation.fromVector3(getEntity().getLocation());
                player.teleport(ejectLocation, true);
            }
            return true;
        }else{
            SoftEjector.addTimer(player);
            JRidesPlugin.getLanguageFile().sendMessage(player, LanguageFileField.NOTIFICATION_SHIFT_EXIT_CONFIRMATION);
            return false;
        }
    }

    @Override
    public Vector3PlusYaw getOffset() {
        return offset;
    }

    @Override
    public void setLocation(Vector3 location, Quaternion orientation) {
        virtualEntity.setLocation(location);
        virtualEntity.setRotation(orientation);

        if(hasPassenger()){
            Quaternion smoothAnimationRotation = orientation.clone();
            smoothAnimationRotation.rotateY(90);
            passenger.getPlayer().setSmoothAnimationRotation(smoothAnimationRotation);
        }
    }

    @Override
    public VirtualEntity getEntity() {
        return virtualEntity;
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

    @Override
    public boolean supportsPlayerControl() {
        return false;
    }

    @Nonnull
    @Override
    public PlayerControl getPlayerControl() {
        throw new RuntimeException("Unimplemented player control");
    }

    @Override
    public void sendPlayerControlInstruction(InstructionType instruction) {
        // Do nothing
    }

    protected void onPassengerExit(Passenger passenger){
        Player player = passenger.getPlayer();
        SeatedOnContext seatedOnContext = player.getSeatedOnContext();
        if(seatedOnContext != null)
            seatedOnContext.restore(player);

        virtualEntity.setPassenger(null);
        PlayerStandUpEvent.send(player, getParentSeatHost().getRideHandle().getRide());

        seatHost.onPlayerExit(passenger);
    }

    protected void onPassengerEnter(Passenger passenger){
        Player player = passenger.getPlayer();
        SeatedOnContext seatedOnContext = SeatedOnContext.create(this, player);
        seatedOnContext.setup(player);

        seatHost.onPlayerEnter(passenger);
    }
}
