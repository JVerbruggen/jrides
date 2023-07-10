package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.models.map.ridecounter.RideCounterMapFactory;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.command.Command;

import java.util.Collections;
import java.util.List;

public class RideCounterMapCommandExecutor extends BaseCommandExecutor {
    private final RideManager rideManager;
    private final RideCounterMapFactory rideCounterMapFactory;

    protected RideCounterMapCommandExecutor() {
        super(1);
        this.rideManager = ServiceProvider.getSingleton(RideManager.class);
        this.rideCounterMapFactory = ServiceProvider.getSingleton(RideCounterMapFactory.class);
    }

    @Override
    public String getHelpMessageForParent() {
        return "/jrides "  + getCommand() + " <ride_identifier> <board_identifier>";
    }

    @Override
    public boolean onCommand(MessageAgent messageAgent, Command command, String arg, String[] args, CommandContext context) {
        if(!messageAgent.isPlayer()){
            languageFile.sendMessage(messageAgent, LanguageFileField.ERROR_PLAYER_COMMAND_ONLY_MESSAGE);
            return true;
        }

        if(args.length != 3){
            languageFile.sendMessage(messageAgent, getHelpMessageForSelf());
            return true;
        }

        String rideIdentifier = args[1];
        if(rideIdentifier.equalsIgnoreCase("")){
            languageFile.sendMessage(messageAgent, getHelpMessageForSelf());
            return true;
        }

        String boardIdentifier = args[2];
        if(boardIdentifier.equalsIgnoreCase("")){
            languageFile.sendMessage(messageAgent, getHelpMessageForSelf());
            return true;
        }

        RideHandle rideHandle = ServiceProvider.getSingleton(RideManager.class).getRideHandle(rideIdentifier);
        if(rideHandle == null){
            languageFile.sendMessage(messageAgent, "Ride '" + rideIdentifier + "' not found", FeedbackType.CONFLICT);
            return true;
        }

        Player player = messageAgent.getPlayer(playerManager);
        rideCounterMapFactory.giveMap(player, rideHandle, boardIdentifier);

        return true;
    }

    @Override
    public String getHelpMessageForSelf() {
        return getHelpMessageForParent();
    }

    @Override
    public String getCommand() {
        return "ridecounteroverview";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_ELEVATED_RIDE_COUNTER_MAP;
    }

    @Override
    public List<String> onTabComplete(MessageAgent messageAgent, Command command, String s, String[] strings) {
        if(!messageAgent.hasPermission(getPermission())) return Collections.emptyList();

        if(strings.length == depth+1)
            return rideManager.getRideIdentifiers();

        if(strings.length == depth+2)
            return rideCounterMapFactory.getBoardIdentifiersByRide(strings[depth+1]);

        return null;
    }


}
