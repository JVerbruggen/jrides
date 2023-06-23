package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.event.player.PlayerTeleportByJRidesEvent;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.command.Command;

import java.util.Collections;
import java.util.List;

public class WarpCommandExecutor extends BaseCommandExecutor {
    private final RideManager rideManager;

    protected WarpCommandExecutor() {
        super(1);
        this.rideManager = ServiceProvider.getSingleton(RideManager.class);
    }

    @Override
    public String getHelpMessageForParent() {
        return "/jrides warp <identifier>";
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

        Player player = messageAgent.getPlayer(playerManager);
        if(!player.getBukkitPlayer().hasPermission(Permissions.COMMAND_RIDE_WARP)){
            languageFile.sendMessage(player, LanguageFileField.ERROR_GENERAL_NO_PERMISSION_MESSAGE);
            return false;
        }

        RideHandle rideHandle = ServiceProvider.getSingleton(RideManager.class).getRideHandle(identifier);
        if(rideHandle == null){
            languageFile.sendMessage(messageAgent, "Ride '" + identifier + "' not found", FeedbackType.CONFLICT);
            return true;
        }
        PlayerTeleportByJRidesEvent.sendEvent(player, rideHandle.getRide().getWarpLocation());

        player.teleport(rideHandle.getRide().getWarpLocation());
        return true;
    }

    @Override
    public String getHelpMessageForSelf() {
        return getHelpMessageForParent();
    }

    @Override
    public String getCommand() {
        return "warp";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_RIDE_WARP;
    }

    @Override
    public List<String> onTabComplete(MessageAgent messageAgent, Command command, String s, String[] strings) {
        if(!messageAgent.hasPermission(getPermission())) return Collections.emptyList();

        if(strings.length == depth+1)
            return rideManager.getRideIdentifiers();
        return null;
    }
}
