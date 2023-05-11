package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileTags;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public abstract class BaseControlMode implements ControlMode {
    protected final StationHandle stationHandle;
    protected final LanguageFile languageFile;
    protected TriggerContext triggerContext;
    protected DispatchLockCollection dispatchLockCollection;
    protected MinMaxWaitingTimer waitingTimer;
    protected Player operator;

    protected final long tickInterval;
    private boolean dispatchIntervalActive;
    private boolean started;

    private int tickIntervalState;

    protected BaseControlMode(StationHandle stationHandle, MinMaxWaitingTimer waitingTimer, DispatchLockCollection dispatchLockCollection, boolean activeOnStart) {
        this.languageFile = JRidesPlugin.getLanguageFile();

        this.stationHandle = stationHandle;
        this.triggerContext = null;
        this.dispatchLockCollection = dispatchLockCollection;
        this.waitingTimer = waitingTimer;

        this.dispatchIntervalActive = activeOnStart;
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
        if(dispatchIntervalActive) waitingTimer.increment(tickInterval);
    }

    @Override
    public boolean setOperator(Player newOperator) {
        if(newOperator == null){
            operator = null;
            return true;
        }
        if(newOperator.equals(operator)){
            return true;
        }
        if(operator == null){
            operator = newOperator;
            return true;
        }
        if(newOperator.getBukkitPlayer().hasPermission(Permissions.OPERATOR_OVERRIDE)){
            operator.getBukkitPlayer().closeInventory();
            languageFile.sendMessage(operator, languageFile.elevatedOperatorOverrideVictimMessage,
                    b -> b.add(LanguageFileTags.player, newOperator.getName()));
            operator.clearOperating();
            operator = newOperator;
            return true;
        }
        return false;
    }

    @Override
    public void setTriggerContext(TriggerContext triggerContext) {
        this.triggerContext = triggerContext;
    }

    @Override
    public TriggerContext getTriggerContext() {
        return triggerContext;
    }

    @Override
    public MinMaxWaitingTimer getWaitingTimer() {
        return waitingTimer;
    }

    @Override
    public void onVehicleArrive(Train train, StationHandle stationHandle) {
        waitingTimer.reset();
        dispatchIntervalActive = true;
    }

    @Override
    public void onVehicleDepart(Train train, StationHandle stationHandle) {
        waitingTimer.reset();
        dispatchIntervalActive = false;
    }

    @Override
    public StationHandle getStationHandle() {
        return stationHandle;
    }

    @Override
    public Player getOperator() {
        return operator;
    }

    @Override
    public boolean allowsAction(ControlAction action, Player player) {
        return player.equals(operator);
    }
}
