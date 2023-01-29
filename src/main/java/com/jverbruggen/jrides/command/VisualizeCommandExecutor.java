package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.tool.ParticleTrackVisualisationTool;
import com.jverbruggen.jrides.language.StringReplacementBuilder;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VisualizeCommandExecutor extends BaseCommandExecutor {
    @Override
    public String getGenericHelpMessage() {
        return "/jrides visualize <identifier>";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg, String[] args) {
        if(!(commandSender instanceof org.bukkit.entity.Player)){
            languageFile.sendMessage(commandSender, languageFile.errorPlayerCommandOnlyMessage);
            return true;
        }

        if(args.length != 2){
            languageFile.sendMessage(commandSender, getGenericHelpMessage());
            return true;
        }

        String identifier = args[1];

        CoasterHandle coasterHandle = ServiceProvider.getSingleton(RideManager.class).getRideHandle(identifier);
        ParticleTrackVisualisationTool tool = coasterHandle.getVisualisationTool();

        String actualIdentifier = coasterHandle.getRide().getIdentifier();
        Player player = playerManager.getPlayer((org.bukkit.entity.Player) commandSender);
        if(tool.isViewer(player)){
            tool.removeViewer(player);

            languageFile.sendMessage(player, languageFile.commandVisualizeRemovedViewer,
                    new StringReplacementBuilder().add("RIDE_IDENTIFIER", actualIdentifier).collect());
        }else{
            tool.addViewer(player);

            languageFile.sendMessage(player, languageFile.commandVisualizeAddedViewer,
                    new StringReplacementBuilder().add("RIDE_IDENTIFIER", actualIdentifier).collect());
        }

        return true;
    }
}