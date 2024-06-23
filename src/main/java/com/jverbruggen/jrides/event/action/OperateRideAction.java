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

package com.jverbruggen.jrides.event.action;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.common.MenuSessionManager;
import com.jverbruggen.jrides.common.Sync;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.inventory.Inventory;

public class OperateRideAction implements RideAction {
    @Override
    public void accept(VirtualEntity virtualEntity, RideHandle rideHandle, Player player) {
        Sync.runSynced(() -> acceptInternal(rideHandle, player));
    }

    private void acceptInternal(RideHandle rideHandle, Player player){
        MenuSessionManager menuSessionManager = ServiceProvider.getSingleton(MenuSessionManager.class);
        LanguageFile languageFile = ServiceProvider.getSingleton(LanguageFile.class);

        Menu rideControlMenu = rideHandle.getRideControlMenu();
        if(rideControlMenu == null){
            languageFile.sendMessage(player, LanguageFileField.ERROR_RIDE_CONTROL_MENU_NOT_FOUND, FeedbackType.CONFLICT);
            return;
        }
        Inventory inventory = rideControlMenu.getInventoryFor(player);

        menuSessionManager.addOpenMenu(player, rideControlMenu, inventory);
        player.getBukkitPlayer().openInventory(inventory);
    }
}
