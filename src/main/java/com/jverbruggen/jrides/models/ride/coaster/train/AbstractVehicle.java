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

package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.SoundCategory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractVehicle implements Vehicle {
    private final String name;
    private List<Player> passengers;
    private boolean crashed;
    private boolean debugMode;

    public AbstractVehicle(String name, boolean debugMode) {
        this.name = name;
        this.crashed = false;
        this.passengers = new ArrayList<>();
        this.debugMode = debugMode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setCrashed(boolean crashed) {
        this.crashed = crashed;
    }

    @Override
    public boolean isCrashed() {
        return crashed;
    }

    @Override
    public List<Player> getPassengers() {
        return passengers;
    }

    public void removePassenger(Player player){
        passengers.remove(player);
    }

    public void addPassenger(Player player){
        passengers.add(player);
    }

    @Override
    public void onPlayerEnter(Player player) {
        addPassenger(player);
    }

    @Override
    public void onPlayerExit(Player player) {
        removePassenger(player);
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    protected void playSound(String soundName){
        if(soundName == null) return;
        JRidesPlugin.getWorld().playSound(getCurrentLocation().toBukkitLocation(JRidesPlugin.getWorld()), soundName, SoundCategory.MASTER, 0.1f, 1f);
    }
}
