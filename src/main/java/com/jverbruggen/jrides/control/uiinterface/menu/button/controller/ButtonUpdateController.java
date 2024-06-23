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

package com.jverbruggen.jrides.control.uiinterface.menu.button.controller;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.models.menu.MenuButton;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class ButtonUpdateController {
    private List<ButtonUpdateState> buttonUpdateStateList;

    public ButtonUpdateController() {
        this.buttonUpdateStateList = new ArrayList<>();
        Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::tick, 1L, 1L).getTaskId();
    }

    public void addButton(MenuButton button, int interval){
        buttonUpdateStateList.add(new ButtonUpdateState(button, interval));
    }

    private void tick() {
        buttonUpdateStateList.forEach(ButtonUpdateState::tick);
    }
}

class ButtonUpdateState{
    private final MenuButton button;
    private final int interval;
    private int intervalState;

    ButtonUpdateState(MenuButton button, int interval) {
        this.button = button;
        this.interval = interval;
        this.intervalState = 0;
    }

    void tick(){
        intervalState++;
        if(intervalState < interval) return;
        intervalState = 0;

        boolean updated = button.getActiveVisual().update();
        if(updated){
            button.updateVisual();
        }
    }
}
