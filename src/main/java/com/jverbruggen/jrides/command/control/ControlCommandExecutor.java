package com.jverbruggen.jrides.command.control;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.command.JRidesCommandExecutor;
import com.jverbruggen.jrides.control.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ControlCommandExecutor implements JRidesCommandExecutor {
    private final RideManager rideManager;

    public ControlCommandExecutor() {
        rideManager = ServiceProvider.GetSingleton(RideManager.class);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg, String[] args) {
        if(args.length <= 2){
            commandSender.sendMessage(getHelpMessage());
            return true;
        }

        String identifier = args[1];
        String subCommand = args[2];

        if(subCommand.equalsIgnoreCase("dispatch")){
            CoasterHandle rideHandle = rideManager.getRideHandle(identifier);
            RideController rideController = rideHandle.getRideController();
            DispatchTrigger dispatchTrigger = rideController.getTriggerContext().getDispatchTrigger();

            boolean dispatched = dispatchTrigger.dispatch();
            if(dispatched) commandSender.sendMessage("Ride " + identifier + " was dispatched!");

            return true;
        }

        commandSender.sendMessage(getHelpMessage());
        return true;
    }

    private String getHelpMessage(){
        return "/jrides control <identifier> dispatch";
    }

    @Override
    public String getGenericHelpMessage() {
        return "/jrides control";
    }
}
