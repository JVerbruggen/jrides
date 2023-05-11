package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileTags;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;

public class SemiAutomaticMode extends BaseControlMode implements ControlMode {
    private Player operator;
    private final LanguageFile languageFile;

    public SemiAutomaticMode(StationHandle stationHandle, MinMaxWaitingTimer waitingTimer, DispatchLockCollection dispatchLockCollection) {
        super(stationHandle, waitingTimer, dispatchLockCollection);
        languageFile = JRidesPlugin.getLanguageFile();

        waitingTimer.setReachedTimeFunction(waitingTimer::reachedMinimum);

        this.operator = null;
    }

    @Override
    public void tick() {
        super.tick();

        MinMaxWaitingTimer waitingTimer = getWaitingTimer();

        Vehicle stationaryVehicle = stationHandle.getStationaryVehicle();
        if(stationaryVehicle != null){
            waitingTimer.sendGenericWaitingNotification(stationaryVehicle.getPassengers());
        }
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
    public boolean allowsAction(ControlAction action, Player player) {
        return player.equals(operator);
    }

    @Override
    public Player getOperator() {
        return operator;
    }
}
