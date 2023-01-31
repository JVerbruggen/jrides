package com.jverbruggen.jrides.models.message;

import com.comphenix.protocol.ProtocolManager;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

public class MessageFactory {
    private ProtocolManager protocolManager;

    public MessageFactory() {
        this.protocolManager = ServiceProvider.getSingleton(ProtocolManager.class);
    }

    public Message getTitle(String title, String subtitle){
        return new TitleMessage(title, subtitle);
    }

    public Message getActionBarMessage(String contents){
        return new ActionBarMessage(protocolManager, contents);
    }
}
