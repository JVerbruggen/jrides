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
