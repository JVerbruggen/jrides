package com.jverbruggen.jrides.models.message;

import com.comphenix.protocol.ProtocolManager;

public class MessageFactory {
    private ProtocolManager protocolManager;

    public MessageFactory(ProtocolManager protocolManager) {
        this.protocolManager = protocolManager;
    }

    public Message getChatMessage(String contents){
        return new SimpleChatMessage(contents);
    }

    public Message getTitle(String title, String subtitle){
        return new TitleMessage(title, subtitle);
    }

    public Message getActionBarMessage(String contents){
        return new ActionBarMessage(protocolManager, contents);
    }
}
