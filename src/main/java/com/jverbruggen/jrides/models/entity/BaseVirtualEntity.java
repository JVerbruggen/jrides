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

package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.event.action.RideAction;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.seat.Seat;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class BaseVirtualEntity implements VirtualEntity {
    protected PacketSender packetSender;
    protected ViewportManager viewportManager;

    private Player passenger;
    private boolean allowsPassengerValue;
    private boolean passengerSyncCounterActive;
    private int passengerSyncCounter;
    private Seat partOfSeat;
    private String customName;

    protected boolean spawned;
    protected boolean rendered;
    protected UUID uuid;
    protected final int entityId;
    protected Vector3 location;
    protected List<Player> viewers;

    private int teleportSyncCountdownState; // If entity isn't teleported every few frames, it starts drifting due to only relative updates

    private RideAction customAction;
    private RideHandle belongsToRideHandle;

    public BaseVirtualEntity(PacketSender packetSender, ViewportManager viewportManager, Vector3 location, int entityId) {
        this.passenger = null;
        this.allowsPassengerValue = false;
        this.passengerSyncCounterActive = false;
        this.passengerSyncCounter = 0;
        this.partOfSeat = null;
        this.customName = null;

        this.packetSender = packetSender;
        this.viewportManager = viewportManager;
        this.entityId = entityId;
        this.uuid = UUID.randomUUID();
        this.location = location;
        this.viewers = new ArrayList<>();
        this.spawned = true;
        this.rendered = true;
        this.teleportSyncCountdownState = 0;
        this.customAction = null;
        this.belongsToRideHandle = null;
    }

    @Override
    public Player getPassenger() {
        return passenger;
    }

    @Override
    public boolean allowsPassenger() {
        return allowsPassengerValue;
    }

    @Override
    public boolean hasPassenger() {
        return passenger != null;
    }

    @Override
    public void setPassenger(Player player) {
        this.passenger = player;

        packetSender.sendMountVirtualEntityPacket(viewers, player, entityId);

        if(player != null){
            this.passengerSyncCounterActive = true;
            this.passengerSyncCounter = 0;
        }else{
            this.passengerSyncCounterActive = false;
        }
    }

    @Override
    public void setHostSeat(Seat seat) {
        partOfSeat = seat;
        this.allowsPassengerValue = true;
    }

    @Override
    public Seat getHostSeat() {
        return partOfSeat;
    }

    @Override
    public List<Player> getViewers() {
        return viewers;
    }

    @Override
    public void addViewer(Player player) {
        if(viewers.contains(player)) return;

        viewers.add(player);
    }

    @Override
    public void removeViewer(Player player) {
        viewers.remove(player);
    }

    @Override
    public boolean isViewer(Player player) {
        return viewers.contains(player);
    }

    @Override
    public Vector3 getLocation() {
        return location;
    }

    @Override
    public void setLocation(Vector3 newLocation) {
        if(newLocation == null) return;

        final int chunkSize = viewportManager.getRenderChunkSize();

        if(Vector3.chunkRotated(location, newLocation, chunkSize)){
            viewportManager.updateForEntity(this);
        }

        double distanceSquared = newLocation.distanceSquared(this.location);

        if(distanceSquared > 49 || teleportSyncCountdownState > 60) {
            Vector3 blockLocation = newLocation.toBlock();
            teleportEntity(blockLocation);
            teleportSyncCountdownState = 0;

            Vector3 delta = Vector3.subtract(newLocation, newLocation.toBlock());
            moveEntity(delta, 0);
        }
        else{
            Vector3 delta = Vector3.subtract(newLocation, this.location);
            moveEntity(delta, 0);
        }

        this.location = newLocation;

        teleportSyncCountdownState++;
    }

    @Override
    public void setRotation(Quaternion orientation) {
        if(orientation == null) return;
        double packetYaw = packetSender.toPacketYaw(orientation.getYaw() - 90);

        moveEntity(Vector3.zero(), packetYaw);
    }

    protected abstract void moveEntity(Vector3 delta, double yawRotation);

    protected abstract void teleportEntity(Vector3 newLocation);

    @Override
    public String getUniqueIdentifier() {
        return uuid.toString();
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public int getEntityId() {
        return entityId;
    }

    @Override
    public void despawn() {
        spawned = false;
        packetSender.destroyVirtualEntity(getViewers(), entityId);
    }

    @Override
    public void despawnFor(Player player, boolean unview) {
        if(unview)
            removeViewer(player);

        packetSender.destroyVirtualEntity(player, entityId);
    }

    @Override
    public void spawnForAll(List<Player> players, boolean hard) {
        for(Player player : players){
            if(isViewer(player) && !hard) continue;
            spawnFor(player);
        }
    }

    public void despawnForAll(List<Player> players, boolean unview){
        for(Player player : List.copyOf(players)){
            if(!isViewer(player)) continue;
            despawnFor(player, unview);
        }
    }

    @Override
    public boolean isAlive() {
        return spawned;
    }

    protected void syncPassenger(Vector3 position){
        if(passengerSyncCounterActive){
            if(passengerSyncCounter > 20){
                passengerSyncCounter = 0;

                this.passenger.setPositionWithoutTeleport(position);
            }else passengerSyncCounter++;
        }
    }

    @Override
    public void setCustomName(String customName) {
        this.customName = customName;

        // TODO: Apply custom name packets
    }

    @Override
    public void setCustomAction(RideAction action) {
        this.customAction = action;
    }

    @Override
    public boolean hasCustomAction() {
        return this.customAction != null;
    }

    @Override
    public void runCustomAction(Player player) {
        if(belongsToRideHandle == null){
            JRidesPlugin.getLogger().severe("Custom action failed because it doesnot belong to a ride handle");
            return;
        }
        this.customAction.accept(this, belongsToRideHandle, player);
    }

    @Override
    public void setBelongsToRide(RideHandle rideHandle) {
        belongsToRideHandle = rideHandle;
    }

    @Override
    public void setRendered(boolean render) {
        if(render && !rendered){
            rendered = true;
            spawnForAll(getViewers(), true);
        }else if(!render && rendered){
            rendered = false;
            despawnForAll(getViewers(), false);
        }
    }

    @Override
    public boolean isRendered() {
        return rendered;
    }
}
