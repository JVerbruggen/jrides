package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;

public abstract class BaseControlMode implements ControlMode {
    protected final RideHandle rideHandle;
    protected final LanguageFile languageFile;
    protected TriggerContext triggerContext;
    protected MinMaxWaitingTimer waitingTimer;
    protected Player operator;

    protected final long tickInterval;

    private int tickIntervalState;

    protected BaseControlMode(RideHandle rideHandle, TriggerContext triggerContext, MinMaxWaitingTimer waitingTimer) {
        this.rideHandle = rideHandle;
        this.languageFile = JRidesPlugin.getLanguageFile();
        this.triggerContext = triggerContext;

        this.waitingTimer = waitingTimer;

        this.tickInterval = 5L;
        this.tickIntervalState = 0;
        this.operator = null;
    }

    @Override
    public void tick() {
        if(tickIntervalState < tickInterval-1){
            tickIntervalState++;
            return;
        }
        tickIntervalState = 0;

        incrementWaitingTimer();
    }

    protected void incrementWaitingTimer(){
        if(triggerContext.getVehiclePresentLock().isUnlocked()) waitingTimer.increment(tickInterval);
    }

    /**
     * Kicks current operator out of control mode.
     * Should only be used in combination with RideController,
     * since the RideController manages switching between
     * control modes.
     * @param newOperator
     * @return
     */
    @Override
    public boolean setOperator(Player newOperator) {
        if(newOperator == null){
            if(operator == null) return true;

            removeCurrentOperator();
            return true;
        }
        if(newOperator.equals(operator)){
            return true;
        }
        if(operator == null){
            operator = newOperator;
            return true;
        }
        if(newOperator.getBukkitPlayer().hasPermission(Permissions.ELEVATED_OPERATOR_OVERRIDE)){
            removeCurrentOperator();
            languageFile.sendMessage(operator, LanguageFileField.ELEVATED_OPERATOR_OVERRIDE_VICTIM_MESSAGE,
                    b -> b.add(LanguageFileTag.player, newOperator.getName()));
            operator = newOperator;
            return true;
        }
        return false;
    }

    private void removeCurrentOperator(){
        if(operator == null) return;

        operator.getBukkitPlayer().closeInventory();
        operator.clearOperating();
        operator = null;
    }

    @Override
    public MinMaxWaitingTimer getWaitingTimer() {
        return waitingTimer;
    }

    @Override
    public void onVehicleArrive(Vehicle vehicle, StationHandle stationHandle) {
        waitingTimer.reset();
    }

    @Override
    public void onVehicleDepart(Vehicle vehicle, StationHandle stationHandle) {
        waitingTimer.reset();
    }

    @Override
    public Player getOperator() {
        return operator;
    }

    @Override
    public boolean allowsAction(ControlAction action, Player player) {
        return player.equals(operator);
    }

    @Override
    public void setTriggerContext(TriggerContext triggerContext) {
        this.triggerContext = triggerContext;
    }
}
