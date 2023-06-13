package com.jverbruggen.jrides.command.control;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.command.BaseCommandExecutor;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ControlCommandExecutor extends BaseCommandExecutor {
    private final RideManager rideManager;

    public ControlCommandExecutor() {
        super(1);
        rideManager = ServiceProvider.getSingleton(RideManager.class);

        registerSubCommand(new ControlMenuCommandExecutor(depth+2));
        registerSubCommand(new ControlAdminMenuCommandExecutor(depth+2));
        registerSubCommand(new ControlDispatchCommandExecutor(depth+2));
    }

    @Override
    public boolean onCommand(MessageAgent messageAgent, Command command, String arg, String[] args, CommandContext commandContext) {
        if(args.length <= 2){
            languageFile.sendMultilineMessage(messageAgent, getHelpMessageForSelf());
            return true;
        }

        String identifier = args[1];
        String subCommand = args[2];

        commandContext.add(RideHandle.class, rideManager.getRideHandle(identifier));

        runSubCommand(messageAgent, command, arg, args, subCommand, commandContext);
        return true;
    }

    @Override
    public String getCommand() {
        return "control";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_CONTROL;
    }

    @Override
    public String getHelpMessageForParent() {
        return "/jrides control";
    }

    @Override
    public List<String> onTabComplete(MessageAgent messageAgent, Command command, String s, String[] strings) {
        if(!messageAgent.hasPermission(getPermission())) return Collections.emptyList();

        if(strings.length == depth+1){
            return rideManager.getRideIdentifiers();
        }else if(strings.length == depth+2){
            return getCommandSuggestions(messageAgent);
        }
        return null;
    }
}
