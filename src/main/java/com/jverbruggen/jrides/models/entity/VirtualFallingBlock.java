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

public class VirtualFallingBlock extends BaseVirtualEntity {
    public VirtualFallingBlock(PacketSender packetSender, ViewportManager viewportManager, Vector3 location, int entityId) {
        super(packetSender, viewportManager, location, entityId);
    }

    @Override
    protected void moveEntity(Vector3 delta, double yawRotation) {

    }

    @Override
    protected void teleportEntity(Vector3 newLocation) {

    }

    @Override
    public Player getPassenger() {
        return null;
    }

    @Override
    public boolean allowsPassenger() {
        return false;
    }

    @Override
    public boolean hasPassenger() {
        return false;
    }

    @Override
    public void setPassenger(Player player) {
        return;
    }

    @Override
    public Quaternion getRotation() {
        return new Quaternion();
    }

    @Override
    public double getYaw() {
        return 0;
    }

    @Override
    public void spawnFor(Player player) {
        addViewer(player);

        if(!rendered)
            return;

        return;
    }

    @Override
    public boolean shouldRenderFor(Player player) {
        return false;
    }

    @Override
    public void setModel(TrainModelItem model) {
        throw new RuntimeException("Not implemented");
    }
}
