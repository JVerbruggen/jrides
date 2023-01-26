package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.tool.ParticleTrackVisualisationTool;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VisualizeCommandExecutor implements JRidesCommandExecutor {
    private final PlayerManager playerManager;

    public VisualizeCommandExecutor(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public String getGenericHelpMessage() {
        return "/jrides visualize <identifier>";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg, String[] args) {
        if(!(commandSender instanceof org.bukkit.entity.Player)){
            commandSender.sendMessage("Only for players");
            return true;
        }

        if(args.length != 2){
            commandSender.sendMessage("/jrides visualize <identifier>");
            return true;
        }

        String identifier = args[1];

        CoasterHandle coasterHandle = ServiceProvider.GetSingleton(RideManager.class).getRideHandle(identifier);
        ParticleTrackVisualisationTool tool = coasterHandle.getVisualisationTool();

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
