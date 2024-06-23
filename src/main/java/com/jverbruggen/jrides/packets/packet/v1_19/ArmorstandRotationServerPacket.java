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
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.packet.SingularServerPacket;
import com.jverbruggen.jrides.packets.Packet;

public class ArmorstandRotationServerPacket extends SingularServerPacket implements Packet {
    public static class Type{
        public static final int HEAD = 16;
        public static final int BODY = 17;
        public static final int OFF_HAND = 18;
        public static final int MAIN_HAND = 19;
        public static final int LEFT_LEG = 20;
        public static final int RIGHT_LEG = 21;
    }

    protected final int entityId;
    protected final int rotationType;
    protected final Vector3 rotation;

    public ArmorstandRotationServerPacket(ProtocolManager protocolManager, int entityId, int rotationType, Vector3 rotation) {
        super(protocolManager);
        this.entityId = entityId;
        this.rotationType = rotationType;
        this.rotation = rotation;
    }

    @Override
    public PacketContainer getPacket() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, this.entityId);

        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.getVectorSerializer();
        watcher.setObject(rotationType, serializer, rotation.toVector3F());
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

        return packet;
    }
}
