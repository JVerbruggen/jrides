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

package com.jverbruggen.jrides.command.control;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.command.BaseCommandExecutor;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.command.Command;

import java.util.Collections;
import java.util.List;

public class ControlCommandExecutor extends BaseCommandExecutor {
    private final RideManager rideManager;

    public ControlCommandExecutor() {
        super(1);
        rideManager = ServiceProvider.getSingleton(RideManager.class);

        registerSubCommand(new ControlMenuCommandExecutor(depth+2));
        registerSubCommand(new ControlAdminMenuCommandExecutor(depth+2));
        registerSubCommand(new ControlDispatchCommandExecutor(depth+2));
    }

    @Override
    public boolean onCommand(MessageAgent messageAgent, Command command, String arg, String[] args, CommandContext commandContext) {
        if(args.length <= 2){
            languageFile.sendMultilineMessage(messageAgent, getHelpMessageForSelf());
            return true;
        }

        String identifier = args[1];
        String subCommand = args[2];

        RideHandle rideHandle = rideManager.getRideHandle(identifier);
        if(rideHandle == null){
            languageFile.sendMessage(messageAgent, "Ride '" + identifier + "' not found", FeedbackType.CONFLICT);
            return true;
        }

        commandContext.add(RideHandle.class, rideHandle);

        runSubCommand(messageAgent, command, arg, args, subCommand, commandContext);
        return true;
    }

    @Override
    public String getCommand() {
        return "control";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_CONTROL;
    }

    @Override
    public String getHelpMessageForParent() {
        return "/jrides control";
    }

    @Override
    public List<String> onTabComplete(MessageAgent messageAgent, Command command, String s, String[] strings) {
        if(!messageAgent.hasPermission(getPermission())) return Collections.emptyList();

        if(strings.length == depth+1){
            return rideManager.getRideIdentifiers();
        }else if(strings.length == depth+2){
            return getCommandSuggestions(messageAgent);
        }
        return null;
    }
}
