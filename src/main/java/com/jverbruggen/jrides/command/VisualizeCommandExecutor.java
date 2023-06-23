package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.tool.ParticleTrackVisualisationTool;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.command.Command;

import java.util.Collections;
import java.util.List;

public class VisualizeCommandExecutor extends BaseCommandExecutor {
    private final RideManager rideManager;

    protected VisualizeCommandExecutor() {
        super(1);
        this.rideManager = ServiceProvider.getSingleton(RideManager.class);
    }

    @Override
    public String getHelpMessageForParent() {
        return "/jrides visualize <identifier>";
    }

    @Override
    public boolean onCommand(MessageAgent messageAgent, Command command, String arg, String[] args, CommandContext context) {
        if(!messageAgent.isPlayer()){
            languageFile.sendMessage(messageAgent, LanguageFileField.ERROR_PLAYER_COMMAND_ONLY_MESSAGE);
            return true;
        }

        if(args.length != 2){
            languageFile.sendMessage(messageAgent, getHelpMessageForSelf());
            return true;
        }

        String identifier = args[1];
        if(identifier.equalsIgnoreCase("")){
            languageFile.sendMessage(messageAgent, getHelpMessageForSelf());
            return true;
        }

        RideHandle rideHandle = ServiceProvider.getSingleton(RideManager.class).getRideHandle(identifier);
        if(rideHandle == null){
            languageFile.sendMessage(messageAgent, "Ride '" + identifier + "' not found", FeedbackType.CONFLICT);
            return true;
        }

        if(!(rideHandle instanceof CoasterHandle coasterHandle)){
            languageFile.sendMessage(messageAgent, "Ride cannot be visualized: not a coaster", FeedbackType.CONFLICT);
            return true;
        }

        ParticleTrackVisualisationTool tool = coasterHandle.getVisualisationTool();
        if(tool == null){
            languageFile.sendMessage(messageAgent, "Could not visualize track", FeedbackType.CONFLICT);
            return true;
        }

        String actualIdentifier = coasterHandle.getRide().getIdentifier();
        Player player = messageAgent.getPlayer(playerManager);
        if(tool.isViewer(player)){
            tool.removeViewer(player);

            languageFile.sendMessage(player, LanguageFileField.COMMAND_VISUALIZE_REMOVED_VIEWER,
                    b -> b.add(LanguageFileTag.rideIdentifier, actualIdentifier));
        }else{
            tool.addViewer(player);

            languageFile.sendMessage(player, LanguageFileField.COMMAND_VISUALIZE_ADDED_VIEWER,
                    b -> b.add(LanguageFileTag.rideIdentifier, actualIdentifier));
        }

        return true;
    }

    @Override
    public String getHelpMessageForSelf() {
        return getHelpMessageForParent();
    }

    @Override
    public String getCommand() {
        return "visualize";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_ELEVATED_VISUALIZE;
    }

    @Override
    public List<String> onTabComplete(MessageAgent messageAgent, Command command, String s, String[] strings) {
        if(!messageAgent.hasPermission(getPermission())) return Collections.emptyList();

        if(strings.length == depth+1)
            return rideManager.getRideIdentifiers();
        return null;
    }
}
