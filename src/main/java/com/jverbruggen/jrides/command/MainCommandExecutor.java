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

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.command.control.ControlCommandExecutor;
import org.bukkit.ChatColor;

public class MainCommandExecutor extends BaseCommandExecutor {
    public MainCommandExecutor() {
        super(0);
        registerSubCommand(new HelpCommandExecutor(this));
        registerSubCommand(new BlockSectionCommandExecutor());
        registerSubCommand(new VisualizeCommandExecutor());
        registerSubCommand(new ControlCommandExecutor());
        registerSubCommand(new WarpCommandExecutor());
        registerSubCommand(new RideOverviewMapCommandExecutor());
        registerSubCommand(new RideCounterMapCommandExecutor());
        registerSubCommand(new RidesCommandExecutor());
        registerSubCommand(new GenerateCommandExecutor());
    }

    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public String getHelpMessageForParent() {
        return ChatColor.GOLD + "This server is running jrides - " + JRidesPlugin.getVersion();
    }

    @Override
    public boolean canEveryoneRun() {
        return true;
    }
}
