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

package com.jverbruggen.jrides.models.entity.armorstand;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.models.entity.BaseVirtualEntity;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.math.ArmorStandPose;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.packets.object.VirtualArmorstandConfiguration;
import com.jverbruggen.jrides.packets.packet.v1_19.ArmorstandRotationServerPacket;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.List;

public class VirtualArmorstand extends BaseVirtualEntity {
    private static Vector3 ARMORSTAND_MODEL_COMPENSATION = null;

    private final Quaternion currentRotation;
    private double yawRotation;
    private final VirtualArmorstandConfiguration configuration;

    public VirtualArmorstand(PacketSender packetSender, ViewportManager viewportManager, Vector3 location, Quaternion rotation, double yawRotation, int entityId, @Nonnull VirtualArmorstandConfiguration configuration) {
        super(packetSender, viewportManager, location, entityId);

        this.currentRotation = rotation;
        this.yawRotation = yawRotation;
        this.configuration = configuration;

        this.fillArmorstandCompensationVector(packetSender);
    }

    private void fillArmorstandCompensationVector(PacketSender packetSender){
        if(ARMORSTAND_MODEL_COMPENSATION == null)
            ARMORSTAND_MODEL_COMPENSATION = packetSender.getArmorstandModelCompensationVector();
    }

    @Override
    public Quaternion getRotation() {
        return currentRotation;
    }

    @Override
    public double getYaw() {
        return yawRotation;
    }

    public void setYaw(double yawRotation){
        this.yawRotation = yawRotation;
    }

    @Override
    public void spawnFor(Player player) {
        super.spawnFor(player);

        addViewer(player);

        if(!rendered) return;

        packetSender.spawnVirtualArmorstand(player, entityId, location, yawRotation, configuration);

        if(configuration.models().hasHead()){
            this.packetSender.sendApplyModelPacket(viewers, entityId, EnumWrappers.ItemSlot.HEAD, configuration.models().getHead());
        }

        setHeadPose(ArmorStandPose.getArmorStandPose(this.currentRotation));

        if(getPassenger() != null){
            Bukkit.getScheduler().runTaskLater(JRidesPlugin.getBukkitPlugin(),
                    () -> packetSender.sendMountVirtualEntityPacket(List.of(player), getPassenger(), entityId), 1L);
        }
    }

    @Override
    public boolean shouldRenderFor(Player player) {
        return false;
    }

    protected void setHeadPose(Vector3 rotation) {
        configuration.rotations().setHead(rotation);
        packetSender.sendRotationPacket(viewers, entityId, ArmorstandRotationServerPacket.Type.HEAD, rotation);
    }

    public void setModel(TrainModelItem model){
        this.configuration.models().setHead(model);
        this.packetSender.sendApplyModelPacket(viewers, entityId, EnumWrappers.ItemSlot.HEAD, model);
    }

    @Override
    public Vector3 getLocation() {
        Vector3 actualArmorstandLocation = super.getLocation();
        return Vector3.add(actualArmorstandLocation, ARMORSTAND_MODEL_COMPENSATION);
    }

    @Override
    public void setLocation(Vector3 newLocation) {
        Vector3 actualArmorstandLocation = Vector3.subtract(newLocation, ARMORSTAND_MODEL_COMPENSATION);
        super.setLocation(actualArmorstandLocation);

        if(actualArmorstandLocation == null) return;

        syncPassenger(newLocation);
    }

    @Override
    public void setRotation(Quaternion orientation) {
        if(orientation == null) return;

        currentRotation.copyFrom(orientation);
        setHeadPose(ArmorStandPose.getArmorStandPose(orientation));
    }

    @Override
    protected void moveEntity(Vector3 delta, double yawRotation) {
        packetSender.moveVirtualArmorstand(this.getViewers(), entityId, delta, yawRotation);
    }

    @Override
    protected void teleportEntity(Vector3 newLocation) {
        packetSender.teleportVirtualEntity(this.getViewers(), entityId, newLocation);
    }
}
