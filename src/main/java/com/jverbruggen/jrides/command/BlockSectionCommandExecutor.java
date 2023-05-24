package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.tool.ParticleTrackVisualisationTool;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFileFields;
import com.jverbruggen.jrides.language.LanguageFileTags;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    public boolean onCommand(CommandSender commandSender, Command command, String arg, String[] args, CommandContext context) {
        if(!(commandSender instanceof org.bukkit.entity.Player)){
            languageFile.sendMessage(commandSender, LanguageFileFields.ERROR_PLAYER_COMMAND_ONLY_MESSAGE);
            return true;
        }

        if(args.length != 2){
            languageFile.sendMessage(commandSender, getHelpMessageForSelf());
            return true;
        }

        String identifier = args[1];
        if(identifier.equalsIgnoreCase("")){
            languageFile.sendMessage(commandSender, getHelpMessageForSelf());
            return true;
        }

        CoasterHandle coasterHandle = ServiceProvider.getSingleton(RideManager.class).getRideHandle(identifier);
        ArrayList<Section> sections = new ArrayList<>(coasterHandle.getTrack().getSections());
        Collections.sort(sections);

        languageFile.sendMessage(commandSender, "-- Block section occupations --");
        for(Section section : sections){
            boolean occupied = section.isOccupied();
            boolean reserved = section.getReservedBy() != null;
            boolean safe = section.isBlockSectionSafe(null);

            ChatColor color = ChatColor.GREEN;
            if(occupied) color = ChatColor.RED;
            else if(reserved) color = ChatColor.DARK_AQUA;
            else if(!safe) color = ChatColor.YELLOW;

            languageFile.sendMessage(commandSender, color + section.toString());
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
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == depth+1)
            return rideManager.getRideIdentifiers();
        return null;
    }
}
