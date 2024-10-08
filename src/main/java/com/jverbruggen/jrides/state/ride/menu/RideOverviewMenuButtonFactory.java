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

package com.jverbruggen.jrides.state.ride.menu;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.GlobalConfig;
import com.jverbruggen.jrides.control.uiinterface.menu.button.action.RunnableButtonAction;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.StatefulButtonVisual;
import com.jverbruggen.jrides.control.uiinterface.menu.button.common.StaticButtonVisual;
import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.menu.ButtonVisual;
import com.jverbruggen.jrides.models.menu.MenuButton;
import com.jverbruggen.jrides.models.menu.SimpleMenuButton;
import com.jverbruggen.jrides.models.menu.lore.LoreSet;
import com.jverbruggen.jrides.models.menu.lore.RideCounterLore;
import com.jverbruggen.jrides.models.menu.lore.TextLore;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.state.OpenState;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RideOverviewMenuButtonFactory {
    private final LanguageFile languageFile;

    public RideOverviewMenuButtonFactory() {
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
    }

    public MenuButton createRideStatusButton(RideHandle rideHandle, int slot){
        HashMap<OpenState, ButtonVisual> options = new HashMap<>();
        ItemStack displayItem = rideHandle.getRide().getDisplayItem();
        if(displayItem == null) displayItem = new ItemStack(Material.STONE, 1);

        ConfigManager configManager = ServiceProvider.getSingleton(ConfigManager.class);
        GlobalConfig globalConfig = configManager.getGlobalConfig();

        LoreSet baseLoreSet = LoreSet.fromStringList(rideHandle.getRide().getDisplayDescription());
        if(globalConfig.getRidesMenuConfig().shouldShowCounters()){
            baseLoreSet.add(new RideCounterLore(rideHandle));
        }

        LoreSet openLore = baseLoreSet.clone();
        LoreSet closedLore = baseLoreSet.clone();
        LoreSet maintenanceLore = baseLoreSet.clone();

        openLore.add(TextLore.from(""));
        openLore.add(TextLore.from(ChatColor.GREEN + languageFile.get(LanguageFileField.MENU_RIDE_OVERVIEW_STATUS_OPEN)));
        closedLore.add(TextLore.from(""));
        closedLore.add(TextLore.from(ChatColor.RED + languageFile.get(LanguageFileField.MENU_RIDE_OVERVIEW_STATUS_CLOSED)));
        maintenanceLore.add(TextLore.from(""));
        maintenanceLore.add(TextLore.from(ChatColor.RED + languageFile.get(LanguageFileField.MENU_RIDE_OVERVIEW_STATUS_CLOSED)));

        ButtonVisual openVisual = new StaticButtonVisual(displayItem, ChatColor.GOLD, rideHandle.getRide().getDisplayName(), openLore);
        ButtonVisual closedVisual = new StaticButtonVisual(displayItem, ChatColor.RED, rideHandle.getRide().getDisplayName(), closedLore);
        ButtonVisual maintenanceVisual = new StaticButtonVisual(displayItem, ChatColor.GRAY, rideHandle.getRide().getDisplayName(), maintenanceLore);

        options.put(OpenState.OPEN, openVisual);
        options.put(OpenState.CLOSED, closedVisual);
        options.put(OpenState.TRANSITION_TO_OPEN, closedVisual);
        options.put(OpenState.TRANSITION_TO_CLOSE, closedVisual);
        options.put(OpenState.MAINTENANCE, maintenanceVisual);
        options.put(OpenState.INACTIVE, maintenanceVisual);

        OpenState current = rideHandle.getState().getOpenState();
        if(current == null) current = OpenState.MAINTENANCE;

        return new SimpleMenuButton(
                new StatefulButtonVisual<>(() -> rideHandle.getState().getOpenState(), options, current),
                slot,
                new RunnableButtonAction(p -> {
                    PlayerLocation warpLocation = rideHandle.getRide().getWarpLocation();
                    if(warpLocation == null){
                        languageFile.sendMessage(p, LanguageFileField.ERROR_CANNOT_WARP, (b) -> b.add(LanguageFileTag.rideDisplayName, rideHandle.getRide().getDisplayName()));
                        return;
                    }
                    p.teleport(warpLocation);
                })
        );
    }
}