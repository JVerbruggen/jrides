/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.event.player.PlayerTeleportByJRidesEvent;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
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
        PlayerLocation warpLocation = rideHandle.getRide().getWarpLocation();
        if(warpLocation == null){
            languageFile.sendMessage(messageAgent, LanguageFileField.ERROR_CANNOT_WARP, (b) -> b.add(LanguageFileTag.rideDisplayName, rideHandle.getRide().getDisplayName()));
            return true;
        }

        PlayerTeleportByJRidesEvent.sendEvent(player, warpLocation, false);

        player.teleport(warpLocation);
        languageFile.sendMessage(player, LanguageFileField.NOTIFICATION_WARPED, b -> b.add(LanguageFileTag.rideDisplayName, rideHandle.getRide().getDisplayName()));
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
