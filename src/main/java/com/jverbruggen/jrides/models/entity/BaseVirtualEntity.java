package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class BaseVirtualEntity implements VirtualEntity {
    protected PacketSender packetSender;
    protected ViewportManager viewportManager;

    protected boolean spawned;
    protected UUID uuid;
    protected int entityId;
    protected Vector3 location;
    protected List<Player> viewers;

    private int teleportSyncCoundownState; // If entity isn't teleported every few frames, it starts drifting due to only relative updates

    public BaseVirtualEntity(PacketSender packetSender, ViewportManager viewportManager, Vector3 location, int entityId) {
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
    public void setLocation(Vector3 newLocation, double yawRotation) {
        double distanceSquared = newLocation.distanceSquared(this.location);

        if(distanceSquared > 49 || teleportSyncCoundownState > 60) {
            Vector3 blockLocation = newLocation.toBlock();
            packetSender.teleportVirtualEntity(this.getViewers(), entityId, blockLocation);
            teleportSyncCoundownState = 0;

            Vector3 delta = Vector3.subtract(newLocation, newLocation.toBlock());
            packetSender.moveVirtualArmorstand(this.getViewers(), entityId, delta, yawRotation);
        }
        else{
            Vector3 delta = Vector3.subtract(newLocation, this.location);
            packetSender.moveVirtualArmorstand(this.getViewers(), entityId, delta, yawRotation);
        }

        this.location = newLocation;
        viewportManager.updateForEntity(this);

        teleportSyncCoundownState++;
    }

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
        packetSender.destroyVirtualEntity(player, entityId);
    }

    @Override
    public void spawnForAll(List<Player> players) {
        for(Player player : players){
            if(isViewer(player)) continue;
            spawnFor(player);
        }
    }
}
