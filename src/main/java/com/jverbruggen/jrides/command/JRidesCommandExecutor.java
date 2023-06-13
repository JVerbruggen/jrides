package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public interface JRidesCommandExecutor extends CommandExecutor, TabCompleter {
    String getHelpMessageForParent();
    String getCommand();
    String getHelpMessageForSelf();
    boolean onCommand(MessageAgent messageAgent, Command command, String s, String[] args, CommandContext commandContext);
    List<String> onTabComplete(MessageAgent messageAgent, Command command, String s, String[] strings);
    String getPermission();

}
