package com.jverbruggen.jrides.models.entity;

import org.bukkit.command.CommandSender;

public class SimpleMessageReceiver implements MessageReceiver {
    private final CommandSender commandSender;

    public SimpleMessageReceiver(CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    @Override
    public void sendMessage(String message) {
        commandSender.sendMessage(message);
    }

    public static SimpleMessageReceiver from(CommandSender commandSender){
        return new SimpleMessageReceiver(commandSender);
    }
}
