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

package com.jverbruggen.jrides.packets.packet.v1_19;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.packet.SingularServerPacket;
import com.jverbruggen.jrides.packets.Packet;

public class ArmorstandMoveServerPacket extends SingularServerPacket implements Packet {
    private int entityId;
    private Vector3 location;
    private double yawRotation;

    public ArmorstandMoveServerPacket(ProtocolManager protocolManager, int entityId, Vector3 location, double yawRotation) {
        super(protocolManager);
        this.entityId = entityId;
        this.location = location;
        this.yawRotation = yawRotation;
    }

    @Override
    public PacketContainer getPacket(){
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.REL_ENTITY_MOVE_LOOK);
        packet.getIntegers()
                .write(0, entityId);
        packet.getShorts()
                .write(0, (short)((location.getX() * 32) * 128))
                .write(1, (short)((location.getY() * 32) * 128))
                .write(2, (short)((location.getZ() * 32) * 128));
        packet.getBytes()
                .write(0, (byte)(yawRotation));
        packet.getBooleans()
                .write(0, false);

        return packet;
    }
}
