package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.seat.Seat;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.state.viewport.ViewportManager;

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

    protected boolean spawned;
    protected UUID uuid;
    protected int entityId;
    protected Vector3 location;
    protected List<Player> viewers;

    private int teleportSyncCoundownState; // If entity isn't teleported every few frames, it starts drifting due to only relative updates

    public BaseVirtualEntity(PacketSender packetSender, ViewportManager viewportManager, Vector3 location, int entityId) {
        this.passenger = null;
        this.allowsPassengerValue = false;
        this.passengerSyncCounterActive = false;
        this.passengerSyncCounter = 0;
        this.partOfSeat = null;

        this.packetSender = packetSender;
        this.viewportManager = viewportManager;
        this.entityId = entityId;
        this.uuid = UUID.randomUUID();
        this.location = location;
        this.viewers = new ArrayList<>();
        this.spawned = true;
        this.teleportSyncCoundownState = 0;
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
        setAllowsPassengerValue(true);
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
    public void setLocation(Vector3 newLocation, Quaternion orientation) {
        final int chunkSize = viewportManager.getRenderChunkSize();

        if(Vector3.chunkRotated(location, newLocation, chunkSize)){
            viewportManager.updateForEntity(this);
        }

        double distanceSquared = newLocation.distanceSquared(this.location);
        double packetYaw = orientation == null ? 0 : orientation.getPacketYaw();

        if(distanceSquared > 49 || teleportSyncCoundownState > 60) {
            Vector3 blockLocation = newLocation.toBlock();
            teleportEntity(blockLocation);
            teleportSyncCoundownState = 0;

            Vector3 delta = Vector3.subtract(newLocation, newLocation.toBlock());
            moveEntity(delta, packetYaw);
        }
        else{
            Vector3 delta = Vector3.subtract(newLocation, this.location);
            moveEntity(delta, packetYaw);
        }

        this.location = newLocation;

        teleportSyncCoundownState++;
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
    public void despawnFor(Player player) {
        removeViewer(player);
        packetSender.destroyVirtualEntity(player, entityId);
    }

    @Override
    public void spawnForAll(List<Player> players) {
        for(Player player : players){
            if(isViewer(player)) continue;
            spawnFor(player);
        }
    }

    @Override
    public boolean isAlive() {
        return spawned;
    }

    protected void setAllowsPassengerValue(boolean allowsPassengerValue) {
        this.allowsPassengerValue = allowsPassengerValue;
    }

    protected void syncPassenger(Vector3 position){
        if(passengerSyncCounterActive){
            if(passengerSyncCounter > 20){
                passengerSyncCounter = 0;

                this.passenger.setPositionWithoutTeleport(position);
            }else passengerSyncCounter++;
        }
    }
}
