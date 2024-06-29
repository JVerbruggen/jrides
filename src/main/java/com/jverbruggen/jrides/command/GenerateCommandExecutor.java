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
import com.jverbruggen.jrides.animator.reference.RideReference;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.generate.GenerateType;
import com.jverbruggen.jrides.generate.TemplateGenerator;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.command.Command;

import java.util.Collections;
import java.util.List;

public class GenerateCommandExecutor extends BaseCommandExecutor {

    protected GenerateCommandExecutor() {
        super(1);
    }

    @Override
    public String getHelpMessageForParent() {
        return "/jrides generate <flatride/coaster> <identifier>";
    }

    @Override
    public boolean onCommand(MessageAgent messageAgent, Command command, String arg, String[] args, CommandContext context) {
        if(args.length != 3){
            languageFile.sendMessage(messageAgent, getHelpMessageForSelf());
            return true;
        }

        String typeString = args[1];
        GenerateType generateType = GenerateType.fromString(typeString);
        if(generateType == null){
            languageFile.sendMessage(messageAgent, "Generate type '" + typeString + "' does not exist", FeedbackType.CONFLICT);
            return true;
        }

        String identifier = args[2];

        // Identifier should not already exist
        RideReference rideReference = ServiceProvider.getSingleton(RideManager.class).getRideReference(identifier);
        if(rideReference != null){
            languageFile.sendMessage(messageAgent, "Ride '" + identifier + "' already exists! Use a new identifier.", FeedbackType.CONFLICT);
            return true;
        }

        // Generate ride template files
        TemplateGenerator templateGenerator = ServiceProvider.getSingleton(TemplateGenerator.class);
        templateGenerator.generate(generateType, identifier);

        languageFile.sendMessage(messageAgent, "Config files for ride '" + identifier + "' generated. RELOAD jrides to see its effect.", FeedbackType.INFO);
        return true;
    }

    @Override
    public String getHelpMessageForSelf() {
        return getHelpMessageForParent();
    }

    @Override
    public String getCommand() {
        return "generate";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_ELEVATED_VISUALIZE;
    }

    @Override
    public List<String> onTabComplete(MessageAgent messageAgent, Command command, String s, String[] strings) {
        if(!messageAgent.hasPermission(getPermission())) return Collections.emptyList();

        if(strings.length == depth+1)
            return List.of("flatride", "coaster");
        if(strings.length == depth+2)
            return List.of("identifier");

        return Collections.emptyList();
    }
}
