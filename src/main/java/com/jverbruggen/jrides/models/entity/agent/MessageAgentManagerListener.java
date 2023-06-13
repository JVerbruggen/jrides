package com.jverbruggen.jrides.models.entity.agent;

import com.jverbruggen.jrides.event.player.PlayerQuitEvent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MessageAgentManagerListener implements Listener {
    private final MessageAgentManager messageAgentManager;

    public MessageAgentManagerListener() {
        this.messageAgentManager = ServiceProvider.getSingleton(MessageAgentManager.class);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        messageAgentManager.removeMessageAgent(e.getPlayer().getName());
    }
}
