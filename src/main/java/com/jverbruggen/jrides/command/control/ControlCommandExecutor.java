package com.jverbruggen.jrides.command.control;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.command.BaseCommandExecutor;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenuFactory;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ControlCommandExecutor extends BaseCommandExecutor {
    private final RideManager rideManager;
    private final RideControlMenuFactory rideControlMenuFactory;
    private final ControlMenuCommandExecutor controlMenuCommandExecutor;
    private final ControlDispatchCommandExecutor controlDispatchCommandExecutor;

    public ControlCommandExecutor(int depth) {
        super(depth);
        rideManager = ServiceProvider.getSingleton(RideManager.class);
        rideControlMenuFactory = ServiceProvider.getSingleton(RideControlMenuFactory.class);

        controlMenuCommandExecutor = registerSubCommand(new ControlMenuCommandExecutor(depth+2));
        controlDispatchCommandExecutor = registerSubCommand(new ControlDispatchCommandExecutor(depth+2));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg, String[] args, CommandContext commandContext) {
        if(args.length <= 2){
            languageFile.sendMultilineMessage(commandSender, getHelpMessageForSelf());
            return true;
        }


        String identifier = args[1];
        String subCommand = args[2];

        commandContext.add(CoasterHandle.class, rideManager.getRideHandle(identifier));

        runSubCommand(commandSender, command, arg, args, subCommand, commandContext);

        return true;
    }

    @Override
    public String getCommand() {
        return "control";
    }

    @Override
    public String getHelpMessageForParent() {
        return "/jrides control";
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == depth+1){
            return rideManager.getRideIdentifiers();
        }else if(strings.length == depth+2){
            return getCommandSuggestions();
        }
        return null;
    }
}
