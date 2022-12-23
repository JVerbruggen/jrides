package com.jverbruggen.jrides.models.message;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.jverbruggen.jrides.models.entity.Player;

import java.lang.reflect.InvocationTargetException;

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

        try {
            this.protocolManager.sendServerPacket(player.getBukkitPlayer(), packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
