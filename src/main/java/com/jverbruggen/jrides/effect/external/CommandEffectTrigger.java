package com.jverbruggen.jrides.effect.external;

import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import org.bukkit.Bukkit;

public class CommandEffectTrigger extends BaseTrainEffectTrigger {
    private final String command;

    public CommandEffectTrigger(String command) {
        this.command = command;
    }

    @Override
    public boolean finishedPlaying() {
        return true;
    }

    @Override
    public boolean execute(Train train) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        return true;
    }
}
