package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.command.control.ControlCommandExecutor;
import com.jverbruggen.jrides.state.player.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommandExecutor implements CommandExecutor {
    private VisualizeCommandExecutor visualizeCommandExecutor;
    private ControlCommandExecutor controlCommandExecutor;

    public MainCommandExecutor(PlayerManager playerManager) {
        this.visualizeCommandExecutor = new VisualizeCommandExecutor(playerManager);
        this.controlCommandExecutor = new ControlCommandExecutor();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length == 0){
            commandSender.sendMessage(visualizeCommandExecutor.getGenericHelpMessage());
            commandSender.sendMessage(controlCommandExecutor.getGenericHelpMessage());
            return true;
        }

        String subCommand = args[0];
        switch(subCommand){
            case "visualize":
                return visualizeCommandExecutor.onCommand(commandSender, command, s, args);
            case "control":
                return controlCommandExecutor.onCommand(commandSender, command, s, args);
        }

        commandSender.sendMessage("Unknown jrides command. Type /jrides for help");

        return false;
    }
}
