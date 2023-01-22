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

    public BaseVirtualEntity(PacketSender packetSender, ViewportManager viewportManager, Vector3 location, int entityId) {
        this.packetSender = packetSender;
        this.viewportManager = viewportManager;
        this.entityId = entityId;
        this.uuid = UUID.randomUUID();
        this.location = location;
        this.viewers = new ArrayList<>();
        this.spawned = true;
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
    public void setLocation(Vector3 newLocation) {
        double distanceSquared = newLocation.distanceSquared(this.location);
        Vector3 delta = Vector3.subtract(newLocation, this.location);
        this.location = newLocation;

        viewportManager.updateForEntity(this);

        if(distanceSquared > 49) { // 7^2
            packetSender.teleportVirtualEntity(this.getViewers(), entityId, newLocation);
        }else{
            packetSender.moveVirtualArmorstand(this.getViewers(), entityId, delta, 0);
        }
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
