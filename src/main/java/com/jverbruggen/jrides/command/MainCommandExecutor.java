package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.command.control.ControlCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MainCommandExecutor extends BaseCommandExecutor {
    private VisualizeCommandExecutor visualizeCommandExecutor;
    private ControlCommandExecutor controlCommandExecutor;

    public MainCommandExecutor() {
        this.visualizeCommandExecutor = new VisualizeCommandExecutor();
        this.controlCommandExecutor = new ControlCommandExecutor();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length == 0){
            languageFile.sendMultilineMessage(commandSender, getGenericHelpMessage());
            return true;
        }

        String subCommand = args[0];
        switch(subCommand){
            case "visualize":
                return visualizeCommandExecutor.onCommand(commandSender, command, s, args);
            case "control":
                return controlCommandExecutor.onCommand(commandSender, command, s, args);
        }

        languageFile.sendMessage(commandSender, languageFile.errorUnknownCommandMessage);

        return false;
    }

    @Override
    public String getGenericHelpMessage() {
        return visualizeCommandExecutor.getGenericHelpMessage() + "\n" +
                controlCommandExecutor.getGenericHelpMessage();
    }
}
