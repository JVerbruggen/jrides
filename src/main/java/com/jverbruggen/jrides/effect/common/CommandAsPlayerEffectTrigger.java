package com.jverbruggen.jrides.effect.common;

import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class CommandAsPlayerEffectTrigger extends BaseTrainEffectTrigger {
    private final String command;

    public CommandAsPlayerEffectTrigger(String command) {
        this.command = command;
    }

    @Override
    public boolean finishedPlaying() {
        return true;
    }

    @Override
    public void execute(Train train) {
        for(Player player : train.getPassengers()){
            player.getBukkitPlayer().performCommand(command);
        }
    }

    @Override
    public void executeReversed(Train train) {
        execute(train);
    }
}
