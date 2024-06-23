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
import com.jverbruggen.jrides.packets.Packet;
import com.jverbruggen.jrides.packets.packet.SingularServerPacket;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class SpawnArmorstandServerPacket extends SingularServerPacket implements Packet {
    private int entityId;
    private double locationX;
    private double locationY;
    private double locationZ;
    private double yawRotation;
    private UUID uuid;

    public SpawnArmorstandServerPacket(ProtocolManager protocolManager, int entityId, double locationX, double locationY, double locationZ, double yawRotation, UUID uuid) {
        super(protocolManager);
        this.entityId = entityId;
        this.locationX = locationX;
        this.locationY = locationY;
        this.locationZ = locationZ;
        this.yawRotation = yawRotation;
        this.uuid = uuid;
    }

    @Override
    public PacketContainer getPacket() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        packet.getIntegers()
                .write(0, entityId);
        packet.getEntityTypeModifier()
                .write(0, EntityType.ARMOR_STAND);
        packet.getDoubles()
                .write(0, locationX)
                .write(1, locationY)
                .write(2, locationZ);
        packet.getBytes()
                .write(1, (byte)yawRotation);
        packet.getUUIDs()
                .write(0, uuid);

        return packet;
    }
}
