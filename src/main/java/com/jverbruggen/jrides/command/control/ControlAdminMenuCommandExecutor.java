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
import com.jverbruggen.jrides.common.MenuSessionManager;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenuFactory;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.command.Command;
import org.bukkit.inventory.Inventory;

public class ControlAdminMenuCommandExecutor extends BaseCommandExecutor {
    private final RideControlMenuFactory rideControlMenuFactory;
    private final MenuSessionManager menuSessionManager;

    protected ControlAdminMenuCommandExecutor(int depth) {
        super(depth);
        this.rideControlMenuFactory = ServiceProvider.getSingleton(RideControlMenuFactory.class);
        this.menuSessionManager = ServiceProvider.getSingleton(MenuSessionManager.class);
    }

    @Override
    public boolean onCommand(MessageAgent messageAgent, Command command, String s, String[] args, CommandContext commandContext) {
        if(!messageAgent.isPlayer()){
            languageFile.sendMessage(messageAgent, LanguageFileField.ERROR_PLAYER_COMMAND_ONLY_MESSAGE, FeedbackType.CONFLICT);
            return true;
        }

        Player player = messageAgent.getPlayer(playerManager);

        RideHandle rideHandle = commandContext.get(RideHandle.class);
        Menu rideControlMenu = rideControlMenuFactory.getAdminMenu(rideHandle.getRideController());
        if(rideControlMenu == null){
            languageFile.sendMessage(player, LanguageFileField.ERROR_RIDE_CONTROL_MENU_NOT_FOUND, FeedbackType.CONFLICT);
            return true;
        }
        Inventory inventory = rideControlMenu.getInventoryFor(player);

        menuSessionManager.addOpenMenu(player, rideControlMenu, inventory);
        player.getBukkitPlayer().openInventory(inventory);
        return true;
    }

    @Override
    public String getCommand() {
        return "admin";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_ELEVATED_ADMIN_MENU;
    }

    @Override
    public String getHelpMessageForParent() {
        return "/jrides control <identifier> admin";
    }
}
