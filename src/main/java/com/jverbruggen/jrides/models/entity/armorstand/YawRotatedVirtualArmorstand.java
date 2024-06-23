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

import com.jverbruggen.jrides.models.math.ArmorStandPose;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.packets.object.VirtualArmorstandConfiguration;
import com.jverbruggen.jrides.state.viewport.ViewportManager;

import javax.annotation.Nonnull;

public class YawRotatedVirtualArmorstand extends VirtualArmorstand {
    public YawRotatedVirtualArmorstand(PacketSender packetSender, ViewportManager viewportManager, Vector3 location, Quaternion rotation, double yawRotation, int entityId, @Nonnull VirtualArmorstandConfiguration configuration) {
        super(packetSender, viewportManager, location, rotation, yawRotation, entityId, configuration);
    }

    @Override
    public void setRotation(Quaternion orientation) {
        if(orientation == null) return;

        Vector3 headPose = ArmorStandPose.getArmorStandPose(orientation);
        headPose.y = 0;

        setHeadPose(headPose);
        setYaw(orientation.getPacketYaw());
        moveEntity(Vector3.zero(), getYaw());
    }
}
