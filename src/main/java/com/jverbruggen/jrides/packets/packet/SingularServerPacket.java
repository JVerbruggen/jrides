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

package com.jverbruggen.jrides.packets.packet;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.packets.Packet;

import java.util.List;

public abstract class SingularServerPacket implements Packet {
    protected ProtocolManager protocolManager;

    public SingularServerPacket(ProtocolManager protocolManager) {
        this.protocolManager = protocolManager;
    }

    public abstract PacketContainer getPacket();

    protected boolean sendPacket(Player player, PacketContainer packet){
        this.protocolManager.sendServerPacket(player.getBukkitPlayer(), packet);
        return true;
    }

    @Override
    public boolean send(Player player) {
        return sendPacket(player, getPacket());
    }

    @Override
    public void sendAll(List<Player> players) {
        PacketContainer packet = getPacket();
        for(Player player : players){
            sendPacket(player, packet);
        }
    }
}
