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
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.models.menu.SimpleMenu;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

import java.util.List;

public class RideOverviewMenuFactory {
    private final LanguageFile languageFile;
    private final RideOverviewMenuButtonFactory rideOverviewMenuButtonFactory;
    private Menu rideOverviewMenu;

    public RideOverviewMenuFactory() {
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        this.rideOverviewMenuButtonFactory = ServiceProvider.getSingleton(RideOverviewMenuButtonFactory.class);
        this.rideOverviewMenu = null;
    }

    public Menu createRideOverviewMenu(List<RideHandle> rideHandles){
        int count = rideHandles.size();
        if(count > 9*6)
            // TODO: Large quantity of rides in menu support with pages
            throw new RuntimeException("Menus for a large quantity of rides is not yet supported.");

        SimpleMenu rideOverviewMenu = new SimpleMenu(3, languageFile.get(LanguageFileField.MENU_RIDE_OVERVIEW_TITLE));
        for(int i = 0; i < count; i++){
            rideOverviewMenu.addButton(rideOverviewMenuButtonFactory.createRideStatusButton(rideHandles.get(i), i));
        }

        this.rideOverviewMenu = rideOverviewMenu;

        return rideOverviewMenu;
    }

    public Menu getRideOverviewMenu(){
        if(this.rideOverviewMenu == null)
            throw new RuntimeException("Ride overview menu was not created yet, probably due to a previous error");

        return this.rideOverviewMenu;
    }
}
