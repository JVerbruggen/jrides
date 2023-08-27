package com.jverbruggen.jrides.effect.external;

import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import org.bukkit.Bukkit;

public class CommandForPlayerEffectTrigger extends BaseTrainEffectTrigger {
    private final String command;

    public CommandForPlayerEffectTrigger(String command) {
        this.command = command;
    }

    @Override
    public boolean finishedPlaying() {
        return true;
    }

    @Override
    public void execute(Train train) {
        for(Player player : train.getPassengers()){
            String replacedCommand = command.replaceAll("%PLAYER%", player.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), replacedCommand);
        }
    }

    @Override
    public void executeReversed(Train train) {
        execute(train);
    }
}