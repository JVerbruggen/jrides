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

package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.models.properties.PlayerLocation;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class AbstractRide implements Ride {
    private String identifier;
    private String displayName;
    private List<String> displayDescription;
    private ItemStack displayItem;
    private PlayerLocation warpLocation;
    private boolean canExitDuringRide;

    public AbstractRide(String identifier, String displayName, List<String> displayDescription, ItemStack displayItem, PlayerLocation warpLocation, boolean canExitDuringRide) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.displayDescription = displayDescription;
        this.displayItem = displayItem;
        this.warpLocation = warpLocation;
        this.canExitDuringRide = canExitDuringRide;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public ItemStack getDisplayItem() {
        return displayItem;
    }

    @Override
    public List<String> getDisplayDescription() {
        return displayDescription;
    }

    @Override
    public PlayerLocation getWarpLocation() {
        return warpLocation;
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public boolean canExitDuringRide() {
        return canExitDuringRide;
    }
}
