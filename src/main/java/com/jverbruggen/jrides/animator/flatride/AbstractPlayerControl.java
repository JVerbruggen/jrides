package com.jverbruggen.jrides.animator.flatride;

import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.models.ride.flatride.PlayerControl;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPlayerControl implements PlayerControl {
    private List<JRidesPlayer> controlling;

    public AbstractPlayerControl() {
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

        controlling.forEach(
                p -> p.sendTitle(ChatColor.RED + "Control the cup!", ChatColor.GOLD + "Press A or D to control the cup", 60));
    }

    @Override
    public void sendStopNotification() {

    }
}
