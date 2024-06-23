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

package com.jverbruggen.jrides.control.uiinterface.menu.open;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.common.MenuSessionManager;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class SignMenuListener implements Listener {
    private final String secondLineTriggerText;
    private final LanguageFile languageFile;
    private final RideManager rideManager;
    private final PlayerManager playerManager;
    private final MenuSessionManager menuSessionManager;

    public SignMenuListener(String secondLineTriggerText) {
        this.secondLineTriggerText = secondLineTriggerText;
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        this.rideManager = ServiceProvider.getSingleton(RideManager.class);
        this.playerManager = ServiceProvider.getSingleton(PlayerManager.class);
        this.menuSessionManager = ServiceProvider.getSingleton(MenuSessionManager.class);
    }

    /**
     * Sign format:
     * <br/>---
     * <br/>
     * <br/>[< ride_identifier >]
     * <br/>Control panel
     * <br/>
     * <br/>---
     * @param event
     */
    @EventHandler
    public void onSignClick(PlayerInteractEvent event){
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;


        Block block = event.getClickedBlock();
        BlockData blockData = block.getBlockData();
        if(!(blockData instanceof WallSign)) return;

        Sign sign = (Sign) block.getState();
        String lineText = ChatColor.stripColor(sign.getLine(2));
        if(!lineText.equalsIgnoreCase(secondLineTriggerText)) return;

        String identifierRaw = sign.getLine(1);
        String identifier = identifierRaw
                .replace("[", "")
                .replace("]", "");

        Player player = playerManager.getPlayer(event.getPlayer());
        if(!player.hasPermission(Permissions.CABIN_OPERATE)){
            JRidesPlugin.getLanguageFile().sendMessage(player, LanguageFileField.ERROR_OPERATING_NO_PERMISSION);
            return;
        }


        RideHandle rideHandle = rideManager.getRideHandle(identifier);
        if(rideHandle == null) return;

        Menu rideControlMenu = rideHandle.getRideControlMenu();
        if(rideControlMenu == null){
            languageFile.sendMessage(player, LanguageFileField.ERROR_RIDE_CONTROL_MENU_NOT_FOUND, FeedbackType.CONFLICT);
            return;
        }

        Inventory inventory = rideControlMenu.getInventoryFor(player);

        menuSessionManager.addOpenMenu(player, rideControlMenu, inventory);
        player.getBukkitPlayer().openInventory(inventory);
        player.playSound(Sound.UI_BUTTON_CLICK);
    }
}
