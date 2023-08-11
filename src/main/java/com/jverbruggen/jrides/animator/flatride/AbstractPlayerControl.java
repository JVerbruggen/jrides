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
