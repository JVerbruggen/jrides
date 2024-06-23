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
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.trigger.SimpleDispatchTrigger;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import org.bukkit.command.Command;

public class ControlDispatchCommandExecutor extends BaseCommandExecutor {
    protected ControlDispatchCommandExecutor(int depth) {
        super(depth);
    }

    @Override
    public boolean onCommand(MessageAgent messageAgent, Command command, String s, String[] args, CommandContext commandContext) {
        RideHandle rideHandle = commandContext.get(RideHandle.class);
        RideController rideController = rideHandle.getRideController();
        if(rideController == null){
            languageFile.sendMessage(messageAgent, LanguageFileField.ERROR_RIDE_CONTROL_MENU_NOT_FOUND, FeedbackType.CONFLICT);
            return true;
        }

        SimpleDispatchTrigger dispatchTrigger = rideController.getTriggerContext().getDispatchTrigger();

        boolean dispatched = dispatchTrigger.execute(messageAgent);
        if(dispatched)
            languageFile.sendMessage(messageAgent, LanguageFileField.COMMAND_RIDE_DISPATCHED_MESSAGE,
                    b -> b.add(LanguageFileTag.rideDisplayName, rideHandle.getRide().getDisplayName()));

        return true;
    }

    @Override
    public String getCommand() {
        return "dispatch";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_CONTROL_DISPATCH;
    }

    @Override
    public String getHelpMessageForParent() {
        return "/jrides control <identifier> dispatch";
    }
}
