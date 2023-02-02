package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.tool.ParticleTrackVisualisationTool;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.language.LanguageFileTags;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class WarpCommandExecutor extends BaseCommandExecutor {
    private final RideManager rideManager;

    protected WarpCommandExecutor(int depth) {
        super(depth);
        this.rideManager = ServiceProvider.getSingleton(RideManager.class);
    }

    @Override
    public String getHelpMessageForParent() {
        return "/jrides warp <identifier>";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg, String[] args, CommandContext context) {
        if(!(commandSender instanceof org.bukkit.entity.Player)){
            languageFile.sendMessage(commandSender, languageFile.errorPlayerCommandOnlyMessage);
            return true;
        }

        if(args.length != 2){
            languageFile.sendMessage(commandSender, getHelpMessageForSelf());
            return true;
        }

        String identifier = args[1];

        CoasterHandle coasterHandle = ServiceProvider.getSingleton(RideManager.class).getRideHandle(identifier);
        Player player = playerManager.getPlayer((org.bukkit.entity.Player) commandSender);
        if(!player.getBukkitPlayer().hasPermission(Permissions.RIDE_WARP)){
            languageFile.sendMessage(player, languageFile.errorGeneralNoPermissionMessage);
            return false;
        }

        player.teleport(coasterHandle.getRide().getWarpLocation());
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
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == depth+1)
            return rideManager.getRideIdentifiers();
        return null;
    }
}
