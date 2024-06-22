package com.jverbruggen.jrides.models.entity.agent;

import com.jverbruggen.jrides.state.player.PlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSenderMessageAgent implements MessageAgent {
    private CommandSender commandSender;

    public CommandSenderMessageAgent(CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    @Override
    public boolean hasPermission(String permission) {
        if(permission == null) return true;
        return commandSender.hasPermission(permission);
    }

    @Override
    public boolean isPlayer() {
        return commandSender instanceof Player;
    }

    @Override
    public com.jverbruggen.jrides.models.entity.Player getPlayer(PlayerManager playerManager) {
        return playerManager.getPlayer((Player) commandSender);
    }

    @Override
    public void sendMessage(String message) {
        commandSender.sendMessage(message);
    }
}
