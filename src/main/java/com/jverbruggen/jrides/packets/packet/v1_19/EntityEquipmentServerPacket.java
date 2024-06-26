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
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.packets.packet.SingularServerPacket;
import com.jverbruggen.jrides.packets.Packet;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EntityEquipmentServerPacket extends SingularServerPacket implements Packet {
    private final int entityId;
    private final EnumWrappers.ItemSlot itemSlot;
    private final TrainModelItem model;

    public EntityEquipmentServerPacket(ProtocolManager protocolManager, int entityId, EnumWrappers.ItemSlot itemSlot, TrainModelItem model) {
        super(protocolManager);
        this.entityId = entityId;
        this.itemSlot = itemSlot;
        this.model = model;
    }

    @Override
    public PacketContainer getPacket() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packet.getIntegers().write(0, this.entityId);
        List<Pair<EnumWrappers.ItemSlot, ItemStack>> data = new ArrayList<>();
        data.add(new Pair<>(itemSlot, model.getItemStack()));
        packet.getSlotStackPairLists().write(0, data);

        return packet;
    }
}
