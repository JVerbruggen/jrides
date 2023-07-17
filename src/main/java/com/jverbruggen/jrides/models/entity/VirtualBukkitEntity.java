package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.entity.EntityType;

public class VirtualBukkitEntity extends BaseVirtualEntity implements VirtualEntity {
    private final EntityType entityType;

    private double yawRotation;
    private final int leashedToEntity;

    public VirtualBukkitEntity(PacketSender packetSender, ViewportManager viewportManager, Vector3 location, EntityType entityType, double yawRotation, int entityId) {
        super(packetSender, viewportManager, location, entityId);
        this.entityType = entityType;
        this.yawRotation = yawRotation;
        this.leashedToEntity = -1;
    }

    @Override
    protected void moveEntity(Vector3 delta, double yawRotation) {
        packetSender.moveVirtualEntity(this.getViewers(), entityId, delta, yawRotation);
    }

    @Override
    protected void teleportEntity(Vector3 newLocation) {
        packetSender.teleportVirtualEntity(this.getViewers(), entityId, newLocation);
    }

    @Override
    public Quaternion getRotation() {
        return Quaternion.fromYawPitchRoll(0, yawRotation, 0);
    }

    @Override
    public double getYaw() {
        return yawRotation;
    }

    @Override
    public void spawnFor(Player player) {
        addViewer(player);
        packetSender.spawnVirtualEntity(player, entityId, location, getYaw(), entityType, false, leashedToEntity);
    }

    @Override
    public boolean shouldRenderFor(Player player) {
        return false;
    }

    @Override
    public void setLocation(Vector3 newLocation) {
        super.setLocation(newLocation);

        syncPassenger(newLocation);
    }

    @Override
    public void setRotation(Quaternion orientation) {
        super.setRotation(orientation);

        if(orientation != null)
            this.yawRotation = orientation.getYaw() - 90;
    }
}
