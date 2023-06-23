package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.models.map.rideoverview.RideOverviewMapFactory;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.command.Command;

import java.util.Collections;
import java.util.List;

public class RideOverviewMapCommandExecutor extends BaseCommandExecutor {
    private final RideManager rideManager;
    private final RideOverviewMapFactory rideOverviewMapFactory;

    protected RideOverviewMapCommandExecutor() {
        super(1);
        this.rideManager = ServiceProvider.getSingleton(RideManager.class);
        this.rideOverviewMapFactory = ServiceProvider.getSingleton(RideOverviewMapFactory.class);
    }

    @Override
    public String getHelpMessageForParent() {
        return "/jrides "  + getCommand() + " <identifier>";
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

        if(!(rideHandle instanceof CoasterHandle)){
            languageFile.sendMessage(messageAgent, "Ride has no track", FeedbackType.CONFLICT);
            return true;
        }

        Player player = messageAgent.getPlayer(playerManager);
        rideOverviewMapFactory.giveMap(player, ((CoasterHandle) rideHandle));

        return true;
    }

    @Override
    public String getHelpMessageForSelf() {
        return getHelpMessageForParent();
    }

    @Override
    public String getCommand() {
        return "rideoverview";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_ELEVATED_RIDE_OVERVIEW;
    }

    @Override
    public List<String> onTabComplete(MessageAgent messageAgent, Command command, String s, String[] strings) {
        if(!messageAgent.hasPermission(getPermission())) return Collections.emptyList();

        if(strings.length == depth+1)
            return rideManager.getRideIdentifiers();
        return null;
    }
}
