package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.animator.GCRideHandle;
import com.jverbruggen.jrides.animator.tool.ParticleTrackVisualisationTool;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class JRidesCommandExecutor implements CommandExecutor {
    private PlayerManager playerManager;

    public JRidesCommandExecutor(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length == 0){
            commandSender.sendMessage("/jrides visualize <identifier>");
            return true;
        }

        String subCommand = args[0];
        switch(subCommand){
            case "visualize":
                return commandVisualize(commandSender, command, s, args);
        }

        commandSender.sendMessage("Unknown jrides command");

        return false;
    }

    private boolean commandVisualize(CommandSender commandSender, Command command, String s, String[] args){
        if(!(commandSender instanceof org.bukkit.entity.Player)){
            commandSender.sendMessage("Only for players");
            return true;
        }

        if(args.length != 2){
            commandSender.sendMessage("/jrides visualize <identifier>");
            return true;
        }

        String identifier = args[0];

        GCRideHandle rideHandle = ServiceProvider.GetSingleton(RideManager.class).getRideHandle(identifier);
        ParticleTrackVisualisationTool tool = rideHandle.getVisualisationTool();

        Player player = playerManager.getPlayer((org.bukkit.entity.Player) commandSender);
        if(tool.isViewer(player)){
            tool.removeViewer(player);
            player.sendMessage("Removed from viewing ride");
        }else{
            tool.addViewer(player);
            player.sendMessage("Added to viewing ride");
        }

        return true;
    }
}
