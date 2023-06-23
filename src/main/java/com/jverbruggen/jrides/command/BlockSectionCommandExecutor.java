package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockSectionCommandExecutor extends BaseCommandExecutor {
    private final RideManager rideManager;

    protected BlockSectionCommandExecutor() {
        super(1);
        this.rideManager = ServiceProvider.getSingleton(RideManager.class);
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

        Track track = ((CoasterHandle)rideHandle).getTrack();
        if(track == null){
            languageFile.sendMessage(messageAgent, "Track was not found", FeedbackType.CONFLICT);
            return true;
        }

        ArrayList<Section> sections = new ArrayList<>(track.getSections());
        Collections.sort(sections);

        languageFile.sendMessage(messageAgent, "-- Block section occupations --");
        for(Section section : sections){
            boolean occupied = section.isOccupied();
            boolean reserved = section.getReservedBy() != null;
            boolean safe = section.getBlockSectionSafety(null).safe();

            ChatColor color = ChatColor.GREEN;
            if(occupied) color = ChatColor.RED;
            else if(reserved) color = ChatColor.DARK_AQUA;
            else if(!safe) color = ChatColor.YELLOW;

            languageFile.sendMessage(messageAgent, color + section.toString());
        }


        return true;
    }

    @Override
    public String getHelpMessageForSelf() {
        return getHelpMessageForParent();
    }

    @Override
    public String getCommand() {
        return "blocksections";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_ELEVATED_BLOCK_SECTION;
    }

    @Override
    public List<String> onTabComplete(MessageAgent messageAgent, Command command, String s, String[] strings) {
        if(!messageAgent.hasPermission(getPermission())) return Collections.emptyList();

        if(strings.length == depth+1)
            return rideManager.getRideIdentifiers();
        return null;
    }
}
