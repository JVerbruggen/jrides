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

package com.jverbruggen.jrides.models.message;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.jverbruggen.jrides.models.entity.Player;

public class ActionBarMessage implements Message {
    private ProtocolManager protocolManager;
    private String contents;

    public ActionBarMessage(ProtocolManager protocolManager, String contents) {
        this.protocolManager = protocolManager;
        this.contents = contents;
    }

    @Override
    public void send(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.CHAT);
        WrappedChatComponent wcc = WrappedChatComponent.fromJson("{\"text\": \"" + contents + "\"}");
        packet.getChatComponents()
                .write(0, wcc);
        packet.getChatTypes()
                .write(0, EnumWrappers.ChatType.GAME_INFO);

        this.protocolManager.sendServerPacket(player.getBukkitPlayer(), packet);
    }
}
