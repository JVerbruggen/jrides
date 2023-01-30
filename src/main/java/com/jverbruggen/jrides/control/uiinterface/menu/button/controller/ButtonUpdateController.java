package com.jverbruggen.jrides.control.uiinterface.menu.button.controller;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.control.uiinterface.menu.button.RideControlButton;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class ButtonUpdateController {
    private List<ButtonUpdateState> buttonUpdateStateList;

    public ButtonUpdateController() {
        this.buttonUpdateStateList = new ArrayList<>();
        Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::tick, 1L, 1L).getTaskId();
    }

    public void addButton(RideControlButton button, int interval){
        buttonUpdateStateList.add(new ButtonUpdateState(button, interval));
    }

    private void tick() {
        buttonUpdateStateList.forEach(ButtonUpdateState::tick);
    }
}

class ButtonUpdateState{
    private final RideControlButton button;
    private final int interval;
    private int intervalState;

    ButtonUpdateState(RideControlButton button, int interval) {
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
