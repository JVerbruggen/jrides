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

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.entity.EntityType;

public class VirtualBukkitEntity extends BaseVirtualEntity {
    private final EntityType entityType;

    private final int leashedToEntity;
    private final double yawRotationOffset;
    private double currentYawRotation;


    public VirtualBukkitEntity(PacketSender packetSender, ViewportManager viewportManager, Vector3 location, EntityType entityType, double yawRotation, int entityId) {
        super(packetSender, viewportManager, location, entityId);
        this.entityType = entityType;
        this.yawRotationOffset = yawRotation;
        this.currentYawRotation = yawRotation;
        this.leashedToEntity = -1;
    }

    @Override
    protected void moveEntity(Vector3 delta, double yawRotation) {
        packetSender.moveVirtualEntity(this.getViewers(),
                entityId,
                delta,
                yawRotation + packetSender.toPacketYaw(this.yawRotationOffset));
    }

    @Override
    protected void teleportEntity(Vector3 newLocation) {
        packetSender.teleportVirtualEntity(this.getViewers(), entityId, newLocation);
    }

    @Override
    public Quaternion getRotation() {
        return Quaternion.fromYawPitchRoll(0, currentYawRotation, 0);
    }

    @Override
    public double getYaw() {
        return currentYawRotation;
    }

    @Override
    public void spawnFor(Player player) {
        addViewer(player);
        if(!rendered) return;

        packetSender.spawnVirtualEntity(player, entityId, location, getYaw(), entityType, false, leashedToEntity);
    }

    @Override
    public boolean shouldRenderFor(Player player) {
        return false;
    }

    @Override
    public void setModel(TrainModelItem model) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void setLocation(Vector3 newLocation) {
        super.setLocation(newLocation);
        if(newLocation == null) return;

        syncPassenger(newLocation);
    }

    @Override
    public void setRotation(Quaternion orientation) {
        super.setRotation(orientation);

        if(orientation != null)
            this.currentYawRotation = orientation.getYaw() + yawRotationOffset;
    }
}
