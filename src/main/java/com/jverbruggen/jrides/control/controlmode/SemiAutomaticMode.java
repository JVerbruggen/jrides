package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.Seat;
import org.bukkit.Bukkit;

public class SemiAutomaticMode implements ControlMode {

    public SemiAutomaticMode() {
//        Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::tick, 5L, 5L);
    }

    public void tick(){

    }

    @Override
    public void setTriggerContext(TriggerContext triggerContext) {

    }

    @Override
    public void onPlayerEnter(Seat seat, Player player) {

    }

    @Override
    public void onPlayerExit(Seat seat, Player player) {

    }

    @Override
    public void startOperating() {

    }

    @Override
    public void stopOperating() {

    }

    @Override
    public void onDispatch() {

    }

    @Override
    public boolean allowsAction(ControlAction action) {
        return true;
    }
}
