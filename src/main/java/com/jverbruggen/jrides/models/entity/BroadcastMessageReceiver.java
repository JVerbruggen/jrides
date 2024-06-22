package com.jverbruggen.jrides.models.entity;

import org.bukkit.Bukkit;

public class BroadcastMessageReceiver implements MessageReceiver {
    @Override
    public void sendMessage(String message) {
        Bukkit.broadcastMessage(message);
    }
}
