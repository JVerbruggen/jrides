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
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.packet.v1_20.ArmorstandRotationServerPacket;
import com.jverbruggen.jrides.packets.packet.v1_20.EntityMetadataServerPacket;
import org.bukkit.Bukkit;

import java.util.List;

public class PacketSender_1_20_1 extends PacketSender_1_19_2 {
    private static final Vector3 ARMORSTAND_MODEL_COMPENSATION_1_20_1 = new Vector3(0, 1.7, 0);

    public PacketSender_1_20_1(boolean debugMode) {
        super(debugMode);
    }

    public void sendRotationPacket(Player player, int entityId, int rotationType, Vector3 rotation){
        sendDebugLog("sendRotationPacket (single) 1.20.1");

        new ArmorstandRotationServerPacket(
                protocolManager, entityId, rotationType, rotation
        ).send(player);
    }

    @Override
    public void sendRotationPacket(List<Player> players, int entityId, int rotationType, Vector3 rotation) {
        sendDebugLog("sendRotationPacket (multiple) 1.20.1");

        new ArmorstandRotationServerPacket(
                protocolManager, entityId, rotationType, rotation
        ).sendAll(players);
    }

    @Override
    public void sendEntityMetaDataPacket(Player player, int entityId, boolean invisible, String customName) {
        sendDebugLog("sendEntityMetaDataPacket (single) 1.20.1");

        new EntityMetadataServerPacket(
                protocolManager, entityId, invisible, customName
        ).send(player);
    }

    @Override
    public Vector3 getArmorstandModelCompensationVector() {
        return ARMORSTAND_MODEL_COMPENSATION_1_20_1;
    }

    @Override
    public String getIdentifier() {
        return "1.20.1";
    }
}
