package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.command.context.CommandContext;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public interface JRidesCommandExecutor extends CommandExecutor, TabCompleter {
    String getHelpMessageForParent();
    String getCommand();
    String getHelpMessageForSelf();
    boolean onCommand(CommandSender commandSender, Command command, String s, String[] args, CommandContext commandContext);

}
