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

package com.jverbruggen.jrides.animator.flatride;

import com.jverbruggen.jrides.animator.flatride.rotor.controltype.ControlType;
import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.models.ride.flatride.PlayerControl;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPlayerControl implements PlayerControl {
    private List<JRidesPlayer> controlling;
    private final ControlType controlType;


    public AbstractPlayerControl(ControlType controlType) {
        this.controlType = controlType;
        this.controlling = null;
    }

    @Override
    public void addControlling(JRidesPlayer player) {
        if(controlling == null) controlling = new ArrayList<>();
        controlling.add(player);
    }

    @Override
    public void removeControlling(JRidesPlayer player) {
        if(controlling == null) return;
        controlling.remove(player);
        if(controlling.size() == 0) controlling = null;
    }

    @Override
    public void sendStartNotification() {
        if(controlling == null) return;

        String title = controlType.getControlMessageTitle();
        String subTitle = controlType.getControlMessageSubtitle();

        controlling.forEach(
                p -> p.sendTitle(title, subTitle, 60));
    }

    public ControlType getControlType() {
        return controlType;
    }

    @Override
    public void sendStopNotification() {

    }
}
