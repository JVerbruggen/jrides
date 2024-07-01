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

package com.jverbruggen.jrides.packets.impl;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.packets.packet.v1_20.ItemDisplayMetaDataPacket;
import com.jverbruggen.jrides.packets.packet.v1_20.ItemDisplayModelServerPacket;
import org.bukkit.entity.ItemDisplay;

import java.util.List;

public class PacketSender_1_20_4 extends PacketSender_1_20_1 {
    public PacketSender_1_20_4(boolean debugMode) {
        super(debugMode);
    }

    @Override
    public String getIdentifier() {
        return "1.20.4";
    }

    @Override
    public void sendApplyItemDisplayModelPacket(Player player, int entityId, ItemDisplay.ItemDisplayTransform itemDisplayTransform, TrainModelItem model) {
        sendDebugLog("sendApplyItemDisplayModelPacket (single) 1.20.4");

        new ItemDisplayModelServerPacket(
                protocolManager, entityId, itemDisplayTransform, model
        ).send(player);
    }

    @Override
    public void sendApplyItemDisplayModelPacket(List<Player> players, int entityId, ItemDisplay.ItemDisplayTransform itemDisplayTransform, TrainModelItem model) {
        sendDebugLog("sendApplyItemDisplayModelPacket (single) 1.20.4");

        new ItemDisplayModelServerPacket(
                protocolManager, entityId, itemDisplayTransform, model
        ).sendAll(players);
    }

    @Override
    public void sendItemDisplayMetaDataPacket(Player player, int entityId, int PositionRotationInterpolationDuration) {
        sendDebugLog("sendItemDisplayMetaDataPacket (single) 1.20.4");

        new ItemDisplayMetaDataPacket(
                protocolManager, entityId, PositionRotationInterpolationDuration
        ).send(player);
    }

    @Override
    public void sendItemDisplayRotationPacket(List<Player> players, int entityId, Quaternion orientation, int positionRotationInterpolationDuration) {
        sendDebugLog("sendItemDisplayMetaDataPacket (multiple) 1.20.4");

        new ItemDisplayRotationPacket(
                protocolManager, entityId, orientation, positionRotationInterpolationDuration
        ).sendAll(players);
    }
}
