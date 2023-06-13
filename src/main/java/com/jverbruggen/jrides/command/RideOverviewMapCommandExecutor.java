package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.language.LanguageFileFields;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.models.map.rideoverview.RideOverviewMapFactory;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
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
            languageFile.sendMessage(messageAgent, LanguageFileFields.ERROR_PLAYER_COMMAND_ONLY_MESSAGE);
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

        CoasterHandle coasterHandle = ServiceProvider.getSingleton(RideManager.class).getRideHandle(identifier);
        Player player = messageAgent.getPlayer(playerManager);
        rideOverviewMapFactory.giveMap(player, coasterHandle);

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
