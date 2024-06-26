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

import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.common.MenuSessionManager;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.menu.RideOverviewMenuFactory;
import org.bukkit.command.Command;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.List;

public class RidesCommandExecutor extends BaseCommandExecutor {
    private final RideOverviewMenuFactory rideOverviewMenuFactory;
    private final MenuSessionManager menuSessionManager;

    protected RidesCommandExecutor() {
        super(1);
        this.rideOverviewMenuFactory = ServiceProvider.getSingleton(RideOverviewMenuFactory.class);
        this.menuSessionManager = ServiceProvider.getSingleton(MenuSessionManager.class);
    }

    @Override
    public String getHelpMessageForParent() {
        return "/jrides "  + getCommand();
    }

    @Override
    public boolean onCommand(MessageAgent messageAgent, Command command, String arg, String[] args, CommandContext context) {
        if(!messageAgent.isPlayer()){
            languageFile.sendMessage(messageAgent, LanguageFileField.ERROR_PLAYER_COMMAND_ONLY_MESSAGE);
            return true;
        }

        if(args.length != 1){
            languageFile.sendMessage(messageAgent, getHelpMessageForSelf());
            return true;
        }

        Player player = messageAgent.getPlayer(playerManager);

        Menu menu = rideOverviewMenuFactory.getRideOverviewMenu();
        Inventory inventory = menu.getInventoryFor(player);
        menuSessionManager.addOpenMenu(player, menu, inventory);
        player.getBukkitPlayer().openInventory(inventory);

        return true;
    }

    @Override
    public String getHelpMessageForSelf() {
        return getHelpMessageForParent();
    }

    @Override
    public String getCommand() {
        return "rides";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_RIDES_MENU;
    }

    @Override
    public List<String> onTabComplete(MessageAgent messageAgent, Command command, String s, String[] strings) {
        if(!messageAgent.hasPermission(getPermission())) return Collections.emptyList();

        return null;
    }
}
