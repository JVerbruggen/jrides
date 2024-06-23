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
import com.jverbruggen.jrides.packets.packet.SingularServerPacket;
import com.jverbruggen.jrides.packets.Packet;

public class AttachEntityServerPacket extends SingularServerPacket implements Packet {
    private int entityId;
    private int leashedToEntityId;

    public AttachEntityServerPacket(ProtocolManager protocolManager, int entityId, int leashedToEntityId) {
        super(protocolManager);
        this.entityId = entityId;
        this.leashedToEntityId = leashedToEntityId;
    }

    @Override
    public PacketContainer getPacket() {
        PacketContainer leashPacket = new PacketContainer(PacketType.Play.Server.ATTACH_ENTITY);
        leashPacket.getIntegers()
                .write(0, entityId)
                .write(1, leashedToEntityId);

        return leashPacket;
    }
}
