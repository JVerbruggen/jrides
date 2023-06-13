package com.jverbruggen.jrides.models.entity.agent;

import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class MessageAgentManager {
    private final HashMap<String, MessageAgent> cache;

    public MessageAgentManager() {
        this.cache = new HashMap<>();
    }

    public MessageAgent getOrCreateMessageAgent(CommandSender commandSender){
        MessageAgent existing = cache.get(commandSender.getName());
        if(existing != null) return existing;

        existing = new CommandSenderMessageAgent(commandSender);
        cache.put(commandSender.getName(), existing);
        return existing;
    }

}
